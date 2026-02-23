package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;

/**
 * A DTO for the {@link sn.yegg.app.domain.AlerteApproche} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteApprocheDTO implements Serializable {

    private Long id;

    private String nom;

    @Min(value = 50)
    @Max(value = 1000)
    private Integer seuilDistance;

    @Min(value = 1)
    @Max(value = 60)
    private Integer seuilTemps;

    @NotNull
    private ThresholdType typeSeuil;

    private String joursActivation;

    private String heureDebut;

    private String heureFin;

    @NotNull
    private AlertStatus statut;

    @NotNull
    private Instant dateCreation;

    private Instant dateModification;

    private Instant dernierDeclenchement;

    @Min(value = 0)
    private Integer nombreDeclenchements;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getSeuilDistance() {
        return seuilDistance;
    }

    public void setSeuilDistance(Integer seuilDistance) {
        this.seuilDistance = seuilDistance;
    }

    public Integer getSeuilTemps() {
        return seuilTemps;
    }

    public void setSeuilTemps(Integer seuilTemps) {
        this.seuilTemps = seuilTemps;
    }

    public ThresholdType getTypeSeuil() {
        return typeSeuil;
    }

    public void setTypeSeuil(ThresholdType typeSeuil) {
        this.typeSeuil = typeSeuil;
    }

    public String getJoursActivation() {
        return joursActivation;
    }

    public void setJoursActivation(String joursActivation) {
        this.joursActivation = joursActivation;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public AlertStatus getStatut() {
        return statut;
    }

    public void setStatut(AlertStatus statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public Instant getDernierDeclenchement() {
        return dernierDeclenchement;
    }

    public void setDernierDeclenchement(Instant dernierDeclenchement) {
        this.dernierDeclenchement = dernierDeclenchement;
    }

    public Integer getNombreDeclenchements() {
        return nombreDeclenchements;
    }

    public void setNombreDeclenchements(Integer nombreDeclenchements) {
        this.nombreDeclenchements = nombreDeclenchements;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlerteApprocheDTO)) {
            return false;
        }

        AlerteApprocheDTO alerteApprocheDTO = (AlerteApprocheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alerteApprocheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteApprocheDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", seuilDistance=" + getSeuilDistance() +
            ", seuilTemps=" + getSeuilTemps() +
            ", typeSeuil='" + getTypeSeuil() + "'" +
            ", joursActivation='" + getJoursActivation() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dernierDeclenchement='" + getDernierDeclenchement() + "'" +
            ", nombreDeclenchements=" + getNombreDeclenchements() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
