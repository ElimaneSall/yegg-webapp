package sn.yegg.app.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class AlertCheckRequest {

    @NotNull
    private Long busId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Double vitesse;
    private Integer cap;
    private Instant timestamp;

    // Getters et Setters
    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

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

    public Double getVitesse() {
        return vitesse;
    }

    public void setVitesse(Double vitesse) {
        this.vitesse = vitesse;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
