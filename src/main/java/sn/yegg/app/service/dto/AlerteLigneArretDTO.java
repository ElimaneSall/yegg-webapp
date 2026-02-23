package sn.yegg.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.yegg.app.domain.AlerteLigneArret} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteLigneArretDTO implements Serializable {

    private Long id;

    private String sens;

    private Boolean actif;

    private LigneDTO ligne;

    private ArretDTO arret;

    private AlerteApprocheDTO alerteApproche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public AlerteApprocheDTO getAlerteApproche() {
        return alerteApproche;
    }

    public void setAlerteApproche(AlerteApprocheDTO alerteApproche) {
        this.alerteApproche = alerteApproche;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlerteLigneArretDTO)) {
            return false;
        }

        AlerteLigneArretDTO alerteLigneArretDTO = (AlerteLigneArretDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alerteLigneArretDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteLigneArretDTO{" +
            "id=" + getId() +
            ", sens='" + getSens() + "'" +
            ", actif='" + getActif() + "'" +
            ", ligne=" + getLigne() +
            ", arret=" + getArret() +
            ", alerteApproche=" + getAlerteApproche() +
            "}";
    }
}
