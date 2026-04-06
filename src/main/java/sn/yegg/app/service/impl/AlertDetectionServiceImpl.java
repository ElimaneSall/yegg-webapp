package sn.yegg.app.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.yegg.app.domain.*;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.repository.HistoriqueAlerteRepository;
import sn.yegg.app.service.AlertDetectionService;
import sn.yegg.app.service.NotificationService;

@Service
@Transactional
public class AlertDetectionServiceImpl implements AlertDetectionService {

    private final AlerteLigneArretRepository alerteLigneArretRepository;
    private final HistoriqueAlerteRepository historiqueAlerteRepository;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;
    private static final double EARTH_RADIUS = 6371000;

    private final Logger log = LoggerFactory.getLogger(AlertDetectionServiceImpl.class);

    public AlertDetectionServiceImpl(
        AlerteLigneArretRepository alerteLigneArretRepository,
        HistoriqueAlerteRepository historiqueAlerteRepository,
        WebSocketService webSocketService,
        NotificationService notificationService
    ) {
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.webSocketService = webSocketService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public void checkApproachAlerts(Bus bus) {
        // 🚪 Entry point
        log.info(
            "🔍 [ALERT-CHECK] Début vérification pour bus={} ligne={}",
            bus.getNumeroVehicule(),
            bus.getLigne() != null ? bus.getLigne().getId() : "NULL"
        );
        log.debug(
            "   📍 Position bus: lat={}, lng={}, vitesse={} km/h",
            bus.getCurrentLatitude(),
            bus.getCurrentLongitude(),
            bus.getCurrentVitesse()
        );

        // ✅ Pré-requis
        if (bus.getLigne() == null || bus.getCurrentLatitude() == null) {
            log.warn("   ⚠️  Pré-requis non satisfaits (ligne ou latitude nulle)");
            return;
        }

        // 📦 Récupération des alertes
        List<AlerteLigneArret> alertes = alerteLigneArretRepository.findActiveAlertsWithUsersByLigne(bus.getLigne().getId());

        log.info("   📦 Alertes actives trouvées pour ligne {}: {}", bus.getLigne().getId(), alertes.size());

        if (alertes.isEmpty()) {
            log.debug("   ℹ️  Aucune alerte configurée pour cette ligne");
            return;
        }

        int processedCount = 0;
        int triggeredCount = 0;

        // 🔁 Boucle sur chaque association alerte/ligne/arrêt
        for (AlerteLigneArret alerteLigneArret : alertes) {
            processedCount++;
            log.debug("   ── [{}] Traitement alerteLigneArret ID={}", processedCount, alerteLigneArret.getId());

            // ⚙️ Vérification statut actif
            if (!Boolean.TRUE.equals(alerteLigneArret.getActif())) {
                log.debug("      ⏭️  SKIP: association inactive (actif=false)");
                continue;
            }

            Arret arret = alerteLigneArret.getArret();
            AlerteApproche alerte = alerteLigneArret.getAlerteApproche();

            log.debug("      🎯 Alerte: '{}' (ID={}), Arrêt: '{}' (ID={})", alerte.getNom(), alerte.getId(), arret.getNom(), arret.getId());
            log.debug("         📍 Arrêt position: lat={}, lng={}", arret.getLatitude(), arret.getLongitude());

            // 🕐 Vérification fenêtre temporelle
            if (!isAlertActiveNow(alerte)) {
                log.debug("      ⏭️  SKIP: alerte inactive (jour/heure hors plage)");
                log.debug(
                    "         Statut={}, Jours={}, Heure={}-{}",
                    alerte.getStatut(),
                    alerte.getJoursActivation(),
                    alerte.getHeureDebut(),
                    alerte.getHeureFin()
                );
                continue;
            }
            log.debug("✅ Alert active in time window");

            // 📏 Calcul distance
            double distance = calculateDistance(
                bus.getCurrentLatitude().doubleValue(),
                bus.getCurrentLongitude().doubleValue(),
                arret.getLatitude().doubleValue(),
                arret.getLongitude().doubleValue()
            );
            log.debug("📏 Distance calculée: {:.1f} mètres={}", distance);

            // ⚡ Évaluation des seuils
            ThresholdCheckResult result = evaluateThresholds(alerte, distance, bus);
            log.debug(
                "      🎯 Résultat seuils: triggered={}, by={}, distanceValue={}, timeValue={}",
                result.isTriggered(),
                result.getTriggeredBy(),
                result.getDistanceValue(),
                result.getTimeValue()
            );

            // 🚨 Déclenchement si conditions remplies
            if (result.isTriggered()) {
                triggeredCount++;
                log.info(
                    "      🚨 ALERT TRIGGERED: '{}' pour bus {} à l'arrêt '{}' ({}m, déclenché par {})",
                    alerte.getNom(),
                    bus.getNumeroVehicule(),
                    arret.getNom(),
                    distance,
                    result.getTriggeredBy()
                );

                triggerAlert(alerte, bus, arret, distance, result);
            } else {
                log.debug("      ❌ No trigger: conditions non satisfaites");
            }
        }

        // 🏁 Summary
        log.info(
            "✅ [ALERT-CHECK] Fin: {} alertes traitées, {} déclenchées pour bus {}",
            processedCount,
            triggeredCount,
            bus.getNumeroVehicule()
        );
    }

    private boolean isAlertActiveNow(AlerteApproche alerte) {
        log.debug("🕐 Checking time window for alert '{}'", alerte.getNom());

        // 1. Vérifier statut
        if (!AlertStatus.ACTIVE.equals(alerte.getStatut())) {
            log.debug("   ❌ Statut != ACTIVE (statut={})", alerte.getStatut());
            return false;
        }

        // 2. Vérifier jour de la semaine (format français : L,M,M,J,V,S,D)
        if (alerte.getJoursActivation() != null && !alerte.getJoursActivation().isEmpty()) {
            String todayFr = getFrenchDayAbbreviation();
            boolean dayMatch = alerte.getJoursActivation().contains(todayFr);

            log.debug(
                "   📅 Jour actuel: {} ({}), Jours activés: '{}', match: {}",
                todayFr,
                LocalDate.now().getDayOfWeek(),
                alerte.getJoursActivation(),
                dayMatch
            );

            if (!dayMatch) {
                return false;
            }
        }

        // 3. Vérifier plage horaire
        if (alerte.getHeureDebut() != null && alerte.getHeureFin() != null) {
            LocalTime now = LocalTime.now();
            LocalTime debut = LocalTime.parse(alerte.getHeureDebut());
            LocalTime fin = LocalTime.parse(alerte.getHeureFin());
            boolean inTimeRange = !now.isBefore(debut) && !now.isAfter(fin);

            log.debug("   🕐 Heure: {}, Plage: {}-{}, dans la plage: {}", now, alerte.getHeureDebut(), alerte.getHeureFin(), inTimeRange);

            if (!inTimeRange) {
                return false;
            }
        }

        log.debug("   ✅ Alert is active");
        return true;
    }

    /**
     * Convertit le jour Java en abréviation française 1 lettre
     * L=Mardi=Mercredi=M, donc on retourne "M" pour les deux
     */
    private String getFrenchDayAbbreviation() {
        return switch (LocalDate.now().getDayOfWeek()) {
            case MONDAY -> "L";
            case TUESDAY, WEDNESDAY -> "M"; // Les deux sont "M" en français
            case THURSDAY -> "J";
            case FRIDAY -> "V";
            case SATURDAY -> "S";
            case SUNDAY -> "D";
        };
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    private ThresholdCheckResult evaluateThresholds(AlerteApproche alerte, double distance, Bus bus) {
        ThresholdCheckResult result = new ThresholdCheckResult();
        result.setDistanceValue(distance);

        // 📏 Distance check
        boolean distanceOk = false;
        if (alerte.getSeuilDistance() != null) {
            distanceOk = distance <= alerte.getSeuilDistance();
            log.debug("   📏 Distance: {}m <= {}m ? → {}", distance, alerte.getSeuilDistance(), distanceOk);
        }

        // ⏱️ Time check
        boolean timeOk = false;
        Integer timeToArrival = null;

        if (alerte.getSeuilTemps() != null) {
            BigDecimal vitesse = bus.getCurrentVitesse();
            log.debug("   🚀 Vitesse bus: {} km/h", vitesse);

            if (vitesse != null && vitesse.doubleValue() > 0) {
                double vitesseMs = (vitesse.doubleValue() * 1000) / 3600;
                timeToArrival = (int) (distance / vitesseMs);
                timeOk = timeToArrival <= alerte.getSeuilTemps();
                result.setTimeValue(timeToArrival);
                log.debug("   ⏱️ Temps d'arrivée: {}s <= {}s ? → {}", timeToArrival, alerte.getSeuilTemps(), timeOk);
            } else {
                log.debug("   ⚠️ Vitesse nulle ou nulle, time check skipped");
            }
        }

        // 🔀 Type de seuil
        log.debug("   🔀 TypeSeuil: {}, distanceOk: {}, timeOk: {}", alerte.getTypeSeuil(), distanceOk, timeOk);

        switch (alerte.getTypeSeuil()) {
            case DISTANCE:
                if (distanceOk) {
                    result.setTriggered(true);
                    result.setTriggeredBy("DISTANCE");
                    log.debug("   ✅ TRIGGERED by DISTANCE");
                }
                break;
            case TIME:
                if (timeOk) {
                    result.setTriggered(true);
                    result.setTriggeredBy("TIME");
                    log.debug("   ✅ TRIGGERED by TIME");
                }
                break;
            case OR_BOTH:
                if (distanceOk || timeOk) {
                    result.setTriggered(true);
                    result.setTriggeredBy(distanceOk ? "DISTANCE" : "TIME");
                    log.debug("   ✅ TRIGGERED by OR_BOTH ({})", result.getTriggeredBy());
                } else {
                    log.debug("   ❌ NOT triggered: distanceOk={}, timeOk={}", distanceOk, timeOk);
                }
                break;
            case AND_BOTH:
                if (distanceOk && timeOk) {
                    result.setTriggered(true);
                    result.setTriggeredBy("BOTH");
                    log.debug("   ✅ TRIGGERED by AND_BOTH");
                }
                break;
        }

        log.debug("   🎯 Final: triggered={}, by={}", result.isTriggered(), result.getTriggeredBy());
        return result;
    }

    private void triggerAlert(AlerteApproche alerte, Bus bus, Arret arret, double distance, ThresholdCheckResult result) {
        // Sauvegarder dans l'historique
        HistoriqueAlerte historique = new HistoriqueAlerte();
        historique.setAlerteApproche(alerte);
        historique.setBus(bus);
        historique.setBusNumero(bus.getNumeroVehicule());
        historique.setDateDeclenchement(Instant.now());
        historique.setDistanceReelle((int) Math.round(distance));
        historique.setTempsReel(result.getTimeValue());
        historique.setTypeDeclenchement(ThresholdType.valueOf(result.getTriggeredBy()));
        historique.setNotificationEnvoyee(true);
        historiqueAlerteRepository.save(historique);

        // Incrémenter le compteur
        alerte.setNombreDeclenchements(alerte.getNombreDeclenchements() + 1);
        alerte.setDernierDeclenchement(Instant.now());

        // Envoyer notifications
        sendNotifications(alerte, bus, arret, distance, result);
    }

    private void sendNotifications(AlerteApproche alerte, Bus bus, Arret arret, double distance, ThresholdCheckResult result) {
        Utilisateur user = alerte.getUtilisateur();
        if (user == null) return;

        int minutes = result.getTimeValue() != null && result.getTimeValue() > 0 ? Math.max(1, result.getTimeValue() / 60) : 1;

        // WebSocket personnel
        Map<String, Object> wsData = Map.of(
            "type",
            "BUS_APPROACHING",
            "alertId",
            alerte.getId(),
            "busNumero",
            bus.getNumeroVehicule(),
            "ligne",
            bus.getLigne().getNumero(),
            "arret",
            arret.getNom(),
            "distance",
            Math.round(distance),
            "minutes",
            minutes,
            "triggeredBy",
            result.getTriggeredBy(),
            "timestamp",
            Instant.now().toString()
        );
        webSocketService.sendPersonalizedAlert(user.getId().toString(), "/queue/alerts", wsData);

        // Push notification FCM
        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty() && Boolean.TRUE.equals(user.getNotificationsPush())) {
            notificationService.sendPushNotification(
                user.getFcmToken(),
                "🚌 Bus " + bus.getLigne().getNumero() + " en approche",
                String.format(
                    "Le bus %s arrive à %s dans ~%d min (%dm)",
                    bus.getNumeroVehicule(),
                    arret.getNom(),
                    minutes,
                    Math.round(distance)
                )
            );
        }
    }

    // Classe interne pour le résultat
    public static class ThresholdCheckResult {

        private boolean triggered;
        private Double distanceValue;
        private Integer timeValue;
        private String triggeredBy;

        // getters/setters...
        public boolean isTriggered() {
            return triggered;
        }

        public void setTriggered(boolean t) {
            this.triggered = t;
        }

        public Double getDistanceValue() {
            return distanceValue;
        }

        public void setDistanceValue(Double d) {
            this.distanceValue = d;
        }

        public Integer getTimeValue() {
            return timeValue;
        }

        public void setTimeValue(Integer t) {
            this.timeValue = t;
        }

        public String getTriggeredBy() {
            return triggeredBy;
        }

        public void setTriggeredBy(String t) {
            this.triggeredBy = t;
        }
    }
}
