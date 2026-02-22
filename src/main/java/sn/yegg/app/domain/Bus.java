package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bus.
 */
@Entity
@Table(name = "bus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_vehicule", nullable = false, unique = true)
    private String numeroVehicule;

    @NotNull
    @Column(name = "plaque", nullable = false, unique = true)
    private String plaque;

    @Column(name = "modele")
    private String modele;

    @NotNull
    @Min(value = 1)
    @Max(value = 200)
    @Column(name = "capacite", nullable = false)
    private Integer capacite;

    @Column(name = "annee_fabrication")
    private Integer anneeFabrication;

    @Column(name = "gps_device_id", unique = true)
    private String gpsDeviceId;

    @Column(name = "gps_status")
    private String gpsStatus;

    @Column(name = "gps_last_ping")
    private Instant gpsLastPing;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "gps_battery_level")
    private Integer gpsBatteryLevel;

    @Column(name = "current_latitude", precision = 21, scale = 2)
    private BigDecimal currentLatitude;

    @Column(name = "current_longitude", precision = 21, scale = 2)
    private BigDecimal currentLongitude;

    @Column(name = "current_vitesse", precision = 21, scale = 2)
    private BigDecimal currentVitesse;

    @Min(value = 0)
    @Max(value = 359)
    @Column(name = "current_cap")
    private Integer currentCap;

    @Column(name = "position_updated_at")
    private Instant positionUpdatedAt;

    @NotNull
    @Column(name = "statut", nullable = false)
    private String statut;

    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets", "operateur" }, allowSetters = true)
    private Ligne ligne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVehicule() {
        return this.numeroVehicule;
    }

    public Bus numeroVehicule(String numeroVehicule) {
        this.setNumeroVehicule(numeroVehicule);
        return this;
    }

    public void setNumeroVehicule(String numeroVehicule) {
        this.numeroVehicule = numeroVehicule;
    }

    public String getPlaque() {
        return this.plaque;
    }

    public Bus plaque(String plaque) {
        this.setPlaque(plaque);
        return this;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public String getModele() {
        return this.modele;
    }

    public Bus modele(String modele) {
        this.setModele(modele);
        return this;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Integer getCapacite() {
        return this.capacite;
    }

    public Bus capacite(Integer capacite) {
        this.setCapacite(capacite);
        return this;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getAnneeFabrication() {
        return this.anneeFabrication;
    }

    public Bus anneeFabrication(Integer anneeFabrication) {
        this.setAnneeFabrication(anneeFabrication);
        return this;
    }

    public void setAnneeFabrication(Integer anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    public String getGpsDeviceId() {
        return this.gpsDeviceId;
    }

    public Bus gpsDeviceId(String gpsDeviceId) {
        this.setGpsDeviceId(gpsDeviceId);
        return this;
    }

    public void setGpsDeviceId(String gpsDeviceId) {
        this.gpsDeviceId = gpsDeviceId;
    }

    public String getGpsStatus() {
        return this.gpsStatus;
    }

    public Bus gpsStatus(String gpsStatus) {
        this.setGpsStatus(gpsStatus);
        return this;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Instant getGpsLastPing() {
        return this.gpsLastPing;
    }

    public Bus gpsLastPing(Instant gpsLastPing) {
        this.setGpsLastPing(gpsLastPing);
        return this;
    }

    public void setGpsLastPing(Instant gpsLastPing) {
        this.gpsLastPing = gpsLastPing;
    }

    public Integer getGpsBatteryLevel() {
        return this.gpsBatteryLevel;
    }

    public Bus gpsBatteryLevel(Integer gpsBatteryLevel) {
        this.setGpsBatteryLevel(gpsBatteryLevel);
        return this;
    }

    public void setGpsBatteryLevel(Integer gpsBatteryLevel) {
        this.gpsBatteryLevel = gpsBatteryLevel;
    }

    public BigDecimal getCurrentLatitude() {
        return this.currentLatitude;
    }

    public Bus currentLatitude(BigDecimal currentLatitude) {
        this.setCurrentLatitude(currentLatitude);
        return this;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return this.currentLongitude;
    }

    public Bus currentLongitude(BigDecimal currentLongitude) {
        this.setCurrentLongitude(currentLongitude);
        return this;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public BigDecimal getCurrentVitesse() {
        return this.currentVitesse;
    }

    public Bus currentVitesse(BigDecimal currentVitesse) {
        this.setCurrentVitesse(currentVitesse);
        return this;
    }

    public void setCurrentVitesse(BigDecimal currentVitesse) {
        this.currentVitesse = currentVitesse;
    }

    public Integer getCurrentCap() {
        return this.currentCap;
    }

    public Bus currentCap(Integer currentCap) {
        this.setCurrentCap(currentCap);
        return this;
    }

    public void setCurrentCap(Integer currentCap) {
        this.currentCap = currentCap;
    }

    public Instant getPositionUpdatedAt() {
        return this.positionUpdatedAt;
    }

    public Bus positionUpdatedAt(Instant positionUpdatedAt) {
        this.setPositionUpdatedAt(positionUpdatedAt);
        return this;
    }

    public void setPositionUpdatedAt(Instant positionUpdatedAt) {
        this.positionUpdatedAt = positionUpdatedAt;
    }

    public String getStatut() {
        return this.statut;
    }

    public Bus statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Bus utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Ligne getLigne() {
        return this.ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public Bus ligne(Ligne ligne) {
        this.setLigne(ligne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bus)) {
            return false;
        }
        return getId() != null && getId().equals(((Bus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bus{" +
            "id=" + getId() +
            ", numeroVehicule='" + getNumeroVehicule() + "'" +
            ", plaque='" + getPlaque() + "'" +
            ", modele='" + getModele() + "'" +
            ", capacite=" + getCapacite() +
            ", anneeFabrication=" + getAnneeFabrication() +
            ", gpsDeviceId='" + getGpsDeviceId() + "'" +
            ", gpsStatus='" + getGpsStatus() + "'" +
            ", gpsLastPing='" + getGpsLastPing() + "'" +
            ", gpsBatteryLevel=" + getGpsBatteryLevel() +
            ", currentLatitude=" + getCurrentLatitude() +
            ", currentLongitude=" + getCurrentLongitude() +
            ", currentVitesse=" + getCurrentVitesse() +
            ", currentCap=" + getCurrentCap() +
            ", positionUpdatedAt='" + getPositionUpdatedAt() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
