package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Favori} entity. This class is used
 * in {@link sn.yegg.app.web.rest.FavoriResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /favoris?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private LongFilter cibleId;

    private StringFilter nomPersonnalise;

    private IntegerFilter ordre;

    private BooleanFilter alerteActive;

    private IntegerFilter alerteSeuil;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public FavoriCriteria() {}

    public FavoriCriteria(FavoriCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.cibleId = other.optionalCibleId().map(LongFilter::copy).orElse(null);
        this.nomPersonnalise = other.optionalNomPersonnalise().map(StringFilter::copy).orElse(null);
        this.ordre = other.optionalOrdre().map(IntegerFilter::copy).orElse(null);
        this.alerteActive = other.optionalAlerteActive().map(BooleanFilter::copy).orElse(null);
        this.alerteSeuil = other.optionalAlerteSeuil().map(IntegerFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FavoriCriteria copy() {
        return new FavoriCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getCibleId() {
        return cibleId;
    }

    public Optional<LongFilter> optionalCibleId() {
        return Optional.ofNullable(cibleId);
    }

    public LongFilter cibleId() {
        if (cibleId == null) {
            setCibleId(new LongFilter());
        }
        return cibleId;
    }

    public void setCibleId(LongFilter cibleId) {
        this.cibleId = cibleId;
    }

    public StringFilter getNomPersonnalise() {
        return nomPersonnalise;
    }

    public Optional<StringFilter> optionalNomPersonnalise() {
        return Optional.ofNullable(nomPersonnalise);
    }

    public StringFilter nomPersonnalise() {
        if (nomPersonnalise == null) {
            setNomPersonnalise(new StringFilter());
        }
        return nomPersonnalise;
    }

    public void setNomPersonnalise(StringFilter nomPersonnalise) {
        this.nomPersonnalise = nomPersonnalise;
    }

    public IntegerFilter getOrdre() {
        return ordre;
    }

    public Optional<IntegerFilter> optionalOrdre() {
        return Optional.ofNullable(ordre);
    }

    public IntegerFilter ordre() {
        if (ordre == null) {
            setOrdre(new IntegerFilter());
        }
        return ordre;
    }

    public void setOrdre(IntegerFilter ordre) {
        this.ordre = ordre;
    }

    public BooleanFilter getAlerteActive() {
        return alerteActive;
    }

    public Optional<BooleanFilter> optionalAlerteActive() {
        return Optional.ofNullable(alerteActive);
    }

    public BooleanFilter alerteActive() {
        if (alerteActive == null) {
            setAlerteActive(new BooleanFilter());
        }
        return alerteActive;
    }

    public void setAlerteActive(BooleanFilter alerteActive) {
        this.alerteActive = alerteActive;
    }

    public IntegerFilter getAlerteSeuil() {
        return alerteSeuil;
    }

    public Optional<IntegerFilter> optionalAlerteSeuil() {
        return Optional.ofNullable(alerteSeuil);
    }

    public IntegerFilter alerteSeuil() {
        if (alerteSeuil == null) {
            setAlerteSeuil(new IntegerFilter());
        }
        return alerteSeuil;
    }

    public void setAlerteSeuil(IntegerFilter alerteSeuil) {
        this.alerteSeuil = alerteSeuil;
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
        final FavoriCriteria that = (FavoriCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(cibleId, that.cibleId) &&
            Objects.equals(nomPersonnalise, that.nomPersonnalise) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(alerteActive, that.alerteActive) &&
            Objects.equals(alerteSeuil, that.alerteSeuil) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, cibleId, nomPersonnalise, ordre, alerteActive, alerteSeuil, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalCibleId().map(f -> "cibleId=" + f + ", ").orElse("") +
            optionalNomPersonnalise().map(f -> "nomPersonnalise=" + f + ", ").orElse("") +
            optionalOrdre().map(f -> "ordre=" + f + ", ").orElse("") +
            optionalAlerteActive().map(f -> "alerteActive=" + f + ", ").orElse("") +
            optionalAlerteSeuil().map(f -> "alerteSeuil=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
