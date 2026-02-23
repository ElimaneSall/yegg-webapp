package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.AlerteLigneArret} entity. This class is used
 * in {@link sn.yegg.app.web.rest.AlerteLigneArretResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alerte-ligne-arrets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteLigneArretCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sens;

    private BooleanFilter actif;

    private LongFilter ligneId;

    private LongFilter arretId;

    private LongFilter alerteApprocheId;

    private Boolean distinct;

    public AlerteLigneArretCriteria() {}

    public AlerteLigneArretCriteria(AlerteLigneArretCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sens = other.optionalSens().map(StringFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.ligneId = other.optionalLigneId().map(LongFilter::copy).orElse(null);
        this.arretId = other.optionalArretId().map(LongFilter::copy).orElse(null);
        this.alerteApprocheId = other.optionalAlerteApprocheId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlerteLigneArretCriteria copy() {
        return new AlerteLigneArretCriteria(this);
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

    public StringFilter getSens() {
        return sens;
    }

    public Optional<StringFilter> optionalSens() {
        return Optional.ofNullable(sens);
    }

    public StringFilter sens() {
        if (sens == null) {
            setSens(new StringFilter());
        }
        return sens;
    }

    public void setSens(StringFilter sens) {
        this.sens = sens;
    }

    public BooleanFilter getActif() {
        return actif;
    }

    public Optional<BooleanFilter> optionalActif() {
        return Optional.ofNullable(actif);
    }

    public BooleanFilter actif() {
        if (actif == null) {
            setActif(new BooleanFilter());
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
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
        final AlerteLigneArretCriteria that = (AlerteLigneArretCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sens, that.sens) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(ligneId, that.ligneId) &&
            Objects.equals(arretId, that.arretId) &&
            Objects.equals(alerteApprocheId, that.alerteApprocheId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sens, actif, ligneId, arretId, alerteApprocheId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteLigneArretCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSens().map(f -> "sens=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalLigneId().map(f -> "ligneId=" + f + ", ").orElse("") +
            optionalArretId().map(f -> "arretId=" + f + ", ").orElse("") +
            optionalAlerteApprocheId().map(f -> "alerteApprocheId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
