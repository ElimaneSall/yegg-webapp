package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.FavoriteType;
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

    /**
     * Class for filtering FavoriteType
     */
    public static class FavoriteTypeFilter extends Filter<FavoriteType> {

        public FavoriteTypeFilter() {}

        public FavoriteTypeFilter(FavoriteTypeFilter filter) {
            super(filter);
        }

        @Override
        public FavoriteTypeFilter copy() {
            return new FavoriteTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FavoriteTypeFilter type;

    private LongFilter cibleId;

    private StringFilter nomPersonnalise;

    private IntegerFilter ordre;

    private InstantFilter dateAjout;

    private InstantFilter dernierAcces;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public FavoriCriteria() {}

    public FavoriCriteria(FavoriCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(FavoriteTypeFilter::copy).orElse(null);
        this.cibleId = other.optionalCibleId().map(LongFilter::copy).orElse(null);
        this.nomPersonnalise = other.optionalNomPersonnalise().map(StringFilter::copy).orElse(null);
        this.ordre = other.optionalOrdre().map(IntegerFilter::copy).orElse(null);
        this.dateAjout = other.optionalDateAjout().map(InstantFilter::copy).orElse(null);
        this.dernierAcces = other.optionalDernierAcces().map(InstantFilter::copy).orElse(null);
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

    public FavoriteTypeFilter getType() {
        return type;
    }

    public Optional<FavoriteTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public FavoriteTypeFilter type() {
        if (type == null) {
            setType(new FavoriteTypeFilter());
        }
        return type;
    }

    public void setType(FavoriteTypeFilter type) {
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

    public InstantFilter getDateAjout() {
        return dateAjout;
    }

    public Optional<InstantFilter> optionalDateAjout() {
        return Optional.ofNullable(dateAjout);
    }

    public InstantFilter dateAjout() {
        if (dateAjout == null) {
            setDateAjout(new InstantFilter());
        }
        return dateAjout;
    }

    public void setDateAjout(InstantFilter dateAjout) {
        this.dateAjout = dateAjout;
    }

    public InstantFilter getDernierAcces() {
        return dernierAcces;
    }

    public Optional<InstantFilter> optionalDernierAcces() {
        return Optional.ofNullable(dernierAcces);
    }

    public InstantFilter dernierAcces() {
        if (dernierAcces == null) {
            setDernierAcces(new InstantFilter());
        }
        return dernierAcces;
    }

    public void setDernierAcces(InstantFilter dernierAcces) {
        this.dernierAcces = dernierAcces;
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
            Objects.equals(dateAjout, that.dateAjout) &&
            Objects.equals(dernierAcces, that.dernierAcces) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, cibleId, nomPersonnalise, ordre, dateAjout, dernierAcces, utilisateurId, distinct);
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
            optionalDateAjout().map(f -> "dateAjout=" + f + ", ").orElse("") +
            optionalDernierAcces().map(f -> "dernierAcces=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
