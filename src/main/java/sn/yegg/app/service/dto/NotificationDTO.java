package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.NotificationStatus;
import sn.yegg.app.domain.enumeration.NotificationType;
import sn.yegg.app.domain.enumeration.Priority;

/**
 * A DTO for the {@link sn.yegg.app.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private NotificationType type;

    @NotNull
    private String titre;

    @Lob
    private String message;

    @Lob
    private String donnees;

    private Priority priorite;

    @NotNull
    private NotificationStatus statut;

    @NotNull
    private Instant dateCreation;

    private Instant dateEnvoi;

    private Boolean lu;

    private Instant dateLecture;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDonnees() {
        return donnees;
    }

    public void setDonnees(String donnees) {
        this.donnees = donnees;
    }

    public Priority getPriorite() {
        return priorite;
    }

    public void setPriorite(Priority priorite) {
        this.priorite = priorite;
    }

    public NotificationStatus getStatut() {
        return statut;
    }

    public void setStatut(NotificationStatus statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Instant dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Boolean getLu() {
        return lu;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    public Instant getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Instant dateLecture) {
        this.dateLecture = dateLecture;
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
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
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
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
