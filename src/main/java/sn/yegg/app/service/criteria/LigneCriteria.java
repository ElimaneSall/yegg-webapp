package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Ligne} entity. This class is used
 * in {@link sn.yegg.app.web.rest.LigneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lignes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private StringFilter nom;

    private StringFilter direction;

    private StringFilter couleur;

    private BigDecimalFilter distanceKm;

    private IntegerFilter dureeMoyenne;

    private StringFilter statut;

    private LongFilter ligneArretsId;

    private LongFilter operateurId;

    private Boolean distinct;

    public LigneCriteria() {}

    public LigneCriteria(LigneCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.direction = other.optionalDirection().map(StringFilter::copy).orElse(null);
        this.couleur = other.optionalCouleur().map(StringFilter::copy).orElse(null);
        this.distanceKm = other.optionalDistanceKm().map(BigDecimalFilter::copy).orElse(null);
        this.dureeMoyenne = other.optionalDureeMoyenne().map(IntegerFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StringFilter::copy).orElse(null);
        this.ligneArretsId = other.optionalLigneArretsId().map(LongFilter::copy).orElse(null);
        this.operateurId = other.optionalOperateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LigneCriteria copy() {
        return new LigneCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getDirection() {
        return direction;
    }

    public Optional<StringFilter> optionalDirection() {
        return Optional.ofNullable(direction);
    }

    public StringFilter direction() {
        if (direction == null) {
            setDirection(new StringFilter());
        }
        return direction;
    }

    public void setDirection(StringFilter direction) {
        this.direction = direction;
    }

    public StringFilter getCouleur() {
        return couleur;
    }

    public Optional<StringFilter> optionalCouleur() {
        return Optional.ofNullable(couleur);
    }

    public StringFilter couleur() {
        if (couleur == null) {
            setCouleur(new StringFilter());
        }
        return couleur;
    }

    public void setCouleur(StringFilter couleur) {
        this.couleur = couleur;
    }

    public BigDecimalFilter getDistanceKm() {
        return distanceKm;
    }

    public Optional<BigDecimalFilter> optionalDistanceKm() {
        return Optional.ofNullable(distanceKm);
    }

    public BigDecimalFilter distanceKm() {
        if (distanceKm == null) {
            setDistanceKm(new BigDecimalFilter());
        }
        return distanceKm;
    }

    public void setDistanceKm(BigDecimalFilter distanceKm) {
        this.distanceKm = distanceKm;
    }

    public IntegerFilter getDureeMoyenne() {
        return dureeMoyenne;
    }

    public Optional<IntegerFilter> optionalDureeMoyenne() {
        return Optional.ofNullable(dureeMoyenne);
    }

    public IntegerFilter dureeMoyenne() {
        if (dureeMoyenne == null) {
            setDureeMoyenne(new IntegerFilter());
        }
        return dureeMoyenne;
    }

    public void setDureeMoyenne(IntegerFilter dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
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

    public LongFilter getLigneArretsId() {
        return ligneArretsId;
    }

    public Optional<LongFilter> optionalLigneArretsId() {
        return Optional.ofNullable(ligneArretsId);
    }

    public LongFilter ligneArretsId() {
        if (ligneArretsId == null) {
            setLigneArretsId(new LongFilter());
        }
        return ligneArretsId;
    }

    public void setLigneArretsId(LongFilter ligneArretsId) {
        this.ligneArretsId = ligneArretsId;
    }

    public LongFilter getOperateurId() {
        return operateurId;
    }

    public Optional<LongFilter> optionalOperateurId() {
        return Optional.ofNullable(operateurId);
    }

    public LongFilter operateurId() {
        if (operateurId == null) {
            setOperateurId(new LongFilter());
        }
        return operateurId;
    }

    public void setOperateurId(LongFilter operateurId) {
        this.operateurId = operateurId;
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
        final LigneCriteria that = (LigneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(couleur, that.couleur) &&
            Objects.equals(distanceKm, that.distanceKm) &&
            Objects.equals(dureeMoyenne, that.dureeMoyenne) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(ligneArretsId, that.ligneArretsId) &&
            Objects.equals(operateurId, that.operateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, nom, direction, couleur, distanceKm, dureeMoyenne, statut, ligneArretsId, operateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDirection().map(f -> "direction=" + f + ", ").orElse("") +
            optionalCouleur().map(f -> "couleur=" + f + ", ").orElse("") +
            optionalDistanceKm().map(f -> "distanceKm=" + f + ", ").orElse("") +
            optionalDureeMoyenne().map(f -> "dureeMoyenne=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalLigneArretsId().map(f -> "ligneArretsId=" + f + ", ").orElse("") +
            optionalOperateurId().map(f -> "operateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
