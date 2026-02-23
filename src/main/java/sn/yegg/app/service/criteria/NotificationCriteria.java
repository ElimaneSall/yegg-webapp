package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.NotificationStatus;
import sn.yegg.app.domain.enumeration.NotificationType;
import sn.yegg.app.domain.enumeration.Priority;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Notification} entity. This class is used
 * in {@link sn.yegg.app.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {}

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private NotificationTypeFilter type;

    private StringFilter titre;

    private PriorityFilter priorite;

    private NotificationStatusFilter statut;

    private InstantFilter dateCreation;

    private InstantFilter dateEnvoi;

    private BooleanFilter lu;

    private InstantFilter dateLecture;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(NotificationTypeFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.priorite = other.optionalPriorite().map(PriorityFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(NotificationStatusFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.dateEnvoi = other.optionalDateEnvoi().map(InstantFilter::copy).orElse(null);
        this.lu = other.optionalLu().map(BooleanFilter::copy).orElse(null);
        this.dateLecture = other.optionalDateLecture().map(InstantFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public NotificationTypeFilter getType() {
        return type;
    }

    public Optional<NotificationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public NotificationTypeFilter type() {
        if (type == null) {
            setType(new NotificationTypeFilter());
        }
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
    }

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
    }

    public PriorityFilter getPriorite() {
        return priorite;
    }

    public Optional<PriorityFilter> optionalPriorite() {
        return Optional.ofNullable(priorite);
    }

    public PriorityFilter priorite() {
        if (priorite == null) {
            setPriorite(new PriorityFilter());
        }
        return priorite;
    }

    public void setPriorite(PriorityFilter priorite) {
        this.priorite = priorite;
    }

    public NotificationStatusFilter getStatut() {
        return statut;
    }

    public Optional<NotificationStatusFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public NotificationStatusFilter statut() {
        if (statut == null) {
            setStatut(new NotificationStatusFilter());
        }
        return statut;
    }

    public void setStatut(NotificationStatusFilter statut) {
        this.statut = statut;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public Optional<InstantFilter> optionalDateCreation() {
        return Optional.ofNullable(dateCreation);
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            setDateCreation(new InstantFilter());
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public InstantFilter getDateEnvoi() {
        return dateEnvoi;
    }

    public Optional<InstantFilter> optionalDateEnvoi() {
        return Optional.ofNullable(dateEnvoi);
    }

    public InstantFilter dateEnvoi() {
        if (dateEnvoi == null) {
            setDateEnvoi(new InstantFilter());
        }
        return dateEnvoi;
    }

    public void setDateEnvoi(InstantFilter dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public BooleanFilter getLu() {
        return lu;
    }

    public Optional<BooleanFilter> optionalLu() {
        return Optional.ofNullable(lu);
    }

    public BooleanFilter lu() {
        if (lu == null) {
            setLu(new BooleanFilter());
        }
        return lu;
    }

    public void setLu(BooleanFilter lu) {
        this.lu = lu;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(priorite, that.priorite) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateEnvoi, that.dateEnvoi) &&
            Objects.equals(lu, that.lu) &&
            Objects.equals(dateLecture, that.dateLecture) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, titre, priorite, statut, dateCreation, dateEnvoi, lu, dateLecture, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalPriorite().map(f -> "priorite=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDateEnvoi().map(f -> "dateEnvoi=" + f + ", ").orElse("") +
            optionalLu().map(f -> "lu=" + f + ", ").orElse("") +
            optionalDateLecture().map(f -> "dateLecture=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
