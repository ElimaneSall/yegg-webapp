package sn.yegg.app.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.*;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.*;
import sn.yegg.app.service.AlerteApprocheService;
import sn.yegg.app.service.NotificationService;
import sn.yegg.app.service.dto.AlertCheckRequest;
import sn.yegg.app.service.dto.AlertCheckResponse;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.mapper.AlerteApprocheMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.AlerteApproche}.
 */
@Service
@Transactional
public class AlerteApprocheServiceImpl implements AlerteApprocheService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteApprocheServiceImpl.class);

    private final AlerteApprocheRepository alerteApprocheRepository;

    private final AlerteApprocheMapper alerteApprocheMapper;
    private final AlerteLigneArretRepository alerteLigneArretRepository;
    private final HistoriqueAlerteRepository historiqueAlerteRepository;
    private final BusRepository busRepository;
    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final NotificationService notificationService;

    // Rayon de la Terre en mètres
    private static final double EARTH_RADIUS = 6371000;

    public AlerteApprocheServiceImpl(
        AlerteApprocheRepository alerteApprocheRepository,
        AlerteApprocheMapper alerteApprocheMapper,
        AlerteLigneArretRepository alerteLigneArretRepository,
        HistoriqueAlerteRepository historiqueAlerteRepository,
        BusRepository busRepository,
        LigneRepository ligneRepository,
        ArretRepository arretRepository,
        NotificationService notificationService
    ) {
        this.alerteApprocheRepository = alerteApprocheRepository;
        this.alerteApprocheMapper = alerteApprocheMapper;
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.busRepository = busRepository;
        this.ligneRepository = ligneRepository;
        this.arretRepository = arretRepository;
        this.notificationService = notificationService;
    }

    @Override
    public AlerteApprocheDTO save(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to save AlerteApproche : {}", alerteApprocheDTO);
        AlerteApproche alerteApproche = alerteApprocheMapper.toEntity(alerteApprocheDTO);
        alerteApproche = alerteApprocheRepository.save(alerteApproche);
        return alerteApprocheMapper.toDto(alerteApproche);
    }

    @Override
    public AlerteApprocheDTO update(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to update AlerteApproche : {}", alerteApprocheDTO);
        AlerteApproche alerteApproche = alerteApprocheMapper.toEntity(alerteApprocheDTO);
        alerteApproche = alerteApprocheRepository.save(alerteApproche);
        return alerteApprocheMapper.toDto(alerteApproche);
    }

    @Override
    public Optional<AlerteApprocheDTO> partialUpdate(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to partially update AlerteApproche : {}", alerteApprocheDTO);

        return alerteApprocheRepository
            .findById(alerteApprocheDTO.getId())
            .map(existingAlerteApproche -> {
                alerteApprocheMapper.partialUpdate(existingAlerteApproche, alerteApprocheDTO);

                return existingAlerteApproche;
            })
            .map(alerteApprocheRepository::save)
            .map(alerteApprocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlerteApprocheDTO> findOne(Long id) {
        LOG.debug("Request to get AlerteApproche : {}", id);
        return alerteApprocheRepository.findById(id).map(alerteApprocheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AlerteApproche : {}", id);
        alerteApprocheRepository.deleteById(id);
    }

    /**
     * Vérifie si un bus déclenche des alertes en fonction de sa position
     */
    // Dans AlerteApprocheServiceImpl.java, modifions la méthode checkAlerts

    @Transactional
    public AlertCheckResponse checkAlerts(AlertCheckRequest request) {
        LOG.debug(
            "Vérification des alertes pour le bus {} à la position ({}, {})",
            request.getBusId(),
            request.getLatitude(),
            request.getLongitude()
        );

        AlertCheckResponse response = new AlertCheckResponse();
        response.setCheckTimestamp(Instant.now());
        response.setAlertTriggered(false);

        // Récupérer le bus
        Bus bus = busRepository
            .findById(request.getBusId())
            .orElseThrow(() -> new RuntimeException("Bus non trouvé avec l'ID: " + request.getBusId()));

        LOG.info("Bus trouvé: {}, ligne: {}", bus.getNumeroVehicule(), bus.getLigne() != null ? bus.getLigne().getId() : "null");

        // Récupérer la ligne du bus
        if (bus.getLigne() == null) {
            LOG.debug("Le bus {} n'est pas assigné à une ligne", request.getBusId());
            return response;
        }

        // Récupérer toutes les alertes actives
        List<AlerteApproche> alertesActives = alerteApprocheRepository.findByStatut(AlertStatus.ACTIVE);
        LOG.info("Nombre d'alertes actives trouvées: {}", alertesActives.size());

        for (AlerteApproche alerte : alertesActives) {
            LOG.info(
                "Alerte trouvée: id={}, nom={}, seuil={}, type={}",
                alerte.getId(),
                alerte.getNom(),
                alerte.getSeuilDistance(),
                alerte.getTypeSeuil()
            );
        }

        // Récupérer tous les arrêts de la ligne
        List<Arret> arrets = arretRepository.findByLigneIdOrderByLigneArretOrdre(bus.getLigne().getId());
        LOG.info("Nombre d'arrêts sur la ligne {}: {}", bus.getLigne().getId(), arrets.size());

        // Vérifier chaque alerte
        List<AlertCheckResponse.TriggeredAlert> triggeredAlerts = new ArrayList<>();

        for (AlerteApproche alerte : alertesActives) {
            LOG.info("Traitement de l'alerte: {}", alerte.getId());

            // Vérifier si l'alerte est active dans la période actuelle
            if (!isAlertActiveInCurrentTime(alerte)) {
                LOG.info("Alerte {} non active dans la période actuelle", alerte.getId());
                continue;
            }

            // Récupérer les associations ligne-arrêt pour cette alerte
            List<AlerteLigneArret> associations = alerteLigneArretRepository.findByAlerteApprocheId(alerte.getId());

            LOG.info("Nombre d'associations pour l'alerte {}: {}", alerte.getId(), associations.size());

            for (AlerteLigneArret association : associations) {
                LOG.info(
                    "Association: alerteLigneArret id={}, arretId={}, ligneId={}, actif={}",
                    association.getId(),
                    association.getArret() != null ? association.getArret().getId() : null,
                    association.getLigne() != null ? association.getLigne().getId() : null,
                    association.getActif()
                );

                // Vérifier si cette association est active
                if (association.getActif() != null && !association.getActif()) {
                    LOG.info("Association {} inactive", association.getId());
                    continue;
                }

                // Trouver l'arrêt correspondant
                Arret arretCible = findArretById(arrets, association.getArret() != null ? association.getArret().getId() : null);

                if (arretCible == null) {
                    LOG.info("Arrêt non trouvé pour l'association {}", association.getId());
                    continue;
                }

                LOG.info(
                    "Arrêt cible: id={}, nom={}, position=({}, {})",
                    arretCible.getId(),
                    arretCible.getNom(),
                    arretCible.getLatitude(),
                    arretCible.getLongitude()
                );

                // Calculer la distance entre le bus et l'arrêt
                double distance = calculateDistance(
                    request.getLatitude(),
                    request.getLongitude(),
                    arretCible.getLatitude().doubleValue(),
                    arretCible.getLongitude().doubleValue()
                );

                LOG.info("Distance calculée entre bus et arrêt {}: {} m", arretCible.getId(), distance);

                // Vérifier les seuils
                ThresholdCheckResult thresholdResult = checkThresholds(alerte, distance, request, arretCible);

                LOG.info(
                    "Résultat du check: triggered={}, triggeredBy={}, distanceValue={}, timeValue={}",
                    thresholdResult.isTriggered(),
                    thresholdResult.getTriggeredBy(),
                    thresholdResult.getDistanceValue(),
                    thresholdResult.getTimeValue()
                );

                if (thresholdResult.isTriggered()) {
                    // Créer l'entrée dans l'historique
                    HistoriqueAlerte historique = createHistoriqueAlerte(
                        alerte,
                        bus,
                        distance,
                        thresholdResult.getTimeValue(),
                        thresholdResult.getTriggeredBy()
                    );
                    historiqueAlerteRepository.save(historique);

                    // Envoyer une notification
                    notificationService.sendAlertNotification(alerte.getUtilisateur(), bus, arretCible, distance, thresholdResult);

                    // Ajouter à la réponse
                    AlertCheckResponse.TriggeredAlert triggered = new AlertCheckResponse.TriggeredAlert();
                    triggered.setAlertId(alerte.getId());
                    triggered.setAlertName(alerte.getNom());
                    triggered.setThresholdType(alerte.getTypeSeuil().name());
                    triggered.setDistanceValue(distance);
                    triggered.setTimeValue(thresholdResult.getTimeValue());
                    triggered.setTriggeredBy(thresholdResult.getTriggeredBy());
                    triggered.setTriggeredAt(Instant.now());

                    triggeredAlerts.add(triggered);
                    response.setAlertTriggered(true);

                    LOG.info(
                        "✅ Alerte déclenchée: {} pour le bus {} à l'arrêt {} (distance={}m)",
                        alerte.getNom(),
                        bus.getNumeroVehicule(),
                        arretCible.getNom(),
                        distance
                    );
                }
            }
        }

        response.setTriggeredAlerts(triggeredAlerts);
        return response;
    }

    /**
     * Trouve les alertes actives pour un bus
     */
    private List<AlerteApproche> findActiveAlertsForBus(Bus bus) {
        // Récupérer les alertes actives des utilisateurs liés à cette ligne
        // Ici, on suppose que les alertes sont associées à des utilisateurs
        // et que ces utilisateurs sont intéressés par cette ligne

        // Version simplifiée : toutes les alertes actives
        return alerteApprocheRepository.findByStatut(AlertStatus.ACTIVE);
    }

    /**
     * Vérifie si l'alerte est active à l'heure actuelle
     */
    private boolean isAlertActiveInCurrentTime(AlerteApproche alerte) {
        if (alerte.getJoursActivation() == null || alerte.getJoursActivation().isEmpty()) {
            return true;
        }

        // Vérifier le jour de la semaine
        String today = LocalDate.now().getDayOfWeek().toString().substring(0, 3);
        if (!alerte.getJoursActivation().contains(today)) {
            return false;
        }

        // Vérifier la plage horaire
        if (alerte.getHeureDebut() != null && alerte.getHeureFin() != null) {
            LocalTime now = LocalTime.now();
            LocalTime debut = LocalTime.parse(alerte.getHeureDebut());
            LocalTime fin = LocalTime.parse(alerte.getHeureFin());

            return !now.isBefore(debut) && !now.isAfter(fin);
        }

        return true;
    }

    /**
     * Calcule la distance entre deux points GPS (formule de Haversine)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a =
            Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Vérifie les seuils de distance et de temps
     */
    private ThresholdCheckResult checkThresholds(AlerteApproche alerte, double distance, AlertCheckRequest request, Arret arret) {
        ThresholdCheckResult result = new ThresholdCheckResult();
        result.setTriggered(false);

        boolean distanceTriggered = false;
        boolean timeTriggered = false;

        // Vérifier le seuil de distance
        if (alerte.getSeuilDistance() != null) {
            distanceTriggered = distance <= alerte.getSeuilDistance();
        }

        // Vérifier le seuil de temps (calculé à partir de la vitesse)
        if (alerte.getSeuilTemps() != null && request.getVitesse() != null && request.getVitesse() > 0) {
            // Convertir la vitesse de km/h en m/s
            double vitesseMs = (request.getVitesse() * 1000) / 3600;
            if (vitesseMs > 0) {
                int timeToArrival = (int) (distance / vitesseMs);
                timeTriggered = timeToArrival <= alerte.getSeuilTemps();
                result.setTimeValue(timeToArrival);
            }
        }

        // Vérifier selon le type de seuil
        switch (alerte.getTypeSeuil()) {
            case DISTANCE:
                if (distanceTriggered) {
                    result.setTriggered(true);
                    result.setTriggeredBy("DISTANCE");
                }
                break;
            case TIME:
                if (timeTriggered) {
                    result.setTriggered(true);
                    result.setTriggeredBy("TIME");
                }
                break;
            case OR_BOTH:
                if (distanceTriggered || timeTriggered) {
                    result.setTriggered(true);
                    result.setTriggeredBy(distanceTriggered ? "DISTANCE" : "TIME");
                }
                break;
            case AND_BOTH:
                if (distanceTriggered && timeTriggered) {
                    result.setTriggered(true);
                    result.setTriggeredBy("BOTH");
                }
                break;
        }

        result.setDistanceValue(distance);
        return result;
    }

    /**
     * Crée une entrée dans l'historique des alertes
     */
    private HistoriqueAlerte createHistoriqueAlerte(AlerteApproche alerte, Bus bus, double distance, Integer temps, String type) {
        HistoriqueAlerte historique = new HistoriqueAlerte();
        historique.setDateDeclenchement(Instant.now());
        historique.setAlerteApproche(alerte);
        historique.setBus(bus);
        historique.setBusNumero(bus.getNumeroVehicule());
        historique.setDistanceReelle((int) Math.round(distance));
        historique.setTempsReel(temps);

        if (type != null) {
            switch (type) {
                case "DISTANCE":
                    historique.setTypeDeclenchement(ThresholdType.DISTANCE);
                    break;
                case "TIME":
                    historique.setTypeDeclenchement(ThresholdType.TIME);
                    break;
                case "BOTH":
                    historique.setTypeDeclenchement(ThresholdType.AND_BOTH);
                    break;
            }
        }

        historique.setNotificationEnvoyee(false);

        return historique;
    }

    /**
     * Trouve un arrêt par son ID dans une liste
     */
    private Arret findArretById(List<Arret> arrets, Long id) {
        if (id == null) return null;
        return arrets.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Classe interne pour les résultats de vérification des seuils
     */
    public static class ThresholdCheckResult {

        private boolean triggered;
        private Double distanceValue;
        private Integer timeValue;
        private String triggeredBy;

        public boolean isTriggered() {
            return triggered;
        }

        public void setTriggered(boolean triggered) {
            this.triggered = triggered;
        }

        public Double getDistanceValue() {
            return distanceValue;
        }

        public void setDistanceValue(Double distanceValue) {
            this.distanceValue = distanceValue;
        }

        public Integer getTimeValue() {
            return timeValue;
        }

        public void setTimeValue(Integer timeValue) {
            this.timeValue = timeValue;
        }

        public String getTriggeredBy() {
            return triggeredBy;
        }

        public void setTriggeredBy(String triggeredBy) {
            this.triggeredBy = triggeredBy;
        }
    }
}
