package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.yegg.app.domain.Feedback} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer note;

    @Lob
    private String commentaire;

    @NotNull
    private String typeObjet;

    @NotNull
    private Long objetId;

    @NotNull
    private Instant dateCreation;

    private Boolean anonyme;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public Long getObjetId() {
        return objetId;
    }

    public void setObjetId(Long objetId) {
        this.objetId = objetId;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getAnonyme() {
        return anonyme;
    }

    public void setAnonyme(Boolean anonyme) {
        this.anonyme = anonyme;
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
        if (!(o instanceof FeedbackDTO)) {
            return false;
        }

        FeedbackDTO feedbackDTO = (FeedbackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackDTO{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", commentaire='" + getCommentaire() + "'" +
            ", typeObjet='" + getTypeObjet() + "'" +
            ", objetId=" + getObjetId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", anonyme='" + getAnonyme() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
