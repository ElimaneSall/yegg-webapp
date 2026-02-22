package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Alerte} entity. This class is used
 * in {@link sn.yegg.app.web.rest.AlerteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alertes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter typeCible;

    private LongFilter cibleId;

    private IntegerFilter seuilMinutes;

    private StringFilter joursActivation;

    private InstantFilter heureDebut;

    private InstantFilter heureFin;

    private StringFilter statut;

    private InstantFilter dernierDeclenchement;

    private IntegerFilter nombreDeclenchements;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public AlerteCriteria() {}

    public AlerteCriteria(AlerteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.typeCible = other.optionalTypeCible().map(StringFilter::copy).orElse(null);
        this.cibleId = other.optionalCibleId().map(LongFilter::copy).orElse(null);
        this.seuilMinutes = other.optionalSeuilMinutes().map(IntegerFilter::copy).orElse(null);
        this.joursActivation = other.optionalJoursActivation().map(StringFilter::copy).orElse(null);
        this.heureDebut = other.optionalHeureDebut().map(InstantFilter::copy).orElse(null);
        this.heureFin = other.optionalHeureFin().map(InstantFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StringFilter::copy).orElse(null);
        this.dernierDeclenchement = other.optionalDernierDeclenchement().map(InstantFilter::copy).orElse(null);
        this.nombreDeclenchements = other.optionalNombreDeclenchements().map(IntegerFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlerteCriteria copy() {
        return new AlerteCriteria(this);
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

    public StringFilter getTypeCible() {
        return typeCible;
    }

    public Optional<StringFilter> optionalTypeCible() {
        return Optional.ofNullable(typeCible);
    }

    public StringFilter typeCible() {
        if (typeCible == null) {
            setTypeCible(new StringFilter());
        }
        return typeCible;
    }

    public void setTypeCible(StringFilter typeCible) {
        this.typeCible = typeCible;
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

    public IntegerFilter getSeuilMinutes() {
        return seuilMinutes;
    }

    public Optional<IntegerFilter> optionalSeuilMinutes() {
        return Optional.ofNullable(seuilMinutes);
    }

    public IntegerFilter seuilMinutes() {
        if (seuilMinutes == null) {
            setSeuilMinutes(new IntegerFilter());
        }
        return seuilMinutes;
    }

    public void setSeuilMinutes(IntegerFilter seuilMinutes) {
        this.seuilMinutes = seuilMinutes;
    }

    public StringFilter getJoursActivation() {
        return joursActivation;
    }

    public Optional<StringFilter> optionalJoursActivation() {
        return Optional.ofNullable(joursActivation);
    }

    public StringFilter joursActivation() {
        if (joursActivation == null) {
            setJoursActivation(new StringFilter());
        }
        return joursActivation;
    }

    public void setJoursActivation(StringFilter joursActivation) {
        this.joursActivation = joursActivation;
    }

    public InstantFilter getHeureDebut() {
        return heureDebut;
    }

    public Optional<InstantFilter> optionalHeureDebut() {
        return Optional.ofNullable(heureDebut);
    }

    public InstantFilter heureDebut() {
        if (heureDebut == null) {
            setHeureDebut(new InstantFilter());
        }
        return heureDebut;
    }

    public void setHeureDebut(InstantFilter heureDebut) {
        this.heureDebut = heureDebut;
    }

    public InstantFilter getHeureFin() {
        return heureFin;
    }

    public Optional<InstantFilter> optionalHeureFin() {
        return Optional.ofNullable(heureFin);
    }

    public InstantFilter heureFin() {
        if (heureFin == null) {
            setHeureFin(new InstantFilter());
        }
        return heureFin;
    }

    public void setHeureFin(InstantFilter heureFin) {
        this.heureFin = heureFin;
    }

    public StringFilter getStatut() {
        return statut;
    }

    public Optional<StringFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StringFilter statut() {
        if (statut == null) {
            setStatut(new StringFilter());
        }
        return statut;
    }

    public void setStatut(StringFilter statut) {
        this.statut = statut;
    }

    public InstantFilter getDernierDeclenchement() {
        return dernierDeclenchement;
    }

    public Optional<InstantFilter> optionalDernierDeclenchement() {
        return Optional.ofNullable(dernierDeclenchement);
    }

    public InstantFilter dernierDeclenchement() {
        if (dernierDeclenchement == null) {
            setDernierDeclenchement(new InstantFilter());
        }
        return dernierDeclenchement;
    }

    public void setDernierDeclenchement(InstantFilter dernierDeclenchement) {
        this.dernierDeclenchement = dernierDeclenchement;
    }

    public IntegerFilter getNombreDeclenchements() {
        return nombreDeclenchements;
    }

    public Optional<IntegerFilter> optionalNombreDeclenchements() {
        return Optional.ofNullable(nombreDeclenchements);
    }

    public IntegerFilter nombreDeclenchements() {
        if (nombreDeclenchements == null) {
            setNombreDeclenchements(new IntegerFilter());
        }
        return nombreDeclenchements;
    }

    public void setNombreDeclenchements(IntegerFilter nombreDeclenchements) {
        this.nombreDeclenchements = nombreDeclenchements;
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
        final AlerteCriteria that = (AlerteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeCible, that.typeCible) &&
            Objects.equals(cibleId, that.cibleId) &&
            Objects.equals(seuilMinutes, that.seuilMinutes) &&
            Objects.equals(joursActivation, that.joursActivation) &&
            Objects.equals(heureDebut, that.heureDebut) &&
            Objects.equals(heureFin, that.heureFin) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dernierDeclenchement, that.dernierDeclenchement) &&
            Objects.equals(nombreDeclenchements, that.nombreDeclenchements) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            typeCible,
            cibleId,
            seuilMinutes,
            joursActivation,
            heureDebut,
            heureFin,
            statut,
            dernierDeclenchement,
            nombreDeclenchements,
            utilisateurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTypeCible().map(f -> "typeCible=" + f + ", ").orElse("") +
            optionalCibleId().map(f -> "cibleId=" + f + ", ").orElse("") +
            optionalSeuilMinutes().map(f -> "seuilMinutes=" + f + ", ").orElse("") +
            optionalJoursActivation().map(f -> "joursActivation=" + f + ", ").orElse("") +
            optionalHeureDebut().map(f -> "heureDebut=" + f + ", ").orElse("") +
            optionalHeureFin().map(f -> "heureFin=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDernierDeclenchement().map(f -> "dernierDeclenchement=" + f + ", ").orElse("") +
            optionalNombreDeclenchements().map(f -> "nombreDeclenchements=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
