package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.NotificationStatus;
import sn.yegg.app.domain.enumeration.NotificationType;
import sn.yegg.app.domain.enumeration.Priority;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Lob
    @Column(name = "donnees")
    private String donnees;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite")
    private Priority priorite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private NotificationStatus statut;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_envoi")
    private Instant dateEnvoi;

    @Column(name = "lu")
    private Boolean lu;

    @Column(name = "date_lecture")
    private Instant dateLecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitre() {
        return this.titre;
    }

    public Notification titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDonnees() {
        return this.donnees;
    }

    public Notification donnees(String donnees) {
        this.setDonnees(donnees);
        return this;
    }

    public void setDonnees(String donnees) {
        this.donnees = donnees;
    }

    public Priority getPriorite() {
        return this.priorite;
    }

    public Notification priorite(Priority priorite) {
        this.setPriorite(priorite);
        return this;
    }

    public void setPriorite(Priority priorite) {
        this.priorite = priorite;
    }

    public NotificationStatus getStatut() {
        return this.statut;
    }

    public Notification statut(NotificationStatus statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(NotificationStatus statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Notification dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateEnvoi() {
        return this.dateEnvoi;
    }

    public Notification dateEnvoi(Instant dateEnvoi) {
        this.setDateEnvoi(dateEnvoi);
        return this;
    }

    public void setDateEnvoi(Instant dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Boolean getLu() {
        return this.lu;
    }

    public Notification lu(Boolean lu) {
        this.setLu(lu);
        return this;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    public Instant getDateLecture() {
        return this.dateLecture;
    }

    public Notification dateLecture(Instant dateLecture) {
        this.setDateLecture(dateLecture);
        return this;
    }

    public void setDateLecture(Instant dateLecture) {
        this.dateLecture = dateLecture;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Notification utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", titre='" + getTitre() + "'" +
            ", message='" + getMessage() + "'" +
            ", donnees='" + getDonnees() + "'" +
            ", priorite='" + getPriorite() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            ", lu='" + getLu() + "'" +
            ", dateLecture='" + getDateLecture() + "'" +
            "}";
    }
}
