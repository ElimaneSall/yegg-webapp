package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.yegg.app.domain.Favori} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private Long cibleId;

    private String nomPersonnalise;

    private Integer ordre;

    private Boolean alerteActive;

    @Min(value = 1)
    @Max(value = 60)
    private Integer alerteSeuil;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCibleId() {
        return cibleId;
    }

    public void setCibleId(Long cibleId) {
        this.cibleId = cibleId;
    }

    public String getNomPersonnalise() {
        return nomPersonnalise;
    }

    public void setNomPersonnalise(String nomPersonnalise) {
        this.nomPersonnalise = nomPersonnalise;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Boolean getAlerteActive() {
        return alerteActive;
    }

    public void setAlerteActive(Boolean alerteActive) {
        this.alerteActive = alerteActive;
    }

    public Integer getAlerteSeuil() {
        return alerteSeuil;
    }

    public void setAlerteSeuil(Integer alerteSeuil) {
        this.alerteSeuil = alerteSeuil;
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
        if (!(o instanceof FavoriDTO)) {
            return false;
        }

        FavoriDTO favoriDTO = (FavoriDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoriDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", cibleId=" + getCibleId() +
            ", nomPersonnalise='" + getNomPersonnalise() + "'" +
            ", ordre=" + getOrdre() +
            ", alerteActive='" + getAlerteActive() + "'" +
            ", alerteSeuil=" + getAlerteSeuil() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
