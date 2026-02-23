package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link sn.yegg.app.domain.LigneArret} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneArretDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer ordre;

    private Integer tempsTrajetDepart;

    private BigDecimal distanceDepart;

    private Integer tempsArretMoyen;

    private Boolean arretPhysique;

    private LigneDTO ligne;

    private ArretDTO arret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Integer getTempsTrajetDepart() {
        return tempsTrajetDepart;
    }

    public void setTempsTrajetDepart(Integer tempsTrajetDepart) {
        this.tempsTrajetDepart = tempsTrajetDepart;
    }

    public BigDecimal getDistanceDepart() {
        return distanceDepart;
    }

    public void setDistanceDepart(BigDecimal distanceDepart) {
        this.distanceDepart = distanceDepart;
    }

    public Integer getTempsArretMoyen() {
        return tempsArretMoyen;
    }

    public void setTempsArretMoyen(Integer tempsArretMoyen) {
        this.tempsArretMoyen = tempsArretMoyen;
    }

    public Boolean getArretPhysique() {
        return arretPhysique;
    }

    public void setArretPhysique(Boolean arretPhysique) {
        this.arretPhysique = arretPhysique;
    }

    public LigneDTO getLigne() {
        return ligne;
    }

    public void setLigne(LigneDTO ligne) {
        this.ligne = ligne;
    }

    public ArretDTO getArret() {
        return arret;
    }

    public void setArret(ArretDTO arret) {
        this.arret = arret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneArretDTO)) {
            return false;
        }

        LigneArretDTO ligneArretDTO = (LigneArretDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneArretDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneArretDTO{" +
            "id=" + getId() +
            ", ordre=" + getOrdre() +
            ", tempsTrajetDepart=" + getTempsTrajetDepart() +
            ", distanceDepart=" + getDistanceDepart() +
            ", tempsArretMoyen=" + getTempsArretMoyen() +
            ", arretPhysique='" + getArretPhysique() + "'" +
            ", ligne=" + getLigne() +
            ", arret=" + getArret() +
            "}";
    }
}
