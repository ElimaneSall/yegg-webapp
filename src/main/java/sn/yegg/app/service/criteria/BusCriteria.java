package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.BusStatus;
import sn.yegg.app.domain.enumeration.EnergyType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Bus} entity. This class is used
 * in {@link sn.yegg.app.web.rest.BusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnergyType
     */
    public static class EnergyTypeFilter extends Filter<EnergyType> {

        public EnergyTypeFilter() {}

        public EnergyTypeFilter(EnergyTypeFilter filter) {
            super(filter);
        }

        @Override
        public EnergyTypeFilter copy() {
            return new EnergyTypeFilter(this);
        }
    }

    /**
     * Class for filtering BusStatus
     */
    public static class BusStatusFilter extends Filter<BusStatus> {

        public BusStatusFilter() {}

        public BusStatusFilter(BusStatusFilter filter) {
            super(filter);
        }

        @Override
        public BusStatusFilter copy() {
            return new BusStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroVehicule;

    private StringFilter plaque;

    private StringFilter modele;

    private StringFilter constructeur;

    private IntegerFilter capacite;

    private IntegerFilter capaciteDebout;

    private IntegerFilter anneeFabrication;

    private EnergyTypeFilter energie;

    private IntegerFilter autonomieKm;

    private StringFilter gpsDeviceId;

    private StringFilter gpsStatus;

    private InstantFilter gpsLastPing;

    private IntegerFilter gpsBatteryLevel;

    private BigDecimalFilter currentLatitude;

    private BigDecimalFilter currentLongitude;

    private BigDecimalFilter currentVitesse;

    private IntegerFilter currentCap;

    private InstantFilter positionUpdatedAt;

    private BusStatusFilter statut;

    private LocalDateFilter dateMiseEnService;

    private LocalDateFilter dateDernierEntretien;

    private IntegerFilter prochainEntretienKm;

    private LongFilter ligneId;

    private LongFilter chauffeurId;

    private Boolean distinct;

    public BusCriteria() {}

    public BusCriteria(BusCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroVehicule = other.optionalNumeroVehicule().map(StringFilter::copy).orElse(null);
        this.plaque = other.optionalPlaque().map(StringFilter::copy).orElse(null);
        this.modele = other.optionalModele().map(StringFilter::copy).orElse(null);
        this.constructeur = other.optionalConstructeur().map(StringFilter::copy).orElse(null);
        this.capacite = other.optionalCapacite().map(IntegerFilter::copy).orElse(null);
        this.capaciteDebout = other.optionalCapaciteDebout().map(IntegerFilter::copy).orElse(null);
        this.anneeFabrication = other.optionalAnneeFabrication().map(IntegerFilter::copy).orElse(null);
        this.energie = other.optionalEnergie().map(EnergyTypeFilter::copy).orElse(null);
        this.autonomieKm = other.optionalAutonomieKm().map(IntegerFilter::copy).orElse(null);
        this.gpsDeviceId = other.optionalGpsDeviceId().map(StringFilter::copy).orElse(null);
        this.gpsStatus = other.optionalGpsStatus().map(StringFilter::copy).orElse(null);
        this.gpsLastPing = other.optionalGpsLastPing().map(InstantFilter::copy).orElse(null);
        this.gpsBatteryLevel = other.optionalGpsBatteryLevel().map(IntegerFilter::copy).orElse(null);
        this.currentLatitude = other.optionalCurrentLatitude().map(BigDecimalFilter::copy).orElse(null);
        this.currentLongitude = other.optionalCurrentLongitude().map(BigDecimalFilter::copy).orElse(null);
        this.currentVitesse = other.optionalCurrentVitesse().map(BigDecimalFilter::copy).orElse(null);
        this.currentCap = other.optionalCurrentCap().map(IntegerFilter::copy).orElse(null);
        this.positionUpdatedAt = other.optionalPositionUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(BusStatusFilter::copy).orElse(null);
        this.dateMiseEnService = other.optionalDateMiseEnService().map(LocalDateFilter::copy).orElse(null);
        this.dateDernierEntretien = other.optionalDateDernierEntretien().map(LocalDateFilter::copy).orElse(null);
        this.prochainEntretienKm = other.optionalProchainEntretienKm().map(IntegerFilter::copy).orElse(null);
        this.ligneId = other.optionalLigneId().map(LongFilter::copy).orElse(null);
        this.chauffeurId = other.optionalChauffeurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BusCriteria copy() {
        return new BusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumeroVehicule() {
        return numeroVehicule;
    }

    public Optional<StringFilter> optionalNumeroVehicule() {
        return Optional.ofNullable(numeroVehicule);
    }

    public StringFilter numeroVehicule() {
        if (numeroVehicule == null) {
            setNumeroVehicule(new StringFilter());
        }
        return numeroVehicule;
    }

    public void setNumeroVehicule(StringFilter numeroVehicule) {
        this.numeroVehicule = numeroVehicule;
    }

    public StringFilter getPlaque() {
        return plaque;
    }

    public Optional<StringFilter> optionalPlaque() {
        return Optional.ofNullable(plaque);
    }

    public StringFilter plaque() {
        if (plaque == null) {
            setPlaque(new StringFilter());
        }
        return plaque;
    }

    public void setPlaque(StringFilter plaque) {
        this.plaque = plaque;
    }

    public StringFilter getModele() {
        return modele;
    }

    public Optional<StringFilter> optionalModele() {
        return Optional.ofNullable(modele);
    }

    public StringFilter modele() {
        if (modele == null) {
            setModele(new StringFilter());
        }
        return modele;
    }

    public void setModele(StringFilter modele) {
        this.modele = modele;
    }

    public StringFilter getConstructeur() {
        return constructeur;
    }

    public Optional<StringFilter> optionalConstructeur() {
        return Optional.ofNullable(constructeur);
    }

    public StringFilter constructeur() {
        if (constructeur == null) {
            setConstructeur(new StringFilter());
        }
        return constructeur;
    }

    public void setConstructeur(StringFilter constructeur) {
        this.constructeur = constructeur;
    }

    public IntegerFilter getCapacite() {
        return capacite;
    }

    public Optional<IntegerFilter> optionalCapacite() {
        return Optional.ofNullable(capacite);
    }

    public IntegerFilter capacite() {
        if (capacite == null) {
            setCapacite(new IntegerFilter());
        }
        return capacite;
    }

    public void setCapacite(IntegerFilter capacite) {
        this.capacite = capacite;
    }

    public IntegerFilter getCapaciteDebout() {
        return capaciteDebout;
    }

    public Optional<IntegerFilter> optionalCapaciteDebout() {
        return Optional.ofNullable(capaciteDebout);
    }

    public IntegerFilter capaciteDebout() {
        if (capaciteDebout == null) {
            setCapaciteDebout(new IntegerFilter());
        }
        return capaciteDebout;
    }

    public void setCapaciteDebout(IntegerFilter capaciteDebout) {
        this.capaciteDebout = capaciteDebout;
    }

    public IntegerFilter getAnneeFabrication() {
        return anneeFabrication;
    }

    public Optional<IntegerFilter> optionalAnneeFabrication() {
        return Optional.ofNullable(anneeFabrication);
    }

    public IntegerFilter anneeFabrication() {
        if (anneeFabrication == null) {
            setAnneeFabrication(new IntegerFilter());
        }
        return anneeFabrication;
    }

    public void setAnneeFabrication(IntegerFilter anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    public EnergyTypeFilter getEnergie() {
        return energie;
    }

    public Optional<EnergyTypeFilter> optionalEnergie() {
        return Optional.ofNullable(energie);
    }

    public EnergyTypeFilter energie() {
        if (energie == null) {
            setEnergie(new EnergyTypeFilter());
        }
        return energie;
    }

    public void setEnergie(EnergyTypeFilter energie) {
        this.energie = energie;
    }

    public IntegerFilter getAutonomieKm() {
        return autonomieKm;
    }

    public Optional<IntegerFilter> optionalAutonomieKm() {
        return Optional.ofNullable(autonomieKm);
    }

    public IntegerFilter autonomieKm() {
        if (autonomieKm == null) {
            setAutonomieKm(new IntegerFilter());
        }
        return autonomieKm;
    }

    public void setAutonomieKm(IntegerFilter autonomieKm) {
        this.autonomieKm = autonomieKm;
    }

    public StringFilter getGpsDeviceId() {
        return gpsDeviceId;
    }

    public Optional<StringFilter> optionalGpsDeviceId() {
        return Optional.ofNullable(gpsDeviceId);
    }

    public StringFilter gpsDeviceId() {
        if (gpsDeviceId == null) {
            setGpsDeviceId(new StringFilter());
        }
        return gpsDeviceId;
    }

    public void setGpsDeviceId(StringFilter gpsDeviceId) {
        this.gpsDeviceId = gpsDeviceId;
    }

    public StringFilter getGpsStatus() {
        return gpsStatus;
    }

    public Optional<StringFilter> optionalGpsStatus() {
        return Optional.ofNullable(gpsStatus);
    }

    public StringFilter gpsStatus() {
        if (gpsStatus == null) {
            setGpsStatus(new StringFilter());
        }
        return gpsStatus;
    }

    public void setGpsStatus(StringFilter gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public InstantFilter getGpsLastPing() {
        return gpsLastPing;
    }

    public Optional<InstantFilter> optionalGpsLastPing() {
        return Optional.ofNullable(gpsLastPing);
    }

    public InstantFilter gpsLastPing() {
        if (gpsLastPing == null) {
            setGpsLastPing(new InstantFilter());
        }
        return gpsLastPing;
    }

    public void setGpsLastPing(InstantFilter gpsLastPing) {
        this.gpsLastPing = gpsLastPing;
    }

    public IntegerFilter getGpsBatteryLevel() {
        return gpsBatteryLevel;
    }

    public Optional<IntegerFilter> optionalGpsBatteryLevel() {
        return Optional.ofNullable(gpsBatteryLevel);
    }

    public IntegerFilter gpsBatteryLevel() {
        if (gpsBatteryLevel == null) {
            setGpsBatteryLevel(new IntegerFilter());
        }
        return gpsBatteryLevel;
    }

    public void setGpsBatteryLevel(IntegerFilter gpsBatteryLevel) {
        this.gpsBatteryLevel = gpsBatteryLevel;
    }

    public BigDecimalFilter getCurrentLatitude() {
        return currentLatitude;
    }

    public Optional<BigDecimalFilter> optionalCurrentLatitude() {
        return Optional.ofNullable(currentLatitude);
    }

    public BigDecimalFilter currentLatitude() {
        if (currentLatitude == null) {
            setCurrentLatitude(new BigDecimalFilter());
        }
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimalFilter currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimalFilter getCurrentLongitude() {
        return currentLongitude;
    }

    public Optional<BigDecimalFilter> optionalCurrentLongitude() {
        return Optional.ofNullable(currentLongitude);
    }

    public BigDecimalFilter currentLongitude() {
        if (currentLongitude == null) {
            setCurrentLongitude(new BigDecimalFilter());
        }
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimalFilter currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public BigDecimalFilter getCurrentVitesse() {
        return currentVitesse;
    }

    public Optional<BigDecimalFilter> optionalCurrentVitesse() {
        return Optional.ofNullable(currentVitesse);
    }

    public BigDecimalFilter currentVitesse() {
        if (currentVitesse == null) {
            setCurrentVitesse(new BigDecimalFilter());
        }
        return currentVitesse;
    }

    public void setCurrentVitesse(BigDecimalFilter currentVitesse) {
        this.currentVitesse = currentVitesse;
    }

    public IntegerFilter getCurrentCap() {
        return currentCap;
    }

    public Optional<IntegerFilter> optionalCurrentCap() {
        return Optional.ofNullable(currentCap);
    }

    public IntegerFilter currentCap() {
        if (currentCap == null) {
            setCurrentCap(new IntegerFilter());
        }
        return currentCap;
    }

    public void setCurrentCap(IntegerFilter currentCap) {
        this.currentCap = currentCap;
    }

    public InstantFilter getPositionUpdatedAt() {
        return positionUpdatedAt;
    }

    public Optional<InstantFilter> optionalPositionUpdatedAt() {
        return Optional.ofNullable(positionUpdatedAt);
    }

    public InstantFilter positionUpdatedAt() {
        if (positionUpdatedAt == null) {
            setPositionUpdatedAt(new InstantFilter());
        }
        return positionUpdatedAt;
    }

    public void setPositionUpdatedAt(InstantFilter positionUpdatedAt) {
        this.positionUpdatedAt = positionUpdatedAt;
    }

    public BusStatusFilter getStatut() {
        return statut;
    }

    public Optional<BusStatusFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public BusStatusFilter statut() {
        if (statut == null) {
            setStatut(new BusStatusFilter());
        }
        return statut;
    }

    public void setStatut(BusStatusFilter statut) {
        this.statut = statut;
    }

    public LocalDateFilter getDateMiseEnService() {
        return dateMiseEnService;
    }

    public Optional<LocalDateFilter> optionalDateMiseEnService() {
        return Optional.ofNullable(dateMiseEnService);
    }

    public LocalDateFilter dateMiseEnService() {
        if (dateMiseEnService == null) {
            setDateMiseEnService(new LocalDateFilter());
        }
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateFilter dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public LocalDateFilter getDateDernierEntretien() {
        return dateDernierEntretien;
    }

    public Optional<LocalDateFilter> optionalDateDernierEntretien() {
        return Optional.ofNullable(dateDernierEntretien);
    }

    public LocalDateFilter dateDernierEntretien() {
        if (dateDernierEntretien == null) {
            setDateDernierEntretien(new LocalDateFilter());
        }
        return dateDernierEntretien;
    }

    public void setDateDernierEntretien(LocalDateFilter dateDernierEntretien) {
        this.dateDernierEntretien = dateDernierEntretien;
    }

    public IntegerFilter getProchainEntretienKm() {
        return prochainEntretienKm;
    }

    public Optional<IntegerFilter> optionalProchainEntretienKm() {
        return Optional.ofNullable(prochainEntretienKm);
    }

    public IntegerFilter prochainEntretienKm() {
        if (prochainEntretienKm == null) {
            setProchainEntretienKm(new IntegerFilter());
        }
        return prochainEntretienKm;
    }

    public void setProchainEntretienKm(IntegerFilter prochainEntretienKm) {
        this.prochainEntretienKm = prochainEntretienKm;
    }

    public LongFilter getLigneId() {
        return ligneId;
    }

    public Optional<LongFilter> optionalLigneId() {
        return Optional.ofNullable(ligneId);
    }

    public LongFilter ligneId() {
        if (ligneId == null) {
            setLigneId(new LongFilter());
        }
        return ligneId;
    }

    public void setLigneId(LongFilter ligneId) {
        this.ligneId = ligneId;
    }

    public LongFilter getChauffeurId() {
        return chauffeurId;
    }

    public Optional<LongFilter> optionalChauffeurId() {
        return Optional.ofNullable(chauffeurId);
    }

    public LongFilter chauffeurId() {
        if (chauffeurId == null) {
            setChauffeurId(new LongFilter());
        }
        return chauffeurId;
    }

    public void setChauffeurId(LongFilter chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BusCriteria that = (BusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroVehicule, that.numeroVehicule) &&
            Objects.equals(plaque, that.plaque) &&
            Objects.equals(modele, that.modele) &&
            Objects.equals(constructeur, that.constructeur) &&
            Objects.equals(capacite, that.capacite) &&
            Objects.equals(capaciteDebout, that.capaciteDebout) &&
            Objects.equals(anneeFabrication, that.anneeFabrication) &&
            Objects.equals(energie, that.energie) &&
            Objects.equals(autonomieKm, that.autonomieKm) &&
            Objects.equals(gpsDeviceId, that.gpsDeviceId) &&
            Objects.equals(gpsStatus, that.gpsStatus) &&
            Objects.equals(gpsLastPing, that.gpsLastPing) &&
            Objects.equals(gpsBatteryLevel, that.gpsBatteryLevel) &&
            Objects.equals(currentLatitude, that.currentLatitude) &&
            Objects.equals(currentLongitude, that.currentLongitude) &&
            Objects.equals(currentVitesse, that.currentVitesse) &&
            Objects.equals(currentCap, that.currentCap) &&
            Objects.equals(positionUpdatedAt, that.positionUpdatedAt) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateMiseEnService, that.dateMiseEnService) &&
            Objects.equals(dateDernierEntretien, that.dateDernierEntretien) &&
            Objects.equals(prochainEntretienKm, that.prochainEntretienKm) &&
            Objects.equals(ligneId, that.ligneId) &&
            Objects.equals(chauffeurId, that.chauffeurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroVehicule,
            plaque,
            modele,
            constructeur,
            capacite,
            capaciteDebout,
            anneeFabrication,
            energie,
            autonomieKm,
            gpsDeviceId,
            gpsStatus,
            gpsLastPing,
            gpsBatteryLevel,
            currentLatitude,
            currentLongitude,
            currentVitesse,
            currentCap,
            positionUpdatedAt,
            statut,
            dateMiseEnService,
            dateDernierEntretien,
            prochainEntretienKm,
            ligneId,
            chauffeurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroVehicule().map(f -> "numeroVehicule=" + f + ", ").orElse("") +
            optionalPlaque().map(f -> "plaque=" + f + ", ").orElse("") +
            optionalModele().map(f -> "modele=" + f + ", ").orElse("") +
            optionalConstructeur().map(f -> "constructeur=" + f + ", ").orElse("") +
            optionalCapacite().map(f -> "capacite=" + f + ", ").orElse("") +
            optionalCapaciteDebout().map(f -> "capaciteDebout=" + f + ", ").orElse("") +
            optionalAnneeFabrication().map(f -> "anneeFabrication=" + f + ", ").orElse("") +
            optionalEnergie().map(f -> "energie=" + f + ", ").orElse("") +
            optionalAutonomieKm().map(f -> "autonomieKm=" + f + ", ").orElse("") +
            optionalGpsDeviceId().map(f -> "gpsDeviceId=" + f + ", ").orElse("") +
            optionalGpsStatus().map(f -> "gpsStatus=" + f + ", ").orElse("") +
            optionalGpsLastPing().map(f -> "gpsLastPing=" + f + ", ").orElse("") +
            optionalGpsBatteryLevel().map(f -> "gpsBatteryLevel=" + f + ", ").orElse("") +
            optionalCurrentLatitude().map(f -> "currentLatitude=" + f + ", ").orElse("") +
            optionalCurrentLongitude().map(f -> "currentLongitude=" + f + ", ").orElse("") +
            optionalCurrentVitesse().map(f -> "currentVitesse=" + f + ", ").orElse("") +
            optionalCurrentCap().map(f -> "currentCap=" + f + ", ").orElse("") +
            optionalPositionUpdatedAt().map(f -> "positionUpdatedAt=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateMiseEnService().map(f -> "dateMiseEnService=" + f + ", ").orElse("") +
            optionalDateDernierEntretien().map(f -> "dateDernierEntretien=" + f + ", ").orElse("") +
            optionalProchainEntretienKm().map(f -> "prochainEntretienKm=" + f + ", ").orElse("") +
            optionalLigneId().map(f -> "ligneId=" + f + ", ").orElse("") +
            optionalChauffeurId().map(f -> "chauffeurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
