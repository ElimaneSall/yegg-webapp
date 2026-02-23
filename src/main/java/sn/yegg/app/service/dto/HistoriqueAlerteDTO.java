package sn.yegg.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.ThresholdType;

/**
 * A DTO for the {@link sn.yegg.app.domain.HistoriqueAlerte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueAlerteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateDeclenchement;

    private String busNumero;

    private Integer distanceReelle;

    private Integer tempsReel;

    private ThresholdType typeDeclenchement;

    @NotNull
    private Boolean notificationEnvoyee;

    private Instant dateLecture;

    private BusDTO bus;

    private AlerteApprocheDTO alerteApproche;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDeclenchement() {
        return dateDeclenchement;
    }

    public void setDateDeclenchement(Instant dateDeclenchement) {
        this.dateDeclenchement = dateDeclenchement;
    }

    public String getBusNumero() {
        return busNumero;
    }

    public void setBusNumero(String busNumero) {
        this.busNumero = busNumero;
    }

    public Integer getDistanceReelle() {
        return distanceReelle;
    }

    public void setDistanceReelle(Integer distanceReelle) {
        this.distanceReelle = distanceReelle;
    }

    public Integer getTempsReel() {
        return tempsReel;
    }

    public void setTempsReel(Integer tempsReel) {
        this.tempsReel = tempsReel;
    }

    public ThresholdType getTypeDeclenchement() {
        return typeDeclenchement;
    }

    public void setTypeDeclenchement(ThresholdType typeDeclenchement) {
        this.typeDeclenchement = typeDeclenchement;
    }

    public Boolean getNotificationEnvoyee() {
        return notificationEnvoyee;
    }

    public void setNotificationEnvoyee(Boolean notificationEnvoyee) {
        this.notificationEnvoyee = notificationEnvoyee;
    }

    public Instant getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Instant dateLecture) {
        this.dateLecture = dateLecture;
    }

    public BusDTO getBus() {
        return bus;
    }

    public void setBus(BusDTO bus) {
        this.bus = bus;
    }

    public AlerteApprocheDTO getAlerteApproche() {
        return alerteApproche;
    }

    public void setAlerteApproche(AlerteApprocheDTO alerteApproche) {
        this.alerteApproche = alerteApproche;
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
        if (!(o instanceof HistoriqueAlerteDTO)) {
            return false;
        }

        HistoriqueAlerteDTO historiqueAlerteDTO = (HistoriqueAlerteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueAlerteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueAlerteDTO{" +
            "id=" + getId() +
            ", dateDeclenchement='" + getDateDeclenchement() + "'" +
            ", busNumero='" + getBusNumero() + "'" +
            ", distanceReelle=" + getDistanceReelle() +
            ", tempsReel=" + getTempsReel() +
            ", typeDeclenchement='" + getTypeDeclenchement() + "'" +
            ", notificationEnvoyee='" + getNotificationEnvoyee() + "'" +
            ", dateLecture='" + getDateLecture() + "'" +
            ", bus=" + getBus() +
            ", alerteApproche=" + getAlerteApproche() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
