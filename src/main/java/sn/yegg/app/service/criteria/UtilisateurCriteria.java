package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.UserRole;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Utilisateur} entity. This class is used
 * in {@link sn.yegg.app.web.rest.UtilisateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /utilisateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserRole
     */
    public static class UserRoleFilter extends Filter<UserRole> {

        public UserRoleFilter() {}

        public UserRoleFilter(UserRoleFilter filter) {
            super(filter);
        }

        @Override
        public UserRoleFilter copy() {
            return new UserRoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prenom;

    private StringFilter nom;

    private StringFilter email;

    private StringFilter telephone;

    private StringFilter motDePasse;

    private UserRoleFilter role;

    private StringFilter matricule;

    private StringFilter fcmToken;

    private BooleanFilter notificationsPush;

    private BooleanFilter notificationsSms;

    private StringFilter langue;

    private InstantFilter dateCreation;

    private InstantFilter derniereConnexion;

    private BooleanFilter actif;

    private LocalDateFilter dateEmbauche;

    private StringFilter numeroPermis;

    private LongFilter favorisId;

    private LongFilter notificationsId;

    private LongFilter feedbacksId;

    private LongFilter historiqueAlertesId;

    private Boolean distinct;

    public UtilisateurCriteria() {}

    public UtilisateurCriteria(UtilisateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.motDePasse = other.optionalMotDePasse().map(StringFilter::copy).orElse(null);
        this.role = other.optionalRole().map(UserRoleFilter::copy).orElse(null);
        this.matricule = other.optionalMatricule().map(StringFilter::copy).orElse(null);
        this.fcmToken = other.optionalFcmToken().map(StringFilter::copy).orElse(null);
        this.notificationsPush = other.optionalNotificationsPush().map(BooleanFilter::copy).orElse(null);
        this.notificationsSms = other.optionalNotificationsSms().map(BooleanFilter::copy).orElse(null);
        this.langue = other.optionalLangue().map(StringFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.derniereConnexion = other.optionalDerniereConnexion().map(InstantFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.dateEmbauche = other.optionalDateEmbauche().map(LocalDateFilter::copy).orElse(null);
        this.numeroPermis = other.optionalNumeroPermis().map(StringFilter::copy).orElse(null);
        this.favorisId = other.optionalFavorisId().map(LongFilter::copy).orElse(null);
        this.notificationsId = other.optionalNotificationsId().map(LongFilter::copy).orElse(null);
        this.feedbacksId = other.optionalFeedbacksId().map(LongFilter::copy).orElse(null);
        this.historiqueAlertesId = other.optionalHistoriqueAlertesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UtilisateurCriteria copy() {
        return new UtilisateurCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public Optional<StringFilter> optionalTelephone() {
        return Optional.ofNullable(telephone);
    }

    public StringFilter telephone() {
        if (telephone == null) {
            setTelephone(new StringFilter());
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getMotDePasse() {
        return motDePasse;
    }

    public Optional<StringFilter> optionalMotDePasse() {
        return Optional.ofNullable(motDePasse);
    }

    public StringFilter motDePasse() {
        if (motDePasse == null) {
            setMotDePasse(new StringFilter());
        }
        return motDePasse;
    }

    public void setMotDePasse(StringFilter motDePasse) {
        this.motDePasse = motDePasse;
    }

    public UserRoleFilter getRole() {
        return role;
    }

    public Optional<UserRoleFilter> optionalRole() {
        return Optional.ofNullable(role);
    }

    public UserRoleFilter role() {
        if (role == null) {
            setRole(new UserRoleFilter());
        }
        return role;
    }

    public void setRole(UserRoleFilter role) {
        this.role = role;
    }

    public StringFilter getMatricule() {
        return matricule;
    }

    public Optional<StringFilter> optionalMatricule() {
        return Optional.ofNullable(matricule);
    }

    public StringFilter matricule() {
        if (matricule == null) {
            setMatricule(new StringFilter());
        }
        return matricule;
    }

    public void setMatricule(StringFilter matricule) {
        this.matricule = matricule;
    }

    public StringFilter getFcmToken() {
        return fcmToken;
    }

    public Optional<StringFilter> optionalFcmToken() {
        return Optional.ofNullable(fcmToken);
    }

    public StringFilter fcmToken() {
        if (fcmToken == null) {
            setFcmToken(new StringFilter());
        }
        return fcmToken;
    }

    public void setFcmToken(StringFilter fcmToken) {
        this.fcmToken = fcmToken;
    }

    public BooleanFilter getNotificationsPush() {
        return notificationsPush;
    }

    public Optional<BooleanFilter> optionalNotificationsPush() {
        return Optional.ofNullable(notificationsPush);
    }

    public BooleanFilter notificationsPush() {
        if (notificationsPush == null) {
            setNotificationsPush(new BooleanFilter());
        }
        return notificationsPush;
    }

    public void setNotificationsPush(BooleanFilter notificationsPush) {
        this.notificationsPush = notificationsPush;
    }

    public BooleanFilter getNotificationsSms() {
        return notificationsSms;
    }

    public Optional<BooleanFilter> optionalNotificationsSms() {
        return Optional.ofNullable(notificationsSms);
    }

    public BooleanFilter notificationsSms() {
        if (notificationsSms == null) {
            setNotificationsSms(new BooleanFilter());
        }
        return notificationsSms;
    }

    public void setNotificationsSms(BooleanFilter notificationsSms) {
        this.notificationsSms = notificationsSms;
    }

    public StringFilter getLangue() {
        return langue;
    }

    public Optional<StringFilter> optionalLangue() {
        return Optional.ofNullable(langue);
    }

    public StringFilter langue() {
        if (langue == null) {
            setLangue(new StringFilter());
        }
        return langue;
    }

    public void setLangue(StringFilter langue) {
        this.langue = langue;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public Optional<InstantFilter> optionalDateCreation() {
        return Optional.ofNullable(dateCreation);
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            setDateCreation(new InstantFilter());
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public InstantFilter getDerniereConnexion() {
        return derniereConnexion;
    }

    public Optional<InstantFilter> optionalDerniereConnexion() {
        return Optional.ofNullable(derniereConnexion);
    }

    public InstantFilter derniereConnexion() {
        if (derniereConnexion == null) {
            setDerniereConnexion(new InstantFilter());
        }
        return derniereConnexion;
    }

    public void setDerniereConnexion(InstantFilter derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public BooleanFilter getActif() {
        return actif;
    }

    public Optional<BooleanFilter> optionalActif() {
        return Optional.ofNullable(actif);
    }

    public BooleanFilter actif() {
        if (actif == null) {
            setActif(new BooleanFilter());
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
    }

    public LocalDateFilter getDateEmbauche() {
        return dateEmbauche;
    }

    public Optional<LocalDateFilter> optionalDateEmbauche() {
        return Optional.ofNullable(dateEmbauche);
    }

    public LocalDateFilter dateEmbauche() {
        if (dateEmbauche == null) {
            setDateEmbauche(new LocalDateFilter());
        }
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDateFilter dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public StringFilter getNumeroPermis() {
        return numeroPermis;
    }

    public Optional<StringFilter> optionalNumeroPermis() {
        return Optional.ofNullable(numeroPermis);
    }

    public StringFilter numeroPermis() {
        if (numeroPermis == null) {
            setNumeroPermis(new StringFilter());
        }
        return numeroPermis;
    }

    public void setNumeroPermis(StringFilter numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public LongFilter getFavorisId() {
        return favorisId;
    }

    public Optional<LongFilter> optionalFavorisId() {
        return Optional.ofNullable(favorisId);
    }

    public LongFilter favorisId() {
        if (favorisId == null) {
            setFavorisId(new LongFilter());
        }
        return favorisId;
    }

    public void setFavorisId(LongFilter favorisId) {
        this.favorisId = favorisId;
    }

    public LongFilter getNotificationsId() {
        return notificationsId;
    }

    public Optional<LongFilter> optionalNotificationsId() {
        return Optional.ofNullable(notificationsId);
    }

    public LongFilter notificationsId() {
        if (notificationsId == null) {
            setNotificationsId(new LongFilter());
        }
        return notificationsId;
    }

    public void setNotificationsId(LongFilter notificationsId) {
        this.notificationsId = notificationsId;
    }

    public LongFilter getFeedbacksId() {
        return feedbacksId;
    }

    public Optional<LongFilter> optionalFeedbacksId() {
        return Optional.ofNullable(feedbacksId);
    }

    public LongFilter feedbacksId() {
        if (feedbacksId == null) {
            setFeedbacksId(new LongFilter());
        }
        return feedbacksId;
    }

    public void setFeedbacksId(LongFilter feedbacksId) {
        this.feedbacksId = feedbacksId;
    }

    public LongFilter getHistoriqueAlertesId() {
        return historiqueAlertesId;
    }

    public Optional<LongFilter> optionalHistoriqueAlertesId() {
        return Optional.ofNullable(historiqueAlertesId);
    }

    public LongFilter historiqueAlertesId() {
        if (historiqueAlertesId == null) {
            setHistoriqueAlertesId(new LongFilter());
        }
        return historiqueAlertesId;
    }

    public void setHistoriqueAlertesId(LongFilter historiqueAlertesId) {
        this.historiqueAlertesId = historiqueAlertesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UtilisateurCriteria that = (UtilisateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(motDePasse, that.motDePasse) &&
            Objects.equals(role, that.role) &&
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(fcmToken, that.fcmToken) &&
            Objects.equals(notificationsPush, that.notificationsPush) &&
            Objects.equals(notificationsSms, that.notificationsSms) &&
            Objects.equals(langue, that.langue) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(derniereConnexion, that.derniereConnexion) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(dateEmbauche, that.dateEmbauche) &&
            Objects.equals(numeroPermis, that.numeroPermis) &&
            Objects.equals(favorisId, that.favorisId) &&
            Objects.equals(notificationsId, that.notificationsId) &&
            Objects.equals(feedbacksId, that.feedbacksId) &&
            Objects.equals(historiqueAlertesId, that.historiqueAlertesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            prenom,
            nom,
            email,
            telephone,
            motDePasse,
            role,
            matricule,
            fcmToken,
            notificationsPush,
            notificationsSms,
            langue,
            dateCreation,
            derniereConnexion,
            actif,
            dateEmbauche,
            numeroPermis,
            favorisId,
            notificationsId,
            feedbacksId,
            historiqueAlertesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalMotDePasse().map(f -> "motDePasse=" + f + ", ").orElse("") +
            optionalRole().map(f -> "role=" + f + ", ").orElse("") +
            optionalMatricule().map(f -> "matricule=" + f + ", ").orElse("") +
            optionalFcmToken().map(f -> "fcmToken=" + f + ", ").orElse("") +
            optionalNotificationsPush().map(f -> "notificationsPush=" + f + ", ").orElse("") +
            optionalNotificationsSms().map(f -> "notificationsSms=" + f + ", ").orElse("") +
            optionalLangue().map(f -> "langue=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDerniereConnexion().map(f -> "derniereConnexion=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalDateEmbauche().map(f -> "dateEmbauche=" + f + ", ").orElse("") +
            optionalNumeroPermis().map(f -> "numeroPermis=" + f + ", ").orElse("") +
            optionalFavorisId().map(f -> "favorisId=" + f + ", ").orElse("") +
            optionalNotificationsId().map(f -> "notificationsId=" + f + ", ").orElse("") +
            optionalFeedbacksId().map(f -> "feedbacksId=" + f + ", ").orElse("") +
            optionalHistoriqueAlertesId().map(f -> "historiqueAlertesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
