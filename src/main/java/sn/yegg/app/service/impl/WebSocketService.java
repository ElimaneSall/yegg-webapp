package sn.yegg.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import sn.yegg.app.service.dto.BusPositionDTO;

@Service
public class WebSocketService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);
    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Envoie la position d'un bus à tous les clients connectés
     */
    public void sendBusPosition(BusPositionDTO busPosition) {
        try {
            log.debug("Envoi WebSocket - Bus {}: {}", busPosition.getBusId(), busPosition);
            messagingTemplate.convertAndSend("/topic/bus-positions", busPosition);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi WebSocket: {}", e.getMessage());
        }
    }

    /**
     * Envoie la position d'un bus à un utilisateur spécifique
     */
    public void sendBusPositionToUser(String userLogin, BusPositionDTO busPosition) {
        try {
            log.debug("Envoi WebSocket à {} - Bus: {}", userLogin, busPosition);
            messagingTemplate.convertAndSendToUser(userLogin, "/queue/bus-positions", busPosition);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi WebSocket à l'utilisateur: {}", e.getMessage());
        }
    }

    /**
     * Envoie une alerte de bus
     */
    public void sendBusAlert(String alertType, String message, Long busId) {
        try {
            var alert = new java.util.HashMap<String, Object>();
            alert.put("type", alertType);
            alert.put("message", message);
            alert.put("busId", busId);
            alert.put("timestamp", java.time.Instant.now().toString());

            messagingTemplate.convertAndSend("/topic/bus-alerts", alert);
            log.info("Alerte envoyée: {} - {}", alertType, message);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'alerte: {}", e.getMessage());
        }
    }
}
