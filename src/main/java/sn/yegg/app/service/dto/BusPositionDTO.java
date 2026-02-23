package sn.yegg.app.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import sn.yegg.app.domain.enumeration.BusStatus;

public class BusPositionDTO {

    private Long busId;
    private String numeroVehicule;
    private String plaque;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal vitesse;
    private Integer cap;
    private Instant timestamp;
    private BusStatus statut;

    // Constructeurs
    public BusPositionDTO() {}

    public BusPositionDTO(
        Long busId,
        String numeroVehicule,
        String plaque,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal vitesse,
        Integer cap,
        Instant timestamp,
        BusStatus statut
    ) {
        this.busId = busId;
        this.numeroVehicule = numeroVehicule;
        this.plaque = plaque;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vitesse = vitesse;
        this.cap = cap;
        this.timestamp = timestamp;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getNumeroVehicule() {
        return numeroVehicule;
    }

    public void setNumeroVehicule(String numeroVehicule) {
        this.numeroVehicule = numeroVehicule;
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getVitesse() {
        return vitesse;
    }

    public void setVitesse(BigDecimal vitesse) {
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

    public BusStatus getStatut() {
        return statut;
    }

    public void setStatut(BusStatus statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return String.format("BusPositionDTO{id=%d, bus='%s', lat=%.6f, lng=%.6f}", busId, numeroVehicule, latitude, longitude);
    }
}
