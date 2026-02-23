package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.ThresholdType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.HistoriqueAlerte} entity. This class is used
 * in {@link sn.yegg.app.web.rest.HistoriqueAlerteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-alertes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueAlerteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ThresholdType
     */
    public static class ThresholdTypeFilter extends Filter<ThresholdType> {

        public ThresholdTypeFilter() {}

        public ThresholdTypeFilter(ThresholdTypeFilter filter) {
            super(filter);
        }

        @Override
        public ThresholdTypeFilter copy() {
            return new ThresholdTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateDeclenchement;

    private StringFilter busNumero;

    private IntegerFilter distanceReelle;

    private IntegerFilter tempsReel;

    private ThresholdTypeFilter typeDeclenchement;

    private BooleanFilter notificationEnvoyee;

    private InstantFilter dateLecture;

    private LongFilter busId;

    private LongFilter alerteApprocheId;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public HistoriqueAlerteCriteria() {}

    public HistoriqueAlerteCriteria(HistoriqueAlerteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateDeclenchement = other.optionalDateDeclenchement().map(InstantFilter::copy).orElse(null);
        this.busNumero = other.optionalBusNumero().map(StringFilter::copy).orElse(null);
        this.distanceReelle = other.optionalDistanceReelle().map(IntegerFilter::copy).orElse(null);
        this.tempsReel = other.optionalTempsReel().map(IntegerFilter::copy).orElse(null);
        this.typeDeclenchement = other.optionalTypeDeclenchement().map(ThresholdTypeFilter::copy).orElse(null);
        this.notificationEnvoyee = other.optionalNotificationEnvoyee().map(BooleanFilter::copy).orElse(null);
        this.dateLecture = other.optionalDateLecture().map(InstantFilter::copy).orElse(null);
        this.busId = other.optionalBusId().map(LongFilter::copy).orElse(null);
        this.alerteApprocheId = other.optionalAlerteApprocheId().map(LongFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriqueAlerteCriteria copy() {
        return new HistoriqueAlerteCriteria(this);
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

    public InstantFilter getDateDeclenchement() {
        return dateDeclenchement;
    }

    public Optional<InstantFilter> optionalDateDeclenchement() {
        return Optional.ofNullable(dateDeclenchement);
    }

    public InstantFilter dateDeclenchement() {
        if (dateDeclenchement == null) {
            setDateDeclenchement(new InstantFilter());
        }
        return dateDeclenchement;
    }

    public void setDateDeclenchement(InstantFilter dateDeclenchement) {
        this.dateDeclenchement = dateDeclenchement;
    }

    public StringFilter getBusNumero() {
        return busNumero;
    }

    public Optional<StringFilter> optionalBusNumero() {
        return Optional.ofNullable(busNumero);
    }

    public StringFilter busNumero() {
        if (busNumero == null) {
            setBusNumero(new StringFilter());
        }
        return busNumero;
    }

    public void setBusNumero(StringFilter busNumero) {
        this.busNumero = busNumero;
    }

    public IntegerFilter getDistanceReelle() {
        return distanceReelle;
    }

    public Optional<IntegerFilter> optionalDistanceReelle() {
        return Optional.ofNullable(distanceReelle);
    }

    public IntegerFilter distanceReelle() {
        if (distanceReelle == null) {
            setDistanceReelle(new IntegerFilter());
        }
        return distanceReelle;
    }

    public void setDistanceReelle(IntegerFilter distanceReelle) {
        this.distanceReelle = distanceReelle;
    }

    public IntegerFilter getTempsReel() {
        return tempsReel;
    }

    public Optional<IntegerFilter> optionalTempsReel() {
        return Optional.ofNullable(tempsReel);
    }

    public IntegerFilter tempsReel() {
        if (tempsReel == null) {
            setTempsReel(new IntegerFilter());
        }
        return tempsReel;
    }

    public void setTempsReel(IntegerFilter tempsReel) {
        this.tempsReel = tempsReel;
    }

    public ThresholdTypeFilter getTypeDeclenchement() {
        return typeDeclenchement;
    }

    public Optional<ThresholdTypeFilter> optionalTypeDeclenchement() {
        return Optional.ofNullable(typeDeclenchement);
    }

    public ThresholdTypeFilter typeDeclenchement() {
        if (typeDeclenchement == null) {
            setTypeDeclenchement(new ThresholdTypeFilter());
        }
        return typeDeclenchement;
    }

    public void setTypeDeclenchement(ThresholdTypeFilter typeDeclenchement) {
        this.typeDeclenchement = typeDeclenchement;
    }

    public BooleanFilter getNotificationEnvoyee() {
        return notificationEnvoyee;
    }

    public Optional<BooleanFilter> optionalNotificationEnvoyee() {
        return Optional.ofNullable(notificationEnvoyee);
    }

    public BooleanFilter notificationEnvoyee() {
        if (notificationEnvoyee == null) {
            setNotificationEnvoyee(new BooleanFilter());
        }
        return notificationEnvoyee;
    }

    public void setNotificationEnvoyee(BooleanFilter notificationEnvoyee) {
        this.notificationEnvoyee = notificationEnvoyee;
    }

    public InstantFilter getDateLecture() {
        return dateLecture;
    }

    public Optional<InstantFilter> optionalDateLecture() {
        return Optional.ofNullable(dateLecture);
    }

    public InstantFilter dateLecture() {
        if (dateLecture == null) {
            setDateLecture(new InstantFilter());
        }
        return dateLecture;
    }

    public void setDateLecture(InstantFilter dateLecture) {
        this.dateLecture = dateLecture;
    }

    public LongFilter getBusId() {
        return busId;
    }

    public Optional<LongFilter> optionalBusId() {
        return Optional.ofNullable(busId);
    }

    public LongFilter busId() {
        if (busId == null) {
            setBusId(new LongFilter());
        }
        return busId;
    }

    public void setBusId(LongFilter busId) {
        this.busId = busId;
    }

    public LongFilter getAlerteApprocheId() {
        return alerteApprocheId;
    }

    public Optional<LongFilter> optionalAlerteApprocheId() {
        return Optional.ofNullable(alerteApprocheId);
    }

    public LongFilter alerteApprocheId() {
        if (alerteApprocheId == null) {
            setAlerteApprocheId(new LongFilter());
        }
        return alerteApprocheId;
    }

    public void setAlerteApprocheId(LongFilter alerteApprocheId) {
        this.alerteApprocheId = alerteApprocheId;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
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
        final HistoriqueAlerteCriteria that = (HistoriqueAlerteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateDeclenchement, that.dateDeclenchement) &&
            Objects.equals(busNumero, that.busNumero) &&
            Objects.equals(distanceReelle, that.distanceReelle) &&
            Objects.equals(tempsReel, that.tempsReel) &&
            Objects.equals(typeDeclenchement, that.typeDeclenchement) &&
            Objects.equals(notificationEnvoyee, that.notificationEnvoyee) &&
            Objects.equals(dateLecture, that.dateLecture) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(alerteApprocheId, that.alerteApprocheId) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateDeclenchement,
            busNumero,
            distanceReelle,
            tempsReel,
            typeDeclenchement,
            notificationEnvoyee,
            dateLecture,
            busId,
            alerteApprocheId,
            utilisateurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueAlerteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateDeclenchement().map(f -> "dateDeclenchement=" + f + ", ").orElse("") +
            optionalBusNumero().map(f -> "busNumero=" + f + ", ").orElse("") +
            optionalDistanceReelle().map(f -> "distanceReelle=" + f + ", ").orElse("") +
            optionalTempsReel().map(f -> "tempsReel=" + f + ", ").orElse("") +
            optionalTypeDeclenchement().map(f -> "typeDeclenchement=" + f + ", ").orElse("") +
            optionalNotificationEnvoyee().map(f -> "notificationEnvoyee=" + f + ", ").orElse("") +
            optionalDateLecture().map(f -> "dateLecture=" + f + ", ").orElse("") +
            optionalBusId().map(f -> "busId=" + f + ", ").orElse("") +
            optionalAlerteApprocheId().map(f -> "alerteApprocheId=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
