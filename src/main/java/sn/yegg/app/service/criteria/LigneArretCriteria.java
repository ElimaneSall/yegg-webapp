package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.LigneArret} entity. This class is used
 * in {@link sn.yegg.app.web.rest.LigneArretResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ligne-arrets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneArretCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter ordre;

    private IntegerFilter tempsTrajetDepart;

    private BigDecimalFilter distanceDepart;

    private IntegerFilter tempsArretMoyen;

    private BooleanFilter arretPhysique;

    private LongFilter ligneId;

    private LongFilter arretId;

    private Boolean distinct;

    public LigneArretCriteria() {}

    public LigneArretCriteria(LigneArretCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ordre = other.optionalOrdre().map(IntegerFilter::copy).orElse(null);
        this.tempsTrajetDepart = other.optionalTempsTrajetDepart().map(IntegerFilter::copy).orElse(null);
        this.distanceDepart = other.optionalDistanceDepart().map(BigDecimalFilter::copy).orElse(null);
        this.tempsArretMoyen = other.optionalTempsArretMoyen().map(IntegerFilter::copy).orElse(null);
        this.arretPhysique = other.optionalArretPhysique().map(BooleanFilter::copy).orElse(null);
        this.ligneId = other.optionalLigneId().map(LongFilter::copy).orElse(null);
        this.arretId = other.optionalArretId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LigneArretCriteria copy() {
        return new LigneArretCriteria(this);
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

    public IntegerFilter getTempsTrajetDepart() {
        return tempsTrajetDepart;
    }

    public Optional<IntegerFilter> optionalTempsTrajetDepart() {
        return Optional.ofNullable(tempsTrajetDepart);
    }

    public IntegerFilter tempsTrajetDepart() {
        if (tempsTrajetDepart == null) {
            setTempsTrajetDepart(new IntegerFilter());
        }
        return tempsTrajetDepart;
    }

    public void setTempsTrajetDepart(IntegerFilter tempsTrajetDepart) {
        this.tempsTrajetDepart = tempsTrajetDepart;
    }

    public BigDecimalFilter getDistanceDepart() {
        return distanceDepart;
    }

    public Optional<BigDecimalFilter> optionalDistanceDepart() {
        return Optional.ofNullable(distanceDepart);
    }

    public BigDecimalFilter distanceDepart() {
        if (distanceDepart == null) {
            setDistanceDepart(new BigDecimalFilter());
        }
        return distanceDepart;
    }

    public void setDistanceDepart(BigDecimalFilter distanceDepart) {
        this.distanceDepart = distanceDepart;
    }

    public IntegerFilter getTempsArretMoyen() {
        return tempsArretMoyen;
    }

    public Optional<IntegerFilter> optionalTempsArretMoyen() {
        return Optional.ofNullable(tempsArretMoyen);
    }

    public IntegerFilter tempsArretMoyen() {
        if (tempsArretMoyen == null) {
            setTempsArretMoyen(new IntegerFilter());
        }
        return tempsArretMoyen;
    }

    public void setTempsArretMoyen(IntegerFilter tempsArretMoyen) {
        this.tempsArretMoyen = tempsArretMoyen;
    }

    public BooleanFilter getArretPhysique() {
        return arretPhysique;
    }

    public Optional<BooleanFilter> optionalArretPhysique() {
        return Optional.ofNullable(arretPhysique);
    }

    public BooleanFilter arretPhysique() {
        if (arretPhysique == null) {
            setArretPhysique(new BooleanFilter());
        }
        return arretPhysique;
    }

    public void setArretPhysique(BooleanFilter arretPhysique) {
        this.arretPhysique = arretPhysique;
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

    public LongFilter getArretId() {
        return arretId;
    }

    public Optional<LongFilter> optionalArretId() {
        return Optional.ofNullable(arretId);
    }

    public LongFilter arretId() {
        if (arretId == null) {
            setArretId(new LongFilter());
        }
        return arretId;
    }

    public void setArretId(LongFilter arretId) {
        this.arretId = arretId;
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
        final LigneArretCriteria that = (LigneArretCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(tempsTrajetDepart, that.tempsTrajetDepart) &&
            Objects.equals(distanceDepart, that.distanceDepart) &&
            Objects.equals(tempsArretMoyen, that.tempsArretMoyen) &&
            Objects.equals(arretPhysique, that.arretPhysique) &&
            Objects.equals(ligneId, that.ligneId) &&
            Objects.equals(arretId, that.arretId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ordre, tempsTrajetDepart, distanceDepart, tempsArretMoyen, arretPhysique, ligneId, arretId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneArretCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrdre().map(f -> "ordre=" + f + ", ").orElse("") +
            optionalTempsTrajetDepart().map(f -> "tempsTrajetDepart=" + f + ", ").orElse("") +
            optionalDistanceDepart().map(f -> "distanceDepart=" + f + ", ").orElse("") +
            optionalTempsArretMoyen().map(f -> "tempsArretMoyen=" + f + ", ").orElse("") +
            optionalArretPhysique().map(f -> "arretPhysique=" + f + ", ").orElse("") +
            optionalLigneId().map(f -> "ligneId=" + f + ", ").orElse("") +
            optionalArretId().map(f -> "arretId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
