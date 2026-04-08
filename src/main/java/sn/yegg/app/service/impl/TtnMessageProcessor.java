package sn.yegg.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.domain.enumeration.TrackingSource;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.repository.TrackingRepository;
import sn.yegg.app.service.AlertDetectionService;
import sn.yegg.app.service.dto.BusPositionDTO;

@Service
public class TtnMessageProcessor {

    private final BusRepository busRepository;
    private final TrackingRepository trackingRepository;
    private final WebSocketService webSocketService;

    private final AlertDetectionService alertDetectionService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(TtnMessageProcessor.class);

    public TtnMessageProcessor(
        BusRepository busRepository,
        TrackingRepository trackingRepository,
        WebSocketService webSocketService,
        AlertDetectionService alertDetectionService
    ) {
        this.busRepository = busRepository;
        this.trackingRepository = trackingRepository;
        this.webSocketService = webSocketService;
        this.alertDetectionService = alertDetectionService;
    }

    public void processMessage(String deviceId, String payload) {
        try {
            log.info("Traitement message pour device: {}", deviceId);
            JsonNode root = objectMapper.readTree(payload);

            Bus bus = busRepository.findByGpsDeviceIdWithLigne(deviceId).orElse(null);
            if (bus == null) {
                log.warn("Aucun bus trouvé avec device ID: {}", deviceId);
                return;
            }

            boolean positionUpdated = extractAndUpdatePosition(bus, root);

            if (positionUpdated) {
                busRepository.save(bus);
                saveTracking(bus, root);

                log.info(
                    "✅ Position mise à jour pour bus {}: lat={}, lng={}",
                    bus.getNumeroVehicule(),
                    bus.getCurrentLatitude(),
                    bus.getCurrentLongitude()
                );

                // 🚀 Envoyer la position via WebSocket
                sendBusPositionViaWebSocket(bus);

                // 🔔 VÉRIFIER LES ALERTES (NOUVEAU)
                checkAndTriggerAlerts(bus);
            }
        } catch (Exception e) {
            log.error("❌ Erreur traitement message TTN: {}", e.getMessage(), e);
        }
    }

    /**
     * Vérifie et déclenche les alertes pour un bus
     */
    private void checkAndTriggerAlerts(Bus bus) {
        try {
            if (bus.getLigne() != null && bus.getCurrentLatitude() != null && bus.getCurrentLongitude() != null) {
                alertDetectionService.checkApproachAlerts(bus);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la vérification des alertes pour bus {}: {}", bus.getNumeroVehicule(), e.getMessage());
            // Ne pas propager l'exception pour ne pas bloquer le traitement MQTT
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
                bus.getModele(),
                bus.getPlaque(),
                bus.getCurrentLatitude(),
                bus.getCurrentLongitude(),
                bus.getCurrentVitesse(),
                bus.getCurrentCap(),
                bus.getPositionUpdatedAt(),
                bus.getStatut(),
                bus.getLigne().getNumero(),
                bus.getLigne().getDirection()
            );

            webSocketService.sendBusPosition(positionDTO);
            log.debug("Position envoyée via WebSocket pour bus {}", bus.getNumeroVehicule());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi WebSocket: {}", e.getMessage());
        }
    }

    /**
     * Extrait position, H3 et cap depuis le payload TTN
     */
    private boolean extractAndUpdatePosition(Bus bus, JsonNode root) {
        try {
            JsonNode uplink = root.path("uplink_message");
            if (uplink.isMissingNode()) {
                log.warn("Pas de uplink_message");
                return false;
            }

            // Timestamp
            Instant ts = extractTimestamp(uplink);
            if (ts != null) {
                bus.setPositionUpdatedAt(ts);
                bus.setGpsLastPing(ts);
            }

            JsonNode payload = uplink.path("decoded_payload");
            BigDecimal lat = null, lng = null, alt = null, vitesse = null;
            Integer cap = null;
            Long h3Index = null;

            if (!payload.isMissingNode()) {
                // GPS (channel 1)
                JsonNode gps = payload.path("gps_1");
                if (!gps.isMissingNode()) {
                    lat = getBigDecimal(gps, "latitude");
                    lng = getBigDecimal(gps, "longitude");
                    alt = getBigDecimal(gps, "altitude");
                }

                // Cap (channel 12)
                // Cap (channel 12) - CORRECTION
                if (payload.has("analog_in_12")) {
                    // Cayenne LPP décode analogInput en FLOAT, pas en INTEGER
                    BigDecimal capVal = getBigDecimal(payload, "analog_in_12");

                    if (capVal != null) {
                        int capInt = capVal.intValue();

                        if (capInt >= 0 && capInt <= 360) {
                            cap = capInt;
                            log.debug("Cap extrait: {}°", capInt);
                        } else if (capInt == 65535 || capInt == -1) {
                            log.debug("Cap indisponible (valeur: {})", capInt);
                        } else {
                            log.warn("Valeur cap invalide: {}", capInt);
                        }
                    } else {
                        log.debug("analog_in_12 est null ou non numérique");
                    }
                }

                // H3 Index (channels 8-11 → 64 bits)
                if (
                    payload.has("analog_in_8") && payload.has("analog_in_9") && payload.has("analog_in_10") && payload.has("analog_in_11")
                ) {
                    Long p1 = getLong(payload, "analog_in_8");
                    Long p2 = getLong(payload, "analog_in_9");
                    Long p3 = getLong(payload, "analog_in_10");
                    Long p4 = getLong(payload, "analog_in_11");

                    if (p1 != null && p2 != null && p3 != null && p4 != null) {
                        h3Index = (p4 << 48) | (p3 << 32) | (p2 << 16) | p1;
                        log.debug("H3 reconstruit: 0x{}", Long.toHexString(h3Index));
                    }
                }

                // Vitesse optionnelle
                if (payload.has("analog_in_2")) {
                    vitesse = getBigDecimal(payload, "analog_in_2");
                }
            }

            // Fallback: locations.frm-payload
            if (lat == null) {
                JsonNode loc = uplink.path("locations");
                if (loc.has("frm-payload")) {
                    JsonNode g = loc.get("frm-payload");
                    lat = getBigDecimal(g, "latitude");
                    lng = getBigDecimal(g, "longitude");
                }
            }

            // Mise à jour Bus
            if (lat != null && lng != null) {
                bus.setCurrentLatitude(lat);
                bus.setCurrentLongitude(lng);
                //  if (alt != null) bus.setCurrentLatitude(alt);
                if (vitesse != null) bus.setCurrentVitesse(vitesse);
                if (cap != null) bus.setCurrentCap(cap);
                if (h3Index != null) {
                    //                    bus.setH3Index(h3Index);
                    //                    bus.setH3Resolution(extractH3Resolution(h3Index));
                }

                log.info(
                    "Position: {} {} | Cap: {}° | H3: 0x{}",
                    lat,
                    lng,
                    cap != null ? cap : "N/A",
                    h3Index != null ? Long.toHexString(h3Index) : "N/A"
                );
                return true;
            }

            log.warn("Position introuvable");
            return false;
        } catch (Exception e) {
            log.error("Erreur extraction: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Extrait la résolution H3 depuis l'index encodé
     */
    private int extractH3Resolution(long h3Index) {
        return (int) ((h3Index >> 52) & 0x0F);
    }

    /**
     * Helper pour Long
     */
    private Long getLong(JsonNode node, String field) {
        JsonNode v = node.path(field);
        return v.isNumber() ? v.longValue() : null;
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
            tracking.setSource(TrackingSource.ONBOARD_GPS);

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
