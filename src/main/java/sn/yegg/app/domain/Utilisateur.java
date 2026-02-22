package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule", unique = true)
    private String matricule;

    @Pattern(regexp = "^[0-9]{9,15}$")
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "notifications_push")
    private Boolean notificationsPush;

    @Column(name = "langue")
    private String langue;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "numero_permis")
    private String numeroPermis;

    @JsonIgnoreProperties(value = { "utilisateur", "ligne" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Bus bus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Utilisateur matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFcmToken() {
        return this.fcmToken;
    }

    public Utilisateur fcmToken(String fcmToken) {
        this.setFcmToken(fcmToken);
        return this;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Boolean getNotificationsPush() {
        return this.notificationsPush;
    }

    public Utilisateur notificationsPush(Boolean notificationsPush) {
        this.setNotificationsPush(notificationsPush);
        return this;
    }

    public void setNotificationsPush(Boolean notificationsPush) {
        this.notificationsPush = notificationsPush;
    }

    public String getLangue() {
        return this.langue;
    }

    public Utilisateur langue(String langue) {
        this.setLangue(langue);
        return this;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public LocalDate getDateEmbauche() {
        return this.dateEmbauche;
    }

    public Utilisateur dateEmbauche(LocalDate dateEmbauche) {
        this.setDateEmbauche(dateEmbauche);
        return this;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getNumeroPermis() {
        return this.numeroPermis;
    }

    public Utilisateur numeroPermis(String numeroPermis) {
        this.setNumeroPermis(numeroPermis);
        return this;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        if (this.bus != null) {
            this.bus.setUtilisateur(null);
        }
        if (bus != null) {
            bus.setUtilisateur(this);
        }
        this.bus = bus;
    }

    public Utilisateur bus(Bus bus) {
        this.setBus(bus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", fcmToken='" + getFcmToken() + "'" +
            ", notificationsPush='" + getNotificationsPush() + "'" +
            ", langue='" + getLangue() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", numeroPermis='" + getNumeroPermis() + "'" +
            "}";
    }
}
