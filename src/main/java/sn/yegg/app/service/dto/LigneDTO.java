package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.LineStatus;

/**
 * A DTO for the {@link sn.yegg.app.domain.Ligne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    private String numero;

    @NotNull
    private String nom;

    @NotNull
    private String direction;

    @Lob
    private String description;

    private String couleur;

    @DecimalMin(value = "0")
    private BigDecimal distanceKm;

    @Min(value = 0)
    private Integer dureeMoyenne;

    @Min(value = 1)
    @Max(value = 60)
    private Integer frequence;

    @NotNull
    private LineStatus statut;

    @Lob
    private String joursFeries;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private Boolean actif;

    private OperateurDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getDureeMoyenne() {
        return dureeMoyenne;
    }

    public void setDureeMoyenne(Integer dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }

    public LineStatus getStatut() {
        return statut;
    }

    public void setStatut(LineStatus statut) {
        this.statut = statut;
    }

    public String getJoursFeries() {
        return joursFeries;
    }

    public void setJoursFeries(String joursFeries) {
        this.joursFeries = joursFeries;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public OperateurDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurDTO operateur) {
        this.operateur = operateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneDTO)) {
            return false;
        }

        LigneDTO ligneDTO = (LigneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", nom='" + getNom() + "'" +
            ", direction='" + getDirection() + "'" +
            ", description='" + getDescription() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", dureeMoyenne=" + getDureeMoyenne() +
            ", frequence=" + getFrequence() +
            ", statut='" + getStatut() + "'" +
            ", joursFeries='" + getJoursFeries() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", actif='" + getActif() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
