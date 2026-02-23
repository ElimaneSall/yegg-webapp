package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Operateur} entity. This class is used
 * in {@link sn.yegg.app.web.rest.OperateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /operateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperateurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter email;

    private StringFilter telephone;

    private StringFilter siteWeb;

    private StringFilter siret;

    private InstantFilter dateCreation;

    private BooleanFilter actif;

    private LongFilter lignesId;

    private Boolean distinct;

    public OperateurCriteria() {}

    public OperateurCriteria(OperateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.siteWeb = other.optionalSiteWeb().map(StringFilter::copy).orElse(null);
        this.siret = other.optionalSiret().map(StringFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.lignesId = other.optionalLignesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OperateurCriteria copy() {
        return new OperateurCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public Optional<StringFilter> optionalTelephone() {
        return Optional.ofNullable(telephone);
    }

    public StringFilter telephone() {
        if (telephone == null) {
            setTelephone(new StringFilter());
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getSiteWeb() {
        return siteWeb;
    }

    public Optional<StringFilter> optionalSiteWeb() {
        return Optional.ofNullable(siteWeb);
    }

    public StringFilter siteWeb() {
        if (siteWeb == null) {
            setSiteWeb(new StringFilter());
        }
        return siteWeb;
    }

    public void setSiteWeb(StringFilter siteWeb) {
        this.siteWeb = siteWeb;
    }

    public StringFilter getSiret() {
        return siret;
    }

    public Optional<StringFilter> optionalSiret() {
        return Optional.ofNullable(siret);
    }

    public StringFilter siret() {
        if (siret == null) {
            setSiret(new StringFilter());
        }
        return siret;
    }

    public void setSiret(StringFilter siret) {
        this.siret = siret;
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

    public LongFilter getLignesId() {
        return lignesId;
    }

    public Optional<LongFilter> optionalLignesId() {
        return Optional.ofNullable(lignesId);
    }

    public LongFilter lignesId() {
        if (lignesId == null) {
            setLignesId(new LongFilter());
        }
        return lignesId;
    }

    public void setLignesId(LongFilter lignesId) {
        this.lignesId = lignesId;
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
        final OperateurCriteria that = (OperateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(siteWeb, that.siteWeb) &&
            Objects.equals(siret, that.siret) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(lignesId, that.lignesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, email, telephone, siteWeb, siret, dateCreation, actif, lignesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalSiteWeb().map(f -> "siteWeb=" + f + ", ").orElse("") +
            optionalSiret().map(f -> "siret=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalLignesId().map(f -> "lignesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
