package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.TrackingSource;

/**
 * A Tracking.
 */
@Entity
@Table(name = "tracking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "latitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal latitude;

    @NotNull
    @Column(name = "longitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal longitude;

    @Column(name = "vitesse", precision = 21, scale = 2)
    private BigDecimal vitesse;

    @Min(value = 0)
    @Max(value = 359)
    @Column(name = "cap")
    private Integer cap;

    @Column(name = "jhi_precision")
    private Integer precision;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private TrackingSource source;

    @Column(name = "evenement")
    private String evenement;

    @Lob
    @Column(name = "commentaire")
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligne", "chauffeur" }, allowSetters = true)
    private Bus bus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tracking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public Tracking latitude(BigDecimal latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Tracking longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getVitesse() {
        return this.vitesse;
    }

    public Tracking vitesse(BigDecimal vitesse) {
        this.setVitesse(vitesse);
        return this;
    }

    public void setVitesse(BigDecimal vitesse) {
        this.vitesse = vitesse;
    }

    public Integer getCap() {
        return this.cap;
    }

    public Tracking cap(Integer cap) {
        this.setCap(cap);
        return this;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Integer getPrecision() {
        return this.precision;
    }

    public Tracking precision(Integer precision) {
        this.setPrecision(precision);
        return this;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Tracking timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TrackingSource getSource() {
        return this.source;
    }

    public Tracking source(TrackingSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(TrackingSource source) {
        this.source = source;
    }

    public String getEvenement() {
        return this.evenement;
    }

    public Tracking evenement(String evenement) {
        this.setEvenement(evenement);
        return this;
    }

    public void setEvenement(String evenement) {
        this.evenement = evenement;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Tracking commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Tracking bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tracking)) {
            return false;
        }
        return getId() != null && getId().equals(((Tracking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tracking{" +
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
            "}";
    }
}
