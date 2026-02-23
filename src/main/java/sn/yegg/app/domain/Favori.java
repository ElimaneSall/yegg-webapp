package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.FavoriteType;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FavoriteType type;

    @NotNull
    @Column(name = "cible_id", nullable = false)
    private Long cibleId;

    @Column(name = "nom_personnalise")
    private String nomPersonnalise;

    @Column(name = "ordre")
    private Integer ordre;

    @NotNull
    @Column(name = "date_ajout", nullable = false)
    private Instant dateAjout;

    @Column(name = "dernier_acces")
    private Instant dernierAcces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
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

    public FavoriteType getType() {
        return this.type;
    }

    public Favori type(FavoriteType type) {
        this.setType(type);
        return this;
    }

    public void setType(FavoriteType type) {
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

    public Instant getDateAjout() {
        return this.dateAjout;
    }

    public Favori dateAjout(Instant dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(Instant dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Instant getDernierAcces() {
        return this.dernierAcces;
    }

    public Favori dernierAcces(Instant dernierAcces) {
        this.setDernierAcces(dernierAcces);
        return this;
    }

    public void setDernierAcces(Instant dernierAcces) {
        this.dernierAcces = dernierAcces;
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
            ", dateAjout='" + getDateAjout() + "'" +
            ", dernierAcces='" + getDernierAcces() + "'" +
            "}";
    }
}
