package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.LineStatus;

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
    @Column(name = "numero", length = 10, nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "direction", nullable = false)
    private String direction;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "couleur")
    private String couleur;

    @DecimalMin(value = "0")
    @Column(name = "distance_km", precision = 21, scale = 2)
    private BigDecimal distanceKm;

    @Min(value = 0)
    @Column(name = "duree_moyenne")
    private Integer dureeMoyenne;

    @Min(value = 1)
    @Max(value = 60)
    @Column(name = "frequence")
    private Integer frequence;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private LineStatus statut;

    @Lob
    @Column(name = "jours_feries")
    private String joursFeries;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

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

    public String getDescription() {
        return this.description;
    }

    public Ligne description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getFrequence() {
        return this.frequence;
    }

    public Ligne frequence(Integer frequence) {
        this.setFrequence(frequence);
        return this;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }

    public LineStatus getStatut() {
        return this.statut;
    }

    public Ligne statut(LineStatus statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(LineStatus statut) {
        this.statut = statut;
    }

    public String getJoursFeries() {
        return this.joursFeries;
    }

    public Ligne joursFeries(String joursFeries) {
        this.setJoursFeries(joursFeries);
        return this;
    }

    public void setJoursFeries(String joursFeries) {
        this.joursFeries = joursFeries;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Ligne dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Ligne dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Ligne actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
            ", description='" + getDescription() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", dureeMoyenne=" + getDureeMoyenne() +
            ", frequence=" + getFrequence() +
            ", statut='" + getStatut() + "'" +
            ", joursFeries='" + getJoursFeries() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
