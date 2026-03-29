package sn.yegg.app.service.impl;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.AlerteLigneArret;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.service.AlertDetectionService;
import sn.yegg.app.service.NotificationService;
import sn.yegg.app.utils.GeoUtils;

@Service
@Transactional
public class AlertDetectionServiceImpl implements AlertDetectionService {

    private final AlerteLigneArretRepository alerteLigneArretRepository;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;

    public AlertDetectionServiceImpl(
        AlerteLigneArretRepository alerteLigneArretRepository,
        WebSocketService webSocketService,
        NotificationService notificationService
    ) {
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.webSocketService = webSocketService;
        this.notificationService = notificationService;
    }

    public void checkApproachAlerts(Bus bus) {
        if (bus.getLigne() == null) {
            return;
        }

        // Récupérer TOUTES les alertes actives pour cette ligne avec les utilisateurs
        List<AlerteLigneArret> alertes = alerteLigneArretRepository.findActiveAlertsWithUsersByLigne(bus.getLigne().getId());

        for (AlerteLigneArret alerte : alertes) {
            Arret arret = alerte.getArret();
            AlerteApproche approche = alerte.getAlerteApproche();
            Utilisateur utilisateur = approche.getUtilisateur();

            // Calculer la distance
            double distance = GeoUtils.distanceMeters(
                bus.getCurrentLatitude().doubleValue(),
                bus.getCurrentLongitude().doubleValue(),
                arret.getLatitude().doubleValue(),
                arret.getLongitude().doubleValue()
            );

            // Vérifier les conditions
            boolean trigger = false;
            String triggeredBy = "DISTANCE";
            int timeToArrival = 0;

            // Calculer le temps d'arrivée si nécessaire
            if (bus.getCurrentVitesse() != null && bus.getCurrentVitesse().doubleValue() > 0) {
                double vitesseMs = (bus.getCurrentVitesse().doubleValue() * 1000) / 3600;
                timeToArrival = (int) (distance / vitesseMs);
            }

            // Vérifier selon le type de seuil
            switch (approche.getTypeSeuil()) {
                case DISTANCE:
                    trigger = distance <= approche.getSeuilDistance();
                    break;
                case TIME:
                    trigger = approche.getSeuilTemps() != null && timeToArrival > 0 && timeToArrival <= approche.getSeuilTemps();
                    triggeredBy = "TIME";
                    break;
                case OR_BOTH:
                    boolean distanceOk = distance <= approche.getSeuilDistance();
                    boolean timeOk = approche.getSeuilTemps() != null && timeToArrival > 0 && timeToArrival <= approche.getSeuilTemps();
                    trigger = distanceOk || timeOk;
                    triggeredBy = distanceOk ? "DISTANCE" : "TIME";
                    break;
                case AND_BOTH:
                    trigger =
                        distance <= approche.getSeuilDistance() &&
                        approche.getSeuilTemps() != null &&
                        timeToArrival > 0 &&
                        timeToArrival <= approche.getSeuilTemps();
                    triggeredBy = "BOTH";
                    break;
            }

            if (trigger) {
                // ENVOYER LA NOTIFICATION À L'UTILISATEUR SPÉCIFIQUE
                sendAlertToUser(bus, arret, approche, utilisateur, distance, timeToArrival, triggeredBy);

                // Mettre à jour le compteur
                approche.setNombreDeclenchements(approche.getNombreDeclenchements() + 1);
                approche.setDernierDeclenchement(Instant.now());
            }
        }
    }

    private void sendAlertToUser(
        Bus bus,
        Arret arret,
        AlerteApproche approche,
        Utilisateur utilisateur,
        double distance,
        int timeToArrival,
        String triggeredBy
    ) {
        // Préparer les données pour WebSocket (pour l'interface en temps réel)
        Map<String, Object> alertData = new HashMap<>();
        alertData.put("type", "BUS_APPROACHING");
        alertData.put("busNumero", bus.getNumeroVehicule());
        alertData.put("busId", bus.getId());
        alertData.put("ligne", bus.getLigne().getNumero());
        alertData.put("ligneId", bus.getLigne().getId());
        alertData.put("arret", arret.getNom());
        alertData.put("arretId", arret.getId());
        alertData.put("distance", Math.round(distance));
        alertData.put("minutes", timeToArrival > 0 ? Math.max(1, timeToArrival / 60) : 2);
        alertData.put("triggeredBy", triggeredBy);
        alertData.put("timestamp", Instant.now().toString());

        // 1. ENVOI WEBSOCKET SPÉCIFIQUE À L'UTILISATEUR
        // Format: /user/{userId}/queue/alerts
        webSocketService.sendPersonalizedAlert(utilisateur.getId().toString(), "/queue/alerts", alertData);

        // 2. NOTIFICATION PUSH (FCM) SI TOKEN DISPONIBLE
        if (utilisateur.getFcmToken() != null && !utilisateur.getFcmToken().isEmpty()) {
            String title = "Bus " + bus.getLigne().getNumero() + " en approche";
            String body = String.format(
                "Le bus %s arrive à %s dans %d minutes",
                bus.getNumeroVehicule(),
                arret.getNom(),
                timeToArrival > 0 ? Math.max(1, timeToArrival / 60) : 2
            );

            notificationService.sendPushNotification(
                utilisateur.getFcmToken(),
                title,
                body
                // alertData
            );
        }

        // 3. BROADCAST POUR LA CARTE PUBLIQUE (optionnel)
        Map<String, Object> publicAlert = new HashMap<>();
        publicAlert.put("type", "BUS_APPROACHING");
        publicAlert.put("busNumero", bus.getNumeroVehicule());
        publicAlert.put("ligne", bus.getLigne().getNumero());
        publicAlert.put("arret", arret.getNom());
        publicAlert.put("minutes", timeToArrival > 0 ? Math.max(1, timeToArrival / 60) : 2);

        webSocketService.sendBusAlert("BUS_APPROACHING", "Bus " + bus.getNumeroVehicule() + " arrive bientôt", bus.getId());
    }
}
