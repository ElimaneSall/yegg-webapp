package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.FavoriteType;

/**
 * A DTO for the {@link sn.yegg.app.domain.Favori} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriDTO implements Serializable {

    private Long id;

    @NotNull
    private FavoriteType type;

    @NotNull
    private Long cibleId;

    private String nomPersonnalise;

    private Integer ordre;

    @NotNull
    private Instant dateAjout;

    private Instant dernierAcces;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FavoriteType getType() {
        return type;
    }

    public void setType(FavoriteType type) {
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

    public Instant getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Instant dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Instant getDernierAcces() {
        return dernierAcces;
    }

    public void setDernierAcces(Instant dernierAcces) {
        this.dernierAcces = dernierAcces;
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
            ", dateAjout='" + getDateAjout() + "'" +
            ", dernierAcces='" + getDernierAcces() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
