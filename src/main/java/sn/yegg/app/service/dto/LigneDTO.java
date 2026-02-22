package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String couleur;

    private BigDecimal distanceKm;

    private Integer dureeMoyenne;

    @NotNull
    private String statut;

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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
            ", couleur='" + getCouleur() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", dureeMoyenne=" + getDureeMoyenne() +
            ", statut='" + getStatut() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
