package sn.yegg.app.web.rest;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.service.dto.BusPositionDTO;
import sn.yegg.app.service.impl.WebSocketService;

@RestController
@RequestMapping("/api/websocket")
public class WebSocketTestResource {

    private final Logger log = LoggerFactory.getLogger(WebSocketTestResource.class);
    private final WebSocketService webSocketService;
    private final BusRepository busRepository;

    public WebSocketTestResource(WebSocketService webSocketService, BusRepository busRepository) {
        this.webSocketService = webSocketService;
        this.busRepository = busRepository;
    }

    /**
     * Envoie manuellement la position d'un bus via WebSocket
     */
    @PostMapping("/send-position/{busId}")
    public ResponseEntity<String> sendBusPosition(@PathVariable Long busId) {
        return busRepository
            .findById(busId)
            .map(bus -> {
                BusPositionDTO dto = new BusPositionDTO(
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

                webSocketService.sendBusPosition(dto);
                return ResponseEntity.ok("Position envoyée pour le bus " + busId);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Envoie les positions de tous les bus
     */
    @PostMapping("/send-all-positions")
    public ResponseEntity<String> sendAllBusPositions() {
        List<Bus> buses = busRepository.findAll();

        List<BusPositionDTO> positions = buses
            .stream()
            .filter(bus -> bus.getCurrentLatitude() != null)
            .map(bus ->
                new BusPositionDTO(
                    bus.getId(),
                    bus.getNumeroVehicule(),
                    bus.getPlaque(),
                    bus.getCurrentLatitude(),
                    bus.getCurrentLongitude(),
                    bus.getCurrentVitesse(),
                    bus.getCurrentCap(),
                    bus.getPositionUpdatedAt(),
                    bus.getStatut()
                )
            )
            .collect(Collectors.toList());

        positions.forEach(webSocketService::sendBusPosition);

        return ResponseEntity.ok(positions.size() + " positions envoyées");
    }

    /**
     * Envoie une alerte de test
     */
    @PostMapping("/send-alert")
    public ResponseEntity<String> sendAlert(
        @RequestParam String type,
        @RequestParam String message,
        @RequestParam(required = false) Long busId
    ) {
        webSocketService.sendBusAlert(type, message, busId);
        return ResponseEntity.ok("Alerte envoyée: " + type);
    }
}
