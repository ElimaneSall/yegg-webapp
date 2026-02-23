package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "note", nullable = false)
    private Integer note;

    @Lob
    @Column(name = "commentaire")
    private String commentaire;

    @NotNull
    @Column(name = "type_objet", nullable = false)
    private String typeObjet;

    @NotNull
    @Column(name = "objet_id", nullable = false)
    private Long objetId;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "anonyme")
    private Boolean anonyme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return this.note;
    }

    public Feedback note(Integer note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Feedback commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getTypeObjet() {
        return this.typeObjet;
    }

    public Feedback typeObjet(String typeObjet) {
        this.setTypeObjet(typeObjet);
        return this;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public Long getObjetId() {
        return this.objetId;
    }

    public Feedback objetId(Long objetId) {
        this.setObjetId(objetId);
        return this;
    }

    public void setObjetId(Long objetId) {
        this.objetId = objetId;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Feedback dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getAnonyme() {
        return this.anonyme;
    }

    public Feedback anonyme(Boolean anonyme) {
        this.setAnonyme(anonyme);
        return this;
    }

    public void setAnonyme(Boolean anonyme) {
        this.anonyme = anonyme;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Feedback utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback)) {
            return false;
        }
        return getId() != null && getId().equals(((Feedback) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", commentaire='" + getCommentaire() + "'" +
            ", typeObjet='" + getTypeObjet() + "'" +
            ", objetId=" + getObjetId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", anonyme='" + getAnonyme() + "'" +
            "}";
    }
}
