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
import sn.yegg.app.service.dto.BusPositionDTO;

@Service
public class TtnMessageProcessor {

    private final BusRepository busRepository;
    private final TrackingRepository trackingRepository;
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(TtnMessageProcessor.class);

    public TtnMessageProcessor(BusRepository busRepository, TrackingRepository trackingRepository, WebSocketService webSocketService) {
        this.busRepository = busRepository;
        this.trackingRepository = trackingRepository;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public void processMessage(String deviceId, String payload) {
        try {
            log.info("Traitement message pour device: {}", deviceId);

            JsonNode root = objectMapper.readTree(payload);

            Optional<Bus> busOpt = busRepository.findByGpsDeviceId(deviceId);
            if (busOpt.isEmpty()) {
                log.warn("Aucun bus trouvé avec device ID: {}", deviceId);
                return;
            }

            Bus bus = busOpt.get();
            log.info("Bus trouvé: {} ({})", bus.getNumeroVehicule(), bus.getPlaque());

            boolean positionUpdated = extractAndUpdatePosition(bus, root);

            if (positionUpdated) {
                busRepository.save(bus);
                saveTracking(bus, root);

                log.info(
                    "✅ Position mise à jour pour bus {}: lat={}, lng={}, vitesse={}",
                    bus.getNumeroVehicule(),
                    bus.getCurrentLatitude(),
                    bus.getCurrentLongitude(),
                    bus.getCurrentVitesse()
                );

                // 🚀 Envoyer la position via WebSocket
                sendBusPositionViaWebSocket(bus);
            } else {
                log.warn("⚠️ Aucune position valide trouvée dans le message");
            }

            // Nettoyer les anciennes données (1% de chance)
            if (Math.random() < 0.01) {
                cleanupOldTrackingData();
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors du traitement du message TTN: {}", e.getMessage(), e);
        }
    }

    /**
     * Envoie la position du bus via WebSocket
     */
    private void sendBusPositionViaWebSocket(Bus bus) {
        try {
            BusPositionDTO positionDTO = new BusPositionDTO(
                bus.getId(),
                bus.getNumeroVehicule(),
                bus.getPlaque(),
                bus.getCurrentLatitude(),
                bus.getCurrentLongitude(),
                bus.getCurrentVitesse(),
                bus.getCurrentCap(),
                bus.getPositionUpdatedAt(),
                bus.getStatut()
            );

            webSocketService.sendBusPosition(positionDTO);
            log.debug("Position envoyée via WebSocket pour bus {}", bus.getNumeroVehicule());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi WebSocket: {}", e.getMessage());
        }
    }

    /**
     * Extrait et met à jour la position du bus à partir du payload TTN
     */
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

            // Récupérer le decoded_payload
            JsonNode decodedPayload = uplinkMessage.path("decoded_payload");

            // Variables pour stocker les données extraites
            BigDecimal latitude = null;
            BigDecimal longitude = null;
            BigDecimal vitesse = null;
            Integer cap = null;

            // STRUCTURE 1: Format Cayenne LPP avec gps_1 (votre format actuel)
            if (!decodedPayload.isMissingNode()) {
                log.debug("decoded_payload: {}", decodedPayload);

                // Chercher gps_1
                JsonNode gpsNode = decodedPayload.path("gps_1");
                if (!gpsNode.isMissingNode()) {
                    latitude = getBigDecimal(gpsNode, "latitude");
                    longitude = getBigDecimal(gpsNode, "longitude");
                    log.info("Structure gps_1 trouvée dans decoded_payload");
                }

                // Chercher la vitesse dans analog_in_2
                if (decodedPayload.has("analog_in_2")) {
                    vitesse = getBigDecimal(decodedPayload, "analog_in_2");
                }

                // Chercher le cap si disponible
                if (decodedPayload.has("cap")) {
                    cap = getIntegerFromJson(decodedPayload, "cap");
                }
            }

            // STRUCTURE 2: Champs directs dans decoded_payload
            if (latitude == null && decodedPayload.has("latitude")) {
                latitude = getBigDecimal(decodedPayload, "latitude");
                longitude = getBigDecimal(decodedPayload, "longitude");
                log.info("Structure champs directs trouvée dans decoded_payload");
            }

            // STRUCTURE 3: Locations frm-payload
            if (latitude == null) {
                JsonNode locations = uplinkMessage.path("locations");
                if (locations.has("frm-payload")) {
                    JsonNode gps = locations.get("frm-payload");
                    latitude = getBigDecimal(gps, "latitude");
                    longitude = getBigDecimal(gps, "longitude");
                    log.info("Structure locations.frm-payload trouvée");
                }
            }

            // STRUCTURE 4: Parsing manuel du frm_payload (si nécessaire)
            if (latitude == null) {
                String frmPayload = uplinkMessage.path("frm_payload").asText();
                if (!frmPayload.isEmpty()) {
                    log.debug("frm_payload brut: {}", frmPayload);
                    // Ajouter ici le parsing spécifique si besoin
                }
            }

            // Si on a trouvé une position valide
            if (latitude != null && longitude != null) {
                bus.setCurrentLatitude(latitude);
                bus.setCurrentLongitude(longitude);

                if (vitesse != null) {
                    bus.setCurrentVitesse(vitesse);
                }

                if (cap != null) {
                    bus.setCurrentCap(cap);
                }

                log.info("✅ Position extraite: lat={}, lng={}, vitesse={}", latitude, longitude, vitesse);
                return true;
            }

            log.warn("⚠️ Aucune position trouvée dans le payload");
            log.debug("Payload complet: {}", root);
            return false;
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'extraction de la position: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sauvegarde une entrée de tracking
     */
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

    /**
     * Extrait le timestamp du message
     */
    private Instant extractTimestamp(JsonNode uplinkMessage) {
        try {
            String receivedAt = uplinkMessage.path("received_at").asText();
            if (receivedAt != null && !receivedAt.isEmpty()) {
                return Instant.parse(receivedAt);
            }
        } catch (DateTimeParseException e) {
            log.warn("Format de timestamp invalide: {}", e.getMessage());
        }
        return Instant.now();
    }

    /**
     * Nettoie les anciennes données de tracking (plus de 7 jours)
     */
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

    /**
     * Récupère un BigDecimal d'un nœud JSON
     */
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

    /**
     * Récupère un Integer d'un nœud JSON
     */
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
