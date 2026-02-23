package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Feedback} entity. This class is used
 * in {@link sn.yegg.app.web.rest.FeedbackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /feedbacks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter note;

    private StringFilter typeObjet;

    private LongFilter objetId;

    private InstantFilter dateCreation;

    private BooleanFilter anonyme;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public FeedbackCriteria() {}

    public FeedbackCriteria(FeedbackCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.note = other.optionalNote().map(IntegerFilter::copy).orElse(null);
        this.typeObjet = other.optionalTypeObjet().map(StringFilter::copy).orElse(null);
        this.objetId = other.optionalObjetId().map(LongFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.anonyme = other.optionalAnonyme().map(BooleanFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FeedbackCriteria copy() {
        return new FeedbackCriteria(this);
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

    public IntegerFilter getNote() {
        return note;
    }

    public Optional<IntegerFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public IntegerFilter note() {
        if (note == null) {
            setNote(new IntegerFilter());
        }
        return note;
    }

    public void setNote(IntegerFilter note) {
        this.note = note;
    }

    public StringFilter getTypeObjet() {
        return typeObjet;
    }

    public Optional<StringFilter> optionalTypeObjet() {
        return Optional.ofNullable(typeObjet);
    }

    public StringFilter typeObjet() {
        if (typeObjet == null) {
            setTypeObjet(new StringFilter());
        }
        return typeObjet;
    }

    public void setTypeObjet(StringFilter typeObjet) {
        this.typeObjet = typeObjet;
    }

    public LongFilter getObjetId() {
        return objetId;
    }

    public Optional<LongFilter> optionalObjetId() {
        return Optional.ofNullable(objetId);
    }

    public LongFilter objetId() {
        if (objetId == null) {
            setObjetId(new LongFilter());
        }
        return objetId;
    }

    public void setObjetId(LongFilter objetId) {
        this.objetId = objetId;
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

    public BooleanFilter getAnonyme() {
        return anonyme;
    }

    public Optional<BooleanFilter> optionalAnonyme() {
        return Optional.ofNullable(anonyme);
    }

    public BooleanFilter anonyme() {
        if (anonyme == null) {
            setAnonyme(new BooleanFilter());
        }
        return anonyme;
    }

    public void setAnonyme(BooleanFilter anonyme) {
        this.anonyme = anonyme;
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
        final FeedbackCriteria that = (FeedbackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(note, that.note) &&
            Objects.equals(typeObjet, that.typeObjet) &&
            Objects.equals(objetId, that.objetId) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(anonyme, that.anonyme) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, typeObjet, objetId, dateCreation, anonyme, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalTypeObjet().map(f -> "typeObjet=" + f + ", ").orElse("") +
            optionalObjetId().map(f -> "objetId=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalAnonyme().map(f -> "anonyme=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
