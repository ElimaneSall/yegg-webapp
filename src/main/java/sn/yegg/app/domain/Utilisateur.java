package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.UserRole;

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

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "matricule", unique = true)
    private String matricule;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "notifications_push")
    private Boolean notificationsPush;

    @Column(name = "notifications_sms")
    private Boolean notificationsSms;

    @Column(name = "langue")
    private String langue;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "derniere_connexion")
    private Instant derniereConnexion;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "numero_permis", unique = true)
    private String numeroPermis;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Set<Favori> favorises = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bus", "alerteApproche", "utilisateur" }, allowSetters = true)
    private Set<HistoriqueAlerte> historiqueAlertes = new HashSet<>();

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

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public UserRole getRole() {
        return this.role;
    }

    public Utilisateur role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
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

    public Boolean getNotificationsSms() {
        return this.notificationsSms;
    }

    public Utilisateur notificationsSms(Boolean notificationsSms) {
        this.setNotificationsSms(notificationsSms);
        return this;
    }

    public void setNotificationsSms(Boolean notificationsSms) {
        this.notificationsSms = notificationsSms;
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

    public byte[] getPhoto() {
        return this.photo;
    }

    public Utilisateur photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Utilisateur photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Utilisateur dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDerniereConnexion() {
        return this.derniereConnexion;
    }

    public Utilisateur derniereConnexion(Instant derniereConnexion) {
        this.setDerniereConnexion(derniereConnexion);
        return this;
    }

    public void setDerniereConnexion(Instant derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Utilisateur actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public Set<Favori> getFavorises() {
        return this.favorises;
    }

    public void setFavorises(Set<Favori> favoris) {
        if (this.favorises != null) {
            this.favorises.forEach(i -> i.setUtilisateur(null));
        }
        if (favoris != null) {
            favoris.forEach(i -> i.setUtilisateur(this));
        }
        this.favorises = favoris;
    }

    public Utilisateur favorises(Set<Favori> favoris) {
        this.setFavorises(favoris);
        return this;
    }

    public Utilisateur addFavoris(Favori favori) {
        this.favorises.add(favori);
        favori.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeFavoris(Favori favori) {
        this.favorises.remove(favori);
        favori.setUtilisateur(null);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setUtilisateur(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setUtilisateur(this));
        }
        this.notifications = notifications;
    }

    public Utilisateur notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public Utilisateur addNotifications(Notification notification) {
        this.notifications.add(notification);
        notification.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeNotifications(Notification notification) {
        this.notifications.remove(notification);
        notification.setUtilisateur(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setUtilisateur(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setUtilisateur(this));
        }
        this.feedbacks = feedbacks;
    }

    public Utilisateur feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Utilisateur addFeedbacks(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeFeedbacks(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setUtilisateur(null);
        return this;
    }

    public Set<HistoriqueAlerte> getHistoriqueAlertes() {
        return this.historiqueAlertes;
    }

    public void setHistoriqueAlertes(Set<HistoriqueAlerte> historiqueAlertes) {
        if (this.historiqueAlertes != null) {
            this.historiqueAlertes.forEach(i -> i.setUtilisateur(null));
        }
        if (historiqueAlertes != null) {
            historiqueAlertes.forEach(i -> i.setUtilisateur(this));
        }
        this.historiqueAlertes = historiqueAlertes;
    }

    public Utilisateur historiqueAlertes(Set<HistoriqueAlerte> historiqueAlertes) {
        this.setHistoriqueAlertes(historiqueAlertes);
        return this;
    }

    public Utilisateur addHistoriqueAlertes(HistoriqueAlerte historiqueAlerte) {
        this.historiqueAlertes.add(historiqueAlerte);
        historiqueAlerte.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeHistoriqueAlertes(HistoriqueAlerte historiqueAlerte) {
        this.historiqueAlertes.remove(historiqueAlerte);
        historiqueAlerte.setUtilisateur(null);
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
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", role='" + getRole() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", fcmToken='" + getFcmToken() + "'" +
            ", notificationsPush='" + getNotificationsPush() + "'" +
            ", notificationsSms='" + getNotificationsSms() + "'" +
            ", langue='" + getLangue() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", derniereConnexion='" + getDerniereConnexion() + "'" +
            ", actif='" + getActif() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", numeroPermis='" + getNumeroPermis() + "'" +
            "}";
    }
}
