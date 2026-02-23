package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.ThresholdType;

/**
 * A HistoriqueAlerte.
 */
@Entity
@Table(name = "historique_alerte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueAlerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_declenchement", nullable = false)
    private Instant dateDeclenchement;

    @Column(name = "bus_numero")
    private String busNumero;

    @Column(name = "distance_reelle")
    private Integer distanceReelle;

    @Column(name = "temps_reel")
    private Integer tempsReel;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_declenchement")
    private ThresholdType typeDeclenchement;

    @NotNull
    @Column(name = "notification_envoyee", nullable = false)
    private Boolean notificationEnvoyee;

    @Column(name = "date_lecture")
    private Instant dateLecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligne", "chauffeur" }, allowSetters = true)
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alerteLigneArrets", "historiqueAlertes", "utilisateur" }, allowSetters = true)
    private AlerteApproche alerteApproche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueAlerte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDeclenchement() {
        return this.dateDeclenchement;
    }

    public HistoriqueAlerte dateDeclenchement(Instant dateDeclenchement) {
        this.setDateDeclenchement(dateDeclenchement);
        return this;
    }

    public void setDateDeclenchement(Instant dateDeclenchement) {
        this.dateDeclenchement = dateDeclenchement;
    }

    public String getBusNumero() {
        return this.busNumero;
    }

    public HistoriqueAlerte busNumero(String busNumero) {
        this.setBusNumero(busNumero);
        return this;
    }

    public void setBusNumero(String busNumero) {
        this.busNumero = busNumero;
    }

    public Integer getDistanceReelle() {
        return this.distanceReelle;
    }

    public HistoriqueAlerte distanceReelle(Integer distanceReelle) {
        this.setDistanceReelle(distanceReelle);
        return this;
    }

    public void setDistanceReelle(Integer distanceReelle) {
        this.distanceReelle = distanceReelle;
    }

    public Integer getTempsReel() {
        return this.tempsReel;
    }

    public HistoriqueAlerte tempsReel(Integer tempsReel) {
        this.setTempsReel(tempsReel);
        return this;
    }

    public void setTempsReel(Integer tempsReel) {
        this.tempsReel = tempsReel;
    }

    public ThresholdType getTypeDeclenchement() {
        return this.typeDeclenchement;
    }

    public HistoriqueAlerte typeDeclenchement(ThresholdType typeDeclenchement) {
        this.setTypeDeclenchement(typeDeclenchement);
        return this;
    }

    public void setTypeDeclenchement(ThresholdType typeDeclenchement) {
        this.typeDeclenchement = typeDeclenchement;
    }

    public Boolean getNotificationEnvoyee() {
        return this.notificationEnvoyee;
    }

    public HistoriqueAlerte notificationEnvoyee(Boolean notificationEnvoyee) {
        this.setNotificationEnvoyee(notificationEnvoyee);
        return this;
    }

    public void setNotificationEnvoyee(Boolean notificationEnvoyee) {
        this.notificationEnvoyee = notificationEnvoyee;
    }

    public Instant getDateLecture() {
        return this.dateLecture;
    }

    public HistoriqueAlerte dateLecture(Instant dateLecture) {
        this.setDateLecture(dateLecture);
        return this;
    }

    public void setDateLecture(Instant dateLecture) {
        this.dateLecture = dateLecture;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public HistoriqueAlerte bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    public AlerteApproche getAlerteApproche() {
        return this.alerteApproche;
    }

    public void setAlerteApproche(AlerteApproche alerteApproche) {
        this.alerteApproche = alerteApproche;
    }

    public HistoriqueAlerte alerteApproche(AlerteApproche alerteApproche) {
        this.setAlerteApproche(alerteApproche);
        return this;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public HistoriqueAlerte utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueAlerte)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueAlerte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueAlerte{" +
            "id=" + getId() +
            ", dateDeclenchement='" + getDateDeclenchement() + "'" +
            ", busNumero='" + getBusNumero() + "'" +
            ", distanceReelle=" + getDistanceReelle() +
            ", tempsReel=" + getTempsReel() +
            ", typeDeclenchement='" + getTypeDeclenchement() + "'" +
            ", notificationEnvoyee='" + getNotificationEnvoyee() + "'" +
            ", dateLecture='" + getDateLecture() + "'" +
            "}";
    }
}
