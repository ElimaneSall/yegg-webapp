package sn.yegg.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TtnPayloadDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(TtnPayloadDecoder.class);

    /**
     * Décode un payload TTN au format Cayenne LPP
     */
    public Map<String, Object> decodeCayenneLpp(byte[] payload) {
        Map<String, Object> result = new HashMap<>();

        // Implementation pour Cayenne LPP (The Things Network default format)
        // Format: channel, type, data
        int index = 0;
        while (index < payload.length) {
            int channel = payload[index++] & 0xFF;
            int type = payload[index++] & 0xFF;

            switch (type) {
                case 0x01: // Digital Input
                    result.put("digital_in_" + channel, payload[index++] & 0xFF);
                    break;
                case 0x02: // Digital Output
                    result.put("digital_out_" + channel, payload[index++] & 0xFF);
                    break;
                case 0x65: // Analog Input (2 bytes)
                    int analogValue = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    result.put("analog_" + channel, analogValue / 100.0);
                    index += 2;
                    break;
                case 0x66: // Analog Output
                    int analogOut = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    result.put("analog_out_" + channel, analogOut / 100.0);
                    index += 2;
                    break;
                case 0x67: // Illuminance (2 bytes)
                    int illuminance = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    result.put("illuminance_" + channel, illuminance);
                    index += 2;
                    break;
                case 0x68: // Presence (1 byte)
                    result.put("presence_" + channel, payload[index++] & 0xFF);
                    break;
                case 0x71: // Temperature (2 bytes)
                    int temp = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    result.put("temperature_" + channel, temp / 10.0);
                    index += 2;
                    break;
                case 0x73: // Accelerometer (3 axes x 2 bytes)
                    int accelX = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    int accelY = ((payload[index + 2] & 0xFF) << 8) | (payload[index + 3] & 0xFF);
                    int accelZ = ((payload[index + 4] & 0xFF) << 8) | (payload[index + 5] & 0xFF);
                    result.put("accel_x_" + channel, accelX / 1000.0);
                    result.put("accel_y_" + channel, accelY / 1000.0);
                    result.put("accel_z_" + channel, accelZ / 1000.0);
                    index += 6;
                    break;
                case 0x86: // Gyrometer (3 axes x 2 bytes)
                    int gyroX = ((payload[index] & 0xFF) << 8) | (payload[index + 1] & 0xFF);
                    int gyroY = ((payload[index + 2] & 0xFF) << 8) | (payload[index + 3] & 0xFF);
                    int gyroZ = ((payload[index + 4] & 0xFF) << 8) | (payload[index + 5] & 0xFF);
                    result.put("gyro_x_" + channel, gyroX / 100.0);
                    result.put("gyro_y_" + channel, gyroY / 100.0);
                    result.put("gyro_z_" + channel, gyroZ / 100.0);
                    index += 6;
                    break;
                case 0x88: // GPS (latitude, longitude, altitude)
                    int lat = ((payload[index] & 0xFF) << 16) | ((payload[index + 1] & 0xFF) << 8) | (payload[index + 2] & 0xFF);
                    int lng = ((payload[index + 3] & 0xFF) << 16) | ((payload[index + 4] & 0xFF) << 8) | (payload[index + 5] & 0xFF);
                    int alt = ((payload[index + 6] & 0xFF) << 8) | (payload[index + 7] & 0xFF);

                    // Conversion spéciale pour GPS (cayenne format)
                    result.put("latitude_" + channel, lat / 10000.0);
                    result.put("longitude_" + channel, lng / 10000.0);
                    result.put("altitude_" + channel, alt);
                    index += 8;
                    break;
                default:
                    log.warn("Type de données inconnu: {}", type);
                    index = payload.length; // Skip remaining
                    break;
            }
        }

        return result;
    }

    /**
     * Traite un message JSON de TTN
     */
    public TtnLocationData extractLocationFromJson(String jsonPayload) {
        try {
            JsonNode root = objectMapper.readTree(jsonPayload);
            JsonNode uplinkMessage = root.path("uplink_message");

            TtnLocationData data = new TtnLocationData();

            // Timestamp
            String receivedAt = uplinkMessage.path("received_at").asText();
            if (receivedAt != null && !receivedAt.isEmpty()) {
                data.setTimestamp(Instant.parse(receivedAt));
            }

            // Décoder le payload
            JsonNode decodedPayload = uplinkMessage.path("decoded_payload");
            if (!decodedPayload.isMissingNode()) {
                data.setLatitude(getDouble(decodedPayload, "latitude"));
                data.setLongitude(getDouble(decodedPayload, "longitude"));
                data.setSpeed(getDouble(decodedPayload, "speed"));
                data.setHeading(getInteger(decodedPayload, "heading"));
                data.setBattery(getInteger(decodedPayload, "battery"));
            }

            // Si pas dans decoded_payload, chercher dans frm_payload
            if (data.getLatitude() == null && uplinkMessage.has("frm_payload")) {
                String frmPayload = uplinkMessage.path("frm_payload").asText();
                // Décoder le payload base64 si nécessaire
                // (implémentation selon votre format spécifique)
            }

            // Utiliser les locations si disponibles
            JsonNode locations = uplinkMessage.path("locations");
            if (!locations.isMissingNode()) {
                JsonNode gps = locations.path("frm-payload");
                if (!gps.isMissingNode()) {
                    data.setLatitude(gps.path("latitude").asDouble());
                    data.setLongitude(gps.path("longitude").asDouble());
                }
            }

            return data;
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction des données de localisation", e);
            return null;
        }
    }

    private Double getDouble(JsonNode node, String field) {
        if (node.has(field) && !node.path(field).isNull()) {
            return node.path(field).asDouble();
        }
        return null;
    }

    private Integer getInteger(JsonNode node, String field) {
        if (node.has(field) && !node.path(field).isNull()) {
            return node.path(field).asInt();
        }
        return null;
    }

    /**
     * Classe interne pour les données de localisation
     */
    public static class TtnLocationData {

        private Double latitude;
        private Double longitude;
        private Double speed;
        private Integer heading;
        private Integer battery;
        private Instant timestamp;

        // Getters et setters
        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Integer getHeading() {
            return heading;
        }

        public void setHeading(Integer heading) {
            this.heading = heading;
        }

        public Integer getBattery() {
            return battery;
        }

        public void setBattery(Integer battery) {
            this.battery = battery;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }
    }
}
