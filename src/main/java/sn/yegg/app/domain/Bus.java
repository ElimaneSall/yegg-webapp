package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.BusStatus;
import sn.yegg.app.domain.enumeration.EnergyType;

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

    @Column(name = "constructeur")
    private String constructeur;

    @NotNull
    @Min(value = 1)
    @Max(value = 200)
    @Column(name = "capacite", nullable = false)
    private Integer capacite;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "capacite_debout")
    private Integer capaciteDebout;

    @Min(value = 1990)
    @Max(value = 2100)
    @Column(name = "annee_fabrication")
    private Integer anneeFabrication;

    @Enumerated(EnumType.STRING)
    @Column(name = "energie")
    private EnergyType energie;

    @Column(name = "autonomie_km")
    private Integer autonomieKm;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private BusStatus statut;

    @Column(name = "date_mise_en_service")
    private LocalDate dateMiseEnService;

    @Column(name = "date_dernier_entretien")
    private LocalDate dateDernierEntretien;

    @Column(name = "prochain_entretien_km")
    private Integer prochainEntretienKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets", "operateur" }, allowSetters = true)
    private Ligne ligne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur chauffeur;

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

    public String getConstructeur() {
        return this.constructeur;
    }

    public Bus constructeur(String constructeur) {
        this.setConstructeur(constructeur);
        return this;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
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

    public Integer getCapaciteDebout() {
        return this.capaciteDebout;
    }

    public Bus capaciteDebout(Integer capaciteDebout) {
        this.setCapaciteDebout(capaciteDebout);
        return this;
    }

    public void setCapaciteDebout(Integer capaciteDebout) {
        this.capaciteDebout = capaciteDebout;
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

    public EnergyType getEnergie() {
        return this.energie;
    }

    public Bus energie(EnergyType energie) {
        this.setEnergie(energie);
        return this;
    }

    public void setEnergie(EnergyType energie) {
        this.energie = energie;
    }

    public Integer getAutonomieKm() {
        return this.autonomieKm;
    }

    public Bus autonomieKm(Integer autonomieKm) {
        this.setAutonomieKm(autonomieKm);
        return this;
    }

    public void setAutonomieKm(Integer autonomieKm) {
        this.autonomieKm = autonomieKm;
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

    public BusStatus getStatut() {
        return this.statut;
    }

    public Bus statut(BusStatus statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(BusStatus statut) {
        this.statut = statut;
    }

    public LocalDate getDateMiseEnService() {
        return this.dateMiseEnService;
    }

    public Bus dateMiseEnService(LocalDate dateMiseEnService) {
        this.setDateMiseEnService(dateMiseEnService);
        return this;
    }

    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public LocalDate getDateDernierEntretien() {
        return this.dateDernierEntretien;
    }

    public Bus dateDernierEntretien(LocalDate dateDernierEntretien) {
        this.setDateDernierEntretien(dateDernierEntretien);
        return this;
    }

    public void setDateDernierEntretien(LocalDate dateDernierEntretien) {
        this.dateDernierEntretien = dateDernierEntretien;
    }

    public Integer getProchainEntretienKm() {
        return this.prochainEntretienKm;
    }

    public Bus prochainEntretienKm(Integer prochainEntretienKm) {
        this.setProchainEntretienKm(prochainEntretienKm);
        return this;
    }

    public void setProchainEntretienKm(Integer prochainEntretienKm) {
        this.prochainEntretienKm = prochainEntretienKm;
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

    public Utilisateur getChauffeur() {
        return this.chauffeur;
    }

    public void setChauffeur(Utilisateur utilisateur) {
        this.chauffeur = utilisateur;
    }

    public Bus chauffeur(Utilisateur utilisateur) {
        this.setChauffeur(utilisateur);
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
            ", constructeur='" + getConstructeur() + "'" +
            ", capacite=" + getCapacite() +
            ", capaciteDebout=" + getCapaciteDebout() +
            ", anneeFabrication=" + getAnneeFabrication() +
            ", energie='" + getEnergie() + "'" +
            ", autonomieKm=" + getAutonomieKm() +
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
            ", dateMiseEnService='" + getDateMiseEnService() + "'" +
            ", dateDernierEntretien='" + getDateDernierEntretien() + "'" +
            ", prochainEntretienKm=" + getProchainEntretienKm() +
            "}";
    }
}
