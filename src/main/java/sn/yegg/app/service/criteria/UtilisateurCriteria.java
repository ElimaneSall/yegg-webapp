package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricule;

    private StringFilter telephone;

    private StringFilter fcmToken;

    private BooleanFilter notificationsPush;

    private StringFilter langue;

    private LocalDateFilter dateEmbauche;

    private StringFilter numeroPermis;

    private LongFilter busId;

    private Boolean distinct;

    public UtilisateurCriteria() {}

    public UtilisateurCriteria(UtilisateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.matricule = other.optionalMatricule().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.fcmToken = other.optionalFcmToken().map(StringFilter::copy).orElse(null);
        this.notificationsPush = other.optionalNotificationsPush().map(BooleanFilter::copy).orElse(null);
        this.langue = other.optionalLangue().map(StringFilter::copy).orElse(null);
        this.dateEmbauche = other.optionalDateEmbauche().map(LocalDateFilter::copy).orElse(null);
        this.numeroPermis = other.optionalNumeroPermis().map(StringFilter::copy).orElse(null);
        this.busId = other.optionalBusId().map(LongFilter::copy).orElse(null);
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

    public LongFilter getBusId() {
        return busId;
    }

    public Optional<LongFilter> optionalBusId() {
        return Optional.ofNullable(busId);
    }

    public LongFilter busId() {
        if (busId == null) {
            setBusId(new LongFilter());
        }
        return busId;
    }

    public void setBusId(LongFilter busId) {
        this.busId = busId;
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
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(fcmToken, that.fcmToken) &&
            Objects.equals(notificationsPush, that.notificationsPush) &&
            Objects.equals(langue, that.langue) &&
            Objects.equals(dateEmbauche, that.dateEmbauche) &&
            Objects.equals(numeroPermis, that.numeroPermis) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricule, telephone, fcmToken, notificationsPush, langue, dateEmbauche, numeroPermis, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMatricule().map(f -> "matricule=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalFcmToken().map(f -> "fcmToken=" + f + ", ").orElse("") +
            optionalNotificationsPush().map(f -> "notificationsPush=" + f + ", ").orElse("") +
            optionalLangue().map(f -> "langue=" + f + ", ").orElse("") +
            optionalDateEmbauche().map(f -> "dateEmbauche=" + f + ", ").orElse("") +
            optionalNumeroPermis().map(f -> "numeroPermis=" + f + ", ").orElse("") +
            optionalBusId().map(f -> "busId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
