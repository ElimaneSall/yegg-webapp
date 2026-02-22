package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.yegg.app.domain.Alerte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteDTO implements Serializable {

    private Long id;

    @NotNull
    private String typeCible;

    @NotNull
    private Long cibleId;

    @NotNull
    @Min(value = 1)
    @Max(value = 60)
    private Integer seuilMinutes;

    private String joursActivation;

    private Instant heureDebut;

    private Instant heureFin;

    @NotNull
    private String statut;

    private Instant dernierDeclenchement;

    private Integer nombreDeclenchements;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCible() {
        return typeCible;
    }

    public void setTypeCible(String typeCible) {
        this.typeCible = typeCible;
    }

    public Long getCibleId() {
        return cibleId;
    }

    public void setCibleId(Long cibleId) {
        this.cibleId = cibleId;
    }

    public Integer getSeuilMinutes() {
        return seuilMinutes;
    }

    public void setSeuilMinutes(Integer seuilMinutes) {
        this.seuilMinutes = seuilMinutes;
    }

    public String getJoursActivation() {
        return joursActivation;
    }

    public void setJoursActivation(String joursActivation) {
        this.joursActivation = joursActivation;
    }

    public Instant getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Instant heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Instant getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Instant heureFin) {
        this.heureFin = heureFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
        if (!(o instanceof AlerteDTO)) {
            return false;
        }

        AlerteDTO alerteDTO = (AlerteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alerteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteDTO{" +
            "id=" + getId() +
            ", typeCible='" + getTypeCible() + "'" +
            ", cibleId=" + getCibleId() +
            ", seuilMinutes=" + getSeuilMinutes() +
            ", joursActivation='" + getJoursActivation() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dernierDeclenchement='" + getDernierDeclenchement() + "'" +
            ", nombreDeclenchements=" + getNombreDeclenchements() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
