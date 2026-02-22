package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ligne.
 */
@Entity
@Table(name = "ligne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ligne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "direction", nullable = false)
    private String direction;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(name = "couleur")
    private String couleur;

    @Column(name = "distance_km", precision = 21, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "duree_moyenne")
    private Integer dureeMoyenne;

    @NotNull
    @Column(name = "statut", nullable = false)
    private String statut;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ligne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ligne", "arret" }, allowSetters = true)
    private Set<LigneArret> ligneArrets = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lignes" }, allowSetters = true)
    private Operateur operateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ligne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Ligne numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return this.nom;
    }

    public Ligne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDirection() {
        return this.direction;
    }

    public Ligne direction(String direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public Ligne couleur(String couleur) {
        this.setCouleur(couleur);
        return this;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public BigDecimal getDistanceKm() {
        return this.distanceKm;
    }

    public Ligne distanceKm(BigDecimal distanceKm) {
        this.setDistanceKm(distanceKm);
        return this;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getDureeMoyenne() {
        return this.dureeMoyenne;
    }

    public Ligne dureeMoyenne(Integer dureeMoyenne) {
        this.setDureeMoyenne(dureeMoyenne);
        return this;
    }

    public void setDureeMoyenne(Integer dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
    }

    public String getStatut() {
        return this.statut;
    }

    public Ligne statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Set<LigneArret> getLigneArrets() {
        return this.ligneArrets;
    }

    public void setLigneArrets(Set<LigneArret> ligneArrets) {
        if (this.ligneArrets != null) {
            this.ligneArrets.forEach(i -> i.setLigne(null));
        }
        if (ligneArrets != null) {
            ligneArrets.forEach(i -> i.setLigne(this));
        }
        this.ligneArrets = ligneArrets;
    }

    public Ligne ligneArrets(Set<LigneArret> ligneArrets) {
        this.setLigneArrets(ligneArrets);
        return this;
    }

    public Ligne addLigneArrets(LigneArret ligneArret) {
        this.ligneArrets.add(ligneArret);
        ligneArret.setLigne(this);
        return this;
    }

    public Ligne removeLigneArrets(LigneArret ligneArret) {
        this.ligneArrets.remove(ligneArret);
        ligneArret.setLigne(null);
        return this;
    }

    public Operateur getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public Ligne operateur(Operateur operateur) {
        this.setOperateur(operateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ligne)) {
            return false;
        }
        return getId() != null && getId().equals(((Ligne) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ligne{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", nom='" + getNom() + "'" +
            ", direction='" + getDirection() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", dureeMoyenne=" + getDureeMoyenne() +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
