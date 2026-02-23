package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.AlerteApproche} entity. This class is used
 * in {@link sn.yegg.app.web.rest.AlerteApprocheResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alerte-approches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteApprocheCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering AlertStatus
     */
    public static class AlertStatusFilter extends Filter<AlertStatus> {

        public AlertStatusFilter() {}

        public AlertStatusFilter(AlertStatusFilter filter) {
            super(filter);
        }

        @Override
        public AlertStatusFilter copy() {
            return new AlertStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private IntegerFilter seuilDistance;

    private IntegerFilter seuilTemps;

    private ThresholdTypeFilter typeSeuil;

    private StringFilter joursActivation;

    private StringFilter heureDebut;

    private StringFilter heureFin;

    private AlertStatusFilter statut;

    private InstantFilter dateCreation;

    private InstantFilter dateModification;

    private InstantFilter dernierDeclenchement;

    private IntegerFilter nombreDeclenchements;

    private LongFilter alerteLigneArretId;

    private LongFilter historiqueAlertesId;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public AlerteApprocheCriteria() {}

    public AlerteApprocheCriteria(AlerteApprocheCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.seuilDistance = other.optionalSeuilDistance().map(IntegerFilter::copy).orElse(null);
        this.seuilTemps = other.optionalSeuilTemps().map(IntegerFilter::copy).orElse(null);
        this.typeSeuil = other.optionalTypeSeuil().map(ThresholdTypeFilter::copy).orElse(null);
        this.joursActivation = other.optionalJoursActivation().map(StringFilter::copy).orElse(null);
        this.heureDebut = other.optionalHeureDebut().map(StringFilter::copy).orElse(null);
        this.heureFin = other.optionalHeureFin().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(AlertStatusFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.dateModification = other.optionalDateModification().map(InstantFilter::copy).orElse(null);
        this.dernierDeclenchement = other.optionalDernierDeclenchement().map(InstantFilter::copy).orElse(null);
        this.nombreDeclenchements = other.optionalNombreDeclenchements().map(IntegerFilter::copy).orElse(null);
        this.alerteLigneArretId = other.optionalAlerteLigneArretId().map(LongFilter::copy).orElse(null);
        this.historiqueAlertesId = other.optionalHistoriqueAlertesId().map(LongFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlerteApprocheCriteria copy() {
        return new AlerteApprocheCriteria(this);
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

    public IntegerFilter getSeuilDistance() {
        return seuilDistance;
    }

    public Optional<IntegerFilter> optionalSeuilDistance() {
        return Optional.ofNullable(seuilDistance);
    }

    public IntegerFilter seuilDistance() {
        if (seuilDistance == null) {
            setSeuilDistance(new IntegerFilter());
        }
        return seuilDistance;
    }

    public void setSeuilDistance(IntegerFilter seuilDistance) {
        this.seuilDistance = seuilDistance;
    }

    public IntegerFilter getSeuilTemps() {
        return seuilTemps;
    }

    public Optional<IntegerFilter> optionalSeuilTemps() {
        return Optional.ofNullable(seuilTemps);
    }

    public IntegerFilter seuilTemps() {
        if (seuilTemps == null) {
            setSeuilTemps(new IntegerFilter());
        }
        return seuilTemps;
    }

    public void setSeuilTemps(IntegerFilter seuilTemps) {
        this.seuilTemps = seuilTemps;
    }

    public ThresholdTypeFilter getTypeSeuil() {
        return typeSeuil;
    }

    public Optional<ThresholdTypeFilter> optionalTypeSeuil() {
        return Optional.ofNullable(typeSeuil);
    }

    public ThresholdTypeFilter typeSeuil() {
        if (typeSeuil == null) {
            setTypeSeuil(new ThresholdTypeFilter());
        }
        return typeSeuil;
    }

    public void setTypeSeuil(ThresholdTypeFilter typeSeuil) {
        this.typeSeuil = typeSeuil;
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

    public StringFilter getHeureDebut() {
        return heureDebut;
    }

    public Optional<StringFilter> optionalHeureDebut() {
        return Optional.ofNullable(heureDebut);
    }

    public StringFilter heureDebut() {
        if (heureDebut == null) {
            setHeureDebut(new StringFilter());
        }
        return heureDebut;
    }

    public void setHeureDebut(StringFilter heureDebut) {
        this.heureDebut = heureDebut;
    }

    public StringFilter getHeureFin() {
        return heureFin;
    }

    public Optional<StringFilter> optionalHeureFin() {
        return Optional.ofNullable(heureFin);
    }

    public StringFilter heureFin() {
        if (heureFin == null) {
            setHeureFin(new StringFilter());
        }
        return heureFin;
    }

    public void setHeureFin(StringFilter heureFin) {
        this.heureFin = heureFin;
    }

    public AlertStatusFilter getStatut() {
        return statut;
    }

    public Optional<AlertStatusFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public AlertStatusFilter statut() {
        if (statut == null) {
            setStatut(new AlertStatusFilter());
        }
        return statut;
    }

    public void setStatut(AlertStatusFilter statut) {
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

    public InstantFilter getDateModification() {
        return dateModification;
    }

    public Optional<InstantFilter> optionalDateModification() {
        return Optional.ofNullable(dateModification);
    }

    public InstantFilter dateModification() {
        if (dateModification == null) {
            setDateModification(new InstantFilter());
        }
        return dateModification;
    }

    public void setDateModification(InstantFilter dateModification) {
        this.dateModification = dateModification;
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

    public LongFilter getAlerteLigneArretId() {
        return alerteLigneArretId;
    }

    public Optional<LongFilter> optionalAlerteLigneArretId() {
        return Optional.ofNullable(alerteLigneArretId);
    }

    public LongFilter alerteLigneArretId() {
        if (alerteLigneArretId == null) {
            setAlerteLigneArretId(new LongFilter());
        }
        return alerteLigneArretId;
    }

    public void setAlerteLigneArretId(LongFilter alerteLigneArretId) {
        this.alerteLigneArretId = alerteLigneArretId;
    }

    public LongFilter getHistoriqueAlertesId() {
        return historiqueAlertesId;
    }

    public Optional<LongFilter> optionalHistoriqueAlertesId() {
        return Optional.ofNullable(historiqueAlertesId);
    }

    public LongFilter historiqueAlertesId() {
        if (historiqueAlertesId == null) {
            setHistoriqueAlertesId(new LongFilter());
        }
        return historiqueAlertesId;
    }

    public void setHistoriqueAlertesId(LongFilter historiqueAlertesId) {
        this.historiqueAlertesId = historiqueAlertesId;
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
        final AlerteApprocheCriteria that = (AlerteApprocheCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(seuilDistance, that.seuilDistance) &&
            Objects.equals(seuilTemps, that.seuilTemps) &&
            Objects.equals(typeSeuil, that.typeSeuil) &&
            Objects.equals(joursActivation, that.joursActivation) &&
            Objects.equals(heureDebut, that.heureDebut) &&
            Objects.equals(heureFin, that.heureFin) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateModification, that.dateModification) &&
            Objects.equals(dernierDeclenchement, that.dernierDeclenchement) &&
            Objects.equals(nombreDeclenchements, that.nombreDeclenchements) &&
            Objects.equals(alerteLigneArretId, that.alerteLigneArretId) &&
            Objects.equals(historiqueAlertesId, that.historiqueAlertesId) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nom,
            seuilDistance,
            seuilTemps,
            typeSeuil,
            joursActivation,
            heureDebut,
            heureFin,
            statut,
            dateCreation,
            dateModification,
            dernierDeclenchement,
            nombreDeclenchements,
            alerteLigneArretId,
            historiqueAlertesId,
            utilisateurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteApprocheCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalSeuilDistance().map(f -> "seuilDistance=" + f + ", ").orElse("") +
            optionalSeuilTemps().map(f -> "seuilTemps=" + f + ", ").orElse("") +
            optionalTypeSeuil().map(f -> "typeSeuil=" + f + ", ").orElse("") +
            optionalJoursActivation().map(f -> "joursActivation=" + f + ", ").orElse("") +
            optionalHeureDebut().map(f -> "heureDebut=" + f + ", ").orElse("") +
            optionalHeureFin().map(f -> "heureFin=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDateModification().map(f -> "dateModification=" + f + ", ").orElse("") +
            optionalDernierDeclenchement().map(f -> "dernierDeclenchement=" + f + ", ").orElse("") +
            optionalNombreDeclenchements().map(f -> "nombreDeclenchements=" + f + ", ").orElse("") +
            optionalAlerteLigneArretId().map(f -> "alerteLigneArretId=" + f + ", ").orElse("") +
            optionalHistoriqueAlertesId().map(f -> "historiqueAlertesId=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
