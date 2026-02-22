package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Favori.
 */
@Entity
@Table(name = "favori")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Favori implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "cible_id", nullable = false)
    private Long cibleId;

    @Column(name = "nom_personnalise")
    private String nomPersonnalise;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "alerte_active")
    private Boolean alerteActive;

    @Min(value = 1)
    @Max(value = 60)
    @Column(name = "alerte_seuil")
    private Integer alerteSeuil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bus" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Favori id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Favori type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCibleId() {
        return this.cibleId;
    }

    public Favori cibleId(Long cibleId) {
        this.setCibleId(cibleId);
        return this;
    }

    public void setCibleId(Long cibleId) {
        this.cibleId = cibleId;
    }

    public String getNomPersonnalise() {
        return this.nomPersonnalise;
    }

    public Favori nomPersonnalise(String nomPersonnalise) {
        this.setNomPersonnalise(nomPersonnalise);
        return this;
    }

    public void setNomPersonnalise(String nomPersonnalise) {
        this.nomPersonnalise = nomPersonnalise;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public Favori ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Boolean getAlerteActive() {
        return this.alerteActive;
    }

    public Favori alerteActive(Boolean alerteActive) {
        this.setAlerteActive(alerteActive);
        return this;
    }

    public void setAlerteActive(Boolean alerteActive) {
        this.alerteActive = alerteActive;
    }

    public Integer getAlerteSeuil() {
        return this.alerteSeuil;
    }

    public Favori alerteSeuil(Integer alerteSeuil) {
        this.setAlerteSeuil(alerteSeuil);
        return this;
    }

    public void setAlerteSeuil(Integer alerteSeuil) {
        this.alerteSeuil = alerteSeuil;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Favori utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favori)) {
            return false;
        }
        return getId() != null && getId().equals(((Favori) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Favori{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", cibleId=" + getCibleId() +
            ", nomPersonnalise='" + getNomPersonnalise() + "'" +
            ", ordre=" + getOrdre() +
            ", alerteActive='" + getAlerteActive() + "'" +
            ", alerteSeuil=" + getAlerteSeuil() +
            "}";
    }
}
