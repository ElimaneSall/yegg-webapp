package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.UserRole;

/**
 * A DTO for the {@link sn.yegg.app.domain.Utilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurDTO implements Serializable {

    private Long id;

    private String prenom;

    private String nom;

    private String email;

    private String telephone;

    private String motDePasse;

    @NotNull
    private UserRole role;

    private String matricule;

    private String fcmToken;

    private Boolean notificationsPush;

    private Boolean notificationsSms;

    private String langue;

    @Lob
    private byte[] photo;

    private String photoContentType;

    @NotNull
    private Instant dateCreation;

    private Instant derniereConnexion;

    @NotNull
    private Boolean actif;

    private LocalDate dateEmbauche;

    private String numeroPermis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Boolean getNotificationsPush() {
        return notificationsPush;
    }

    public void setNotificationsPush(Boolean notificationsPush) {
        this.notificationsPush = notificationsPush;
    }

    public Boolean getNotificationsSms() {
        return notificationsSms;
    }

    public void setNotificationsSms(Boolean notificationsSms) {
        this.notificationsSms = notificationsSms;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(Instant derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurDTO)) {
            return false;
        }

        UtilisateurDTO utilisateurDTO = (UtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurDTO{" +
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
            ", dateCreation='" + getDateCreation() + "'" +
            ", derniereConnexion='" + getDerniereConnexion() + "'" +
            ", actif='" + getActif() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", numeroPermis='" + getNumeroPermis() + "'" +
            "}";
    }
}
