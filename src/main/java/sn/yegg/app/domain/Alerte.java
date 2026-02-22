package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alerte.
 */
@Entity
@Table(name = "alerte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type_cible", nullable = false)
    private String typeCible;

    @NotNull
    @Column(name = "cible_id", nullable = false)
    private Long cibleId;

    @NotNull
    @Min(value = 1)
    @Max(value = 60)
    @Column(name = "seuil_minutes", nullable = false)
    private Integer seuilMinutes;

    @Column(name = "jours_activation")
    private String joursActivation;

    @Column(name = "heure_debut")
    private Instant heureDebut;

    @Column(name = "heure_fin")
    private Instant heureFin;

    @NotNull
    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "dernier_declenchement")
    private Instant dernierDeclenchement;

    @Column(name = "nombre_declenchements")
    private Integer nombreDeclenchements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alerte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCible() {
        return this.typeCible;
    }

    public Alerte typeCible(String typeCible) {
        this.setTypeCible(typeCible);
        return this;
    }

    public void setTypeCible(String typeCible) {
        this.typeCible = typeCible;
    }

    public Long getCibleId() {
        return this.cibleId;
    }

    public Alerte cibleId(Long cibleId) {
        this.setCibleId(cibleId);
        return this;
    }

    public void setCibleId(Long cibleId) {
        this.cibleId = cibleId;
    }

    public Integer getSeuilMinutes() {
        return this.seuilMinutes;
    }

    public Alerte seuilMinutes(Integer seuilMinutes) {
        this.setSeuilMinutes(seuilMinutes);
        return this;
    }

    public void setSeuilMinutes(Integer seuilMinutes) {
        this.seuilMinutes = seuilMinutes;
    }

    public String getJoursActivation() {
        return this.joursActivation;
    }

    public Alerte joursActivation(String joursActivation) {
        this.setJoursActivation(joursActivation);
        return this;
    }

    public void setJoursActivation(String joursActivation) {
        this.joursActivation = joursActivation;
    }

    public Instant getHeureDebut() {
        return this.heureDebut;
    }

    public Alerte heureDebut(Instant heureDebut) {
        this.setHeureDebut(heureDebut);
        return this;
    }

    public void setHeureDebut(Instant heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Instant getHeureFin() {
        return this.heureFin;
    }

    public Alerte heureFin(Instant heureFin) {
        this.setHeureFin(heureFin);
        return this;
    }

    public void setHeureFin(Instant heureFin) {
        this.heureFin = heureFin;
    }

    public String getStatut() {
        return this.statut;
    }

    public Alerte statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getDernierDeclenchement() {
        return this.dernierDeclenchement;
    }

    public Alerte dernierDeclenchement(Instant dernierDeclenchement) {
        this.setDernierDeclenchement(dernierDeclenchement);
        return this;
    }

    public void setDernierDeclenchement(Instant dernierDeclenchement) {
        this.dernierDeclenchement = dernierDeclenchement;
    }

    public Integer getNombreDeclenchements() {
        return this.nombreDeclenchements;
    }

    public Alerte nombreDeclenchements(Integer nombreDeclenchements) {
        this.setNombreDeclenchements(nombreDeclenchements);
        return this;
    }

    public void setNombreDeclenchements(Integer nombreDeclenchements) {
        this.nombreDeclenchements = nombreDeclenchements;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Alerte utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alerte)) {
            return false;
        }
        return getId() != null && getId().equals(((Alerte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alerte{" +
            "id=" + getId() +
            ", typeCible='" + getTypeCible() + "'" +
            ", cibleId=" + getCibleId() +
            ", seuilMinutes=" + getSeuilMinutes() +
            ", joursActivation='" + getJoursActivation() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dernierDeclenchement='" + getDernierDeclenchement() + "'" +
            ", nombreDeclenchements=" + getNombreDeclenchements() +
            "}";
    }
}
