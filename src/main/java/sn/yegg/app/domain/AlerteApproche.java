package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;

/**
 * A AlerteApproche.
 */
@Entity
@Table(name = "alerte_approche")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteApproche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Min(value = 50)
    @Max(value = 1000)
    @Column(name = "seuil_distance")
    private Integer seuilDistance;

    @Min(value = 1)
    @Max(value = 60)
    @Column(name = "seuil_temps")
    private Integer seuilTemps;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_seuil", nullable = false)
    private ThresholdType typeSeuil;

    @Column(name = "jours_activation")
    private String joursActivation;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "heure_fin")
    private String heureFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private AlertStatus statut;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_modification")
    private Instant dateModification;

    @Column(name = "dernier_declenchement")
    private Instant dernierDeclenchement;

    @Min(value = 0)
    @Column(name = "nombre_declenchements")
    private Integer nombreDeclenchements;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alerteApproche")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ligne", "arret", "alerteApproche" }, allowSetters = true)
    private Set<AlerteLigneArret> alerteLigneArrets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alerteApproche")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bus", "alerteApproche", "utilisateur" }, allowSetters = true)
    private Set<HistoriqueAlerte> historiqueAlertes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlerteApproche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public AlerteApproche nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getSeuilDistance() {
        return this.seuilDistance;
    }

    public AlerteApproche seuilDistance(Integer seuilDistance) {
        this.setSeuilDistance(seuilDistance);
        return this;
    }

    public void setSeuilDistance(Integer seuilDistance) {
        this.seuilDistance = seuilDistance;
    }

    public Integer getSeuilTemps() {
        return this.seuilTemps;
    }

    public AlerteApproche seuilTemps(Integer seuilTemps) {
        this.setSeuilTemps(seuilTemps);
        return this;
    }

    public void setSeuilTemps(Integer seuilTemps) {
        this.seuilTemps = seuilTemps;
    }

    public ThresholdType getTypeSeuil() {
        return this.typeSeuil;
    }

    public AlerteApproche typeSeuil(ThresholdType typeSeuil) {
        this.setTypeSeuil(typeSeuil);
        return this;
    }

    public void setTypeSeuil(ThresholdType typeSeuil) {
        this.typeSeuil = typeSeuil;
    }

    public String getJoursActivation() {
        return this.joursActivation;
    }

    public AlerteApproche joursActivation(String joursActivation) {
        this.setJoursActivation(joursActivation);
        return this;
    }

    public void setJoursActivation(String joursActivation) {
        this.joursActivation = joursActivation;
    }

    public String getHeureDebut() {
        return this.heureDebut;
    }

    public AlerteApproche heureDebut(String heureDebut) {
        this.setHeureDebut(heureDebut);
        return this;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return this.heureFin;
    }

    public AlerteApproche heureFin(String heureFin) {
        this.setHeureFin(heureFin);
        return this;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public AlertStatus getStatut() {
        return this.statut;
    }

    public AlerteApproche statut(AlertStatus statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(AlertStatus statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public AlerteApproche dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return this.dateModification;
    }

    public AlerteApproche dateModification(Instant dateModification) {
        this.setDateModification(dateModification);
        return this;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public Instant getDernierDeclenchement() {
        return this.dernierDeclenchement;
    }

    public AlerteApproche dernierDeclenchement(Instant dernierDeclenchement) {
        this.setDernierDeclenchement(dernierDeclenchement);
        return this;
    }

    public void setDernierDeclenchement(Instant dernierDeclenchement) {
        this.dernierDeclenchement = dernierDeclenchement;
    }

    public Integer getNombreDeclenchements() {
        return this.nombreDeclenchements;
    }

    public AlerteApproche nombreDeclenchements(Integer nombreDeclenchements) {
        this.setNombreDeclenchements(nombreDeclenchements);
        return this;
    }

    public void setNombreDeclenchements(Integer nombreDeclenchements) {
        this.nombreDeclenchements = nombreDeclenchements;
    }

    public Set<AlerteLigneArret> getAlerteLigneArrets() {
        return this.alerteLigneArrets;
    }

    public void setAlerteLigneArrets(Set<AlerteLigneArret> alerteLigneArrets) {
        if (this.alerteLigneArrets != null) {
            this.alerteLigneArrets.forEach(i -> i.setAlerteApproche(null));
        }
        if (alerteLigneArrets != null) {
            alerteLigneArrets.forEach(i -> i.setAlerteApproche(this));
        }
        this.alerteLigneArrets = alerteLigneArrets;
    }

    public AlerteApproche alerteLigneArrets(Set<AlerteLigneArret> alerteLigneArrets) {
        this.setAlerteLigneArrets(alerteLigneArrets);
        return this;
    }

    public AlerteApproche addAlerteLigneArret(AlerteLigneArret alerteLigneArret) {
        this.alerteLigneArrets.add(alerteLigneArret);
        alerteLigneArret.setAlerteApproche(this);
        return this;
    }

    public AlerteApproche removeAlerteLigneArret(AlerteLigneArret alerteLigneArret) {
        this.alerteLigneArrets.remove(alerteLigneArret);
        alerteLigneArret.setAlerteApproche(null);
        return this;
    }

    public Set<HistoriqueAlerte> getHistoriqueAlertes() {
        return this.historiqueAlertes;
    }

    public void setHistoriqueAlertes(Set<HistoriqueAlerte> historiqueAlertes) {
        if (this.historiqueAlertes != null) {
            this.historiqueAlertes.forEach(i -> i.setAlerteApproche(null));
        }
        if (historiqueAlertes != null) {
            historiqueAlertes.forEach(i -> i.setAlerteApproche(this));
        }
        this.historiqueAlertes = historiqueAlertes;
    }

    public AlerteApproche historiqueAlertes(Set<HistoriqueAlerte> historiqueAlertes) {
        this.setHistoriqueAlertes(historiqueAlertes);
        return this;
    }

    public AlerteApproche addHistoriqueAlertes(HistoriqueAlerte historiqueAlerte) {
        this.historiqueAlertes.add(historiqueAlerte);
        historiqueAlerte.setAlerteApproche(this);
        return this;
    }

    public AlerteApproche removeHistoriqueAlertes(HistoriqueAlerte historiqueAlerte) {
        this.historiqueAlertes.remove(historiqueAlerte);
        historiqueAlerte.setAlerteApproche(null);
        return this;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public AlerteApproche utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlerteApproche)) {
            return false;
        }
        return getId() != null && getId().equals(((AlerteApproche) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteApproche{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", seuilDistance=" + getSeuilDistance() +
            ", seuilTemps=" + getSeuilTemps() +
            ", typeSeuil='" + getTypeSeuil() + "'" +
            ", joursActivation='" + getJoursActivation() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dernierDeclenchement='" + getDernierDeclenchement() + "'" +
            ", nombreDeclenchements=" + getNombreDeclenchements() +
            "}";
    }
}
