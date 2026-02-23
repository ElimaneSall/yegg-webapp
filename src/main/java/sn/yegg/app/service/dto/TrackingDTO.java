package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.TrackingSource;

/**
 * A DTO for the {@link sn.yegg.app.domain.Tracking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackingDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    private BigDecimal vitesse;

    @Min(value = 0)
    @Max(value = 359)
    private Integer cap;

    private Integer precision;

    @NotNull
    private Instant timestamp;

    @NotNull
    private TrackingSource source;

    private String evenement;

    @Lob
    private String commentaire;

    private BusDTO bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TrackingSource getSource() {
        return source;
    }

    public void setSource(TrackingSource source) {
        this.source = source;
    }

    public String getEvenement() {
        return evenement;
    }

    public void setEvenement(String evenement) {
        this.evenement = evenement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public BusDTO getBus() {
        return bus;
    }

    public void setBus(BusDTO bus) {
        this.bus = bus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackingDTO)) {
            return false;
        }

        TrackingDTO trackingDTO = (TrackingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trackingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackingDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", vitesse=" + getVitesse() +
            ", cap=" + getCap() +
            ", precision=" + getPrecision() +
            ", timestamp='" + getTimestamp() + "'" +
            ", source='" + getSource() + "'" +
            ", evenement='" + getEvenement() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", bus=" + getBus() +
            "}";
    }
}
