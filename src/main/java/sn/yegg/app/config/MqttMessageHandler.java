package sn.yegg.app.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import sn.yegg.app.service.impl.TtnMessageProcessor;

@Configuration
public class MqttMessageHandler {

    private final TtnMessageProcessor ttnMessageProcessor;
    private final MqttConfig mqttConfig;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(MqttMessageHandler.class);

    public MqttMessageHandler(TtnMessageProcessor ttnMessageProcessor, MqttConfig mqttConfig, ObjectMapper objectMapper) {
        this.ttnMessageProcessor = ttnMessageProcessor;
        this.mqttConfig = mqttConfig;
        this.objectMapper = objectMapper;

        log.info("=== CONFIGURATION MQTT HANDLER ===");
        log.info("Broker: {}", mqttConfig.getBroker());
        log.info("Port: {}", mqttConfig.getPort());
        log.info("Username: {}", mqttConfig.getUsername());
        log.info("Topic: {}", mqttConfig.getTopic());
        log.info("===================================");
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel") // ⚠️ Écoute sur le même channel que MqttConfig
    public MessageHandler ttnMessageHandler() {
        return message -> {
            try {
                String payload = message.getPayload().toString();
                String topic = (String) message.getHeaders().get("mqtt_receivedTopic");

                log.info("📡 Message reçu - Topic: {}", topic);

                String deviceId = extractDeviceIdFromTopic(topic);

                if (deviceId != null && !deviceId.isEmpty()) {
                    log.info("✅ Device ID extrait: {}", deviceId);
                    ttnMessageProcessor.processMessage(deviceId, payload);
                } else {
                    log.warn("❌ Impossible d'extraire deviceId du topic: {}", topic);
                }
            } catch (Exception e) {
                log.error("❌ Erreur traitement message MQTT: {}", e.getMessage(), e);
            }
        };
    }

    private String extractDeviceIdFromTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            return null;
        }

        // Format TTN: v3/{appId}/devices/{deviceId}/up
        String[] parts = topic.split("/");

        if (parts.length >= 5 && "devices".equals(parts[2])) {
            return parts[3]; // Index 3 = deviceId
        }

        return null;
    }
}
