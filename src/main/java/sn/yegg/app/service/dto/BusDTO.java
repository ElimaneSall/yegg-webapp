package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.BusStatus;
import sn.yegg.app.domain.enumeration.EnergyType;

/**
 * A DTO for the {@link sn.yegg.app.domain.Bus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroVehicule;

    @NotNull
    private String plaque;

    private String modele;

    private String constructeur;

    @NotNull
    @Min(value = 1)
    @Max(value = 200)
    private Integer capacite;

    @Min(value = 0)
    @Max(value = 100)
    private Integer capaciteDebout;

    @Min(value = 1990)
    @Max(value = 2100)
    private Integer anneeFabrication;

    private EnergyType energie;

    private Integer autonomieKm;

    private String gpsDeviceId;

    private String gpsStatus;

    private Instant gpsLastPing;

    @Min(value = 0)
    @Max(value = 100)
    private Integer gpsBatteryLevel;

    private BigDecimal currentLatitude;

    private BigDecimal currentLongitude;

    private BigDecimal currentVitesse;

    @Min(value = 0)
    @Max(value = 359)
    private Integer currentCap;

    private Instant positionUpdatedAt;

    @NotNull
    private BusStatus statut;

    private LocalDate dateMiseEnService;

    private LocalDate dateDernierEntretien;

    private Integer prochainEntretienKm;

    private LigneDTO ligne;

    private UtilisateurDTO chauffeur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getConstructeur() {
        return constructeur;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getCapaciteDebout() {
        return capaciteDebout;
    }

    public void setCapaciteDebout(Integer capaciteDebout) {
        this.capaciteDebout = capaciteDebout;
    }

    public Integer getAnneeFabrication() {
        return anneeFabrication;
    }

    public void setAnneeFabrication(Integer anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    public EnergyType getEnergie() {
        return energie;
    }

    public void setEnergie(EnergyType energie) {
        this.energie = energie;
    }

    public Integer getAutonomieKm() {
        return autonomieKm;
    }

    public void setAutonomieKm(Integer autonomieKm) {
        this.autonomieKm = autonomieKm;
    }

    public String getGpsDeviceId() {
        return gpsDeviceId;
    }

    public void setGpsDeviceId(String gpsDeviceId) {
        this.gpsDeviceId = gpsDeviceId;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Instant getGpsLastPing() {
        return gpsLastPing;
    }

    public void setGpsLastPing(Instant gpsLastPing) {
        this.gpsLastPing = gpsLastPing;
    }

    public Integer getGpsBatteryLevel() {
        return gpsBatteryLevel;
    }

    public void setGpsBatteryLevel(Integer gpsBatteryLevel) {
        this.gpsBatteryLevel = gpsBatteryLevel;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public BigDecimal getCurrentVitesse() {
        return currentVitesse;
    }

    public void setCurrentVitesse(BigDecimal currentVitesse) {
        this.currentVitesse = currentVitesse;
    }

    public Integer getCurrentCap() {
        return currentCap;
    }

    public void setCurrentCap(Integer currentCap) {
        this.currentCap = currentCap;
    }

    public Instant getPositionUpdatedAt() {
        return positionUpdatedAt;
    }

    public void setPositionUpdatedAt(Instant positionUpdatedAt) {
        this.positionUpdatedAt = positionUpdatedAt;
    }

    public BusStatus getStatut() {
        return statut;
    }

    public void setStatut(BusStatus statut) {
        this.statut = statut;
    }

    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public LocalDate getDateDernierEntretien() {
        return dateDernierEntretien;
    }

    public void setDateDernierEntretien(LocalDate dateDernierEntretien) {
        this.dateDernierEntretien = dateDernierEntretien;
    }

    public Integer getProchainEntretienKm() {
        return prochainEntretienKm;
    }

    public void setProchainEntretienKm(Integer prochainEntretienKm) {
        this.prochainEntretienKm = prochainEntretienKm;
    }

    public LigneDTO getLigne() {
        return ligne;
    }

    public void setLigne(LigneDTO ligne) {
        this.ligne = ligne;
    }

    public UtilisateurDTO getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(UtilisateurDTO chauffeur) {
        this.chauffeur = chauffeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusDTO)) {
            return false;
        }

        BusDTO busDTO = (BusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, busDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusDTO{" +
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
            ", ligne=" + getLigne() +
            ", chauffeur=" + getChauffeur() +
            "}";
    }
}
