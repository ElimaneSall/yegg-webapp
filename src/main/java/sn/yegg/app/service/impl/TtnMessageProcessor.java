package sn.yegg.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.repository.TrackingRepository;

@Service
public class TtnMessageProcessor {

    private final BusRepository busRepository;
    private final TrackingRepository trackingRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(TtnMessageProcessor.class);

    public TtnMessageProcessor(BusRepository busRepository, TrackingRepository trackingRepository) {
        this.busRepository = busRepository;
        this.trackingRepository = trackingRepository;
    }

    @Transactional
    public void processMessage(String deviceId, String payload) {
        try {
            log.info("Traitement message pour device: {}", deviceId);

            JsonNode root = objectMapper.readTree(payload);

            // Récupérer le bus correspondant
            Optional<Bus> busOpt = busRepository.findByGpsDeviceId(deviceId);
            if (busOpt.isEmpty()) {
                log.warn("Aucun bus trouvé avec device ID: {}", deviceId);
                return;
            }

            Bus bus = busOpt.get();
            log.info("Bus trouvé: {} ({})", bus.getNumeroVehicule(), bus.getPlaque());

            // Extraire les données de localisation
            boolean positionUpdated = extractAndUpdatePosition(bus, root);

            if (positionUpdated) {
                // Sauvegarder le bus avec sa nouvelle position
                busRepository.save(bus);

                // Créer une entrée de tracking
                saveTracking(bus, root);

                log.info(
                    "✅ Position mise à jour pour bus {}: lat={}, lng={}",
                    bus.getNumeroVehicule(),
                    bus.getCurrentLatitude(),
                    bus.getCurrentLongitude()
                );
            } else {
                log.warn("⚠️ Aucune position valide trouvée dans le message");
            }

            // Nettoyer les anciennes données (une fois par jour environ)
            if (Math.random() < 0.01) { // 1% de chance, pour ne pas le faire à chaque message
                cleanupOldTrackingData();
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors du traitement du message TTN: {}", e.getMessage(), e);
        }
    }

    private boolean extractAndUpdatePosition(Bus bus, JsonNode root) {
        try {
            JsonNode uplinkMessage = root.path("uplink_message");
            if (uplinkMessage.isMissingNode()) {
                log.warn("Pas de uplink_message dans le payload");
                return false;
            }

            // Extraire le timestamp
            Instant timestamp = extractTimestamp(uplinkMessage);
            if (timestamp != null) {
                bus.setPositionUpdatedAt(timestamp);
                bus.setGpsLastPing(timestamp);
            }

            // Méthode 1: Chercher dans decoded_payload (format Cayenne LPP)
            JsonNode decodedPayload = uplinkMessage.path("decoded_payload");
            if (!decodedPayload.isMissingNode()) {
                log.debug("decoded_payload: {}", decodedPayload);

                // Chercher le GPS dans le format Cayenne
                JsonNode gpsNode = decodedPayload.path("gps_1");
                if (!gpsNode.isMissingNode()) {
                    BigDecimal latitude = getBigDecimal(gpsNode, "latitude");
                    BigDecimal longitude = getBigDecimal(gpsNode, "longitude");

                    if (latitude != null && longitude != null) {
                        bus.setCurrentLatitude(latitude);
                        bus.setCurrentLongitude(longitude);

                        // Vitesse et cap optionnels
                        BigDecimal vitesse = getBigDecimal(decodedPayload, "analog_in_2");
                        if (vitesse != null) {
                            bus.setCurrentVitesse(vitesse);
                        }

                        log.info("Position extraite de gps_1: {}, {}", latitude, longitude);
                        return true;
                    }
                }

                // Chercher des champs individuels
                BigDecimal latitude = getBigDecimal(decodedPayload, "latitude");
                BigDecimal longitude = getBigDecimal(decodedPayload, "longitude");

                if (latitude != null && longitude != null) {
                    bus.setCurrentLatitude(latitude);
                    bus.setCurrentLongitude(longitude);
                    log.info("Position extraite des champs individuels: {}, {}", latitude, longitude);
                    return true;
                }
            }

            // Méthode 2: Chercher dans locations.frm-payload
            JsonNode locations = uplinkMessage.path("locations");
            if (!locations.isMissingNode()) {
                JsonNode frmPayload = locations.path("frm-payload");
                if (!frmPayload.isMissingNode()) {
                    BigDecimal latitude = getBigDecimal(frmPayload, "latitude");
                    BigDecimal longitude = getBigDecimal(frmPayload, "longitude");

                    if (latitude != null && longitude != null) {
                        bus.setCurrentLatitude(latitude);
                        bus.setCurrentLongitude(longitude);
                        log.info("Position extraite de locations.frm-payload: {}, {}", latitude, longitude);
                        return true;
                    }
                }
            }

            // Méthode 3: Parser le frm_payload manuellement (si nécessaire)
            String frmPayload = uplinkMessage.path("frm_payload").asText();
            if (!frmPayload.isEmpty()) {
                // Décoder le base64 et parser selon votre format
                // À implémenter selon votre format spécifique
                log.debug("frm_payload brut: {}", frmPayload);
            }

            return false;
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction de la position: {}", e.getMessage());
            return false;
        }
    }

    private void saveTracking(Bus bus, JsonNode root) {
        try {
            Tracking tracking = new Tracking();
            tracking.setBus(bus);
            tracking.setLatitude(bus.getCurrentLatitude());
            tracking.setLongitude(bus.getCurrentLongitude());
            tracking.setVitesse(bus.getCurrentVitesse());
            tracking.setCap(bus.getCurrentCap());
            tracking.setTimestamp(bus.getPositionUpdatedAt() != null ? bus.getPositionUpdatedAt() : Instant.now());
            tracking.setSource("TTN");

            trackingRepository.save(tracking);
            log.debug("Tracking sauvegardé pour le bus {}", bus.getNumeroVehicule());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde du tracking: {}", e.getMessage());
        }
    }

    private Instant extractTimestamp(JsonNode uplinkMessage) {
        try {
            // Essayer received_at
            String receivedAt = uplinkMessage.path("received_at").asText();
            if (receivedAt != null && !receivedAt.isEmpty()) {
                return Instant.parse(receivedAt);
            }
        } catch (DateTimeParseException e) {
            log.warn("Format de timestamp invalide");
        }
        return Instant.now();
    }

    private void cleanupOldTrackingData() {
        try {
            Instant cutoffDate = Instant.now().minusSeconds(7 * 24 * 3600); // 7 jours
            int deletedCount = trackingRepository.deleteByTimestampBefore(cutoffDate);
            if (deletedCount > 0) {
                log.info("Nettoyage: {} anciennes données supprimées", deletedCount);
            }
        } catch (Exception e) {
            log.error("Erreur lors du nettoyage: {}", e.getMessage());
        }
    }

    private BigDecimal getBigDecimal(JsonNode node, String field) {
        if (node.has(field) && !node.path(field).isNull()) {
            try {
                double value = node.path(field).asDouble();
                return BigDecimal.valueOf(value);
            } catch (Exception e) {
                log.warn("Impossible de convertir {} en BigDecimal: {}", field, e.getMessage());
            }
        }
        return null;
    }

    private Integer getIntegerFromJson(JsonNode node, String field) {
        if (node.has(field) && !node.path(field).isNull()) {
            try {
                return node.path(field).asInt();
            } catch (Exception e) {
                log.warn("Impossible de convertir {} en Integer: {}", field, e.getMessage());
            }
        }
        return null;
    }
}
