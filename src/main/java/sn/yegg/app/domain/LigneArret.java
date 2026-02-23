package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LigneArret.
 */
@Entity
@Table(name = "ligne_arret")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneArret implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private Integer ordre;

    @Column(name = "temps_trajet_depart")
    private Integer tempsTrajetDepart;

    @Column(name = "distance_depart", precision = 21, scale = 2)
    private BigDecimal distanceDepart;

    @Column(name = "temps_arret_moyen")
    private Integer tempsArretMoyen;

    @Column(name = "arret_physique")
    private Boolean arretPhysique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets", "operateur" }, allowSetters = true)
    private Ligne ligne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets" }, allowSetters = true)
    private Arret arret;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LigneArret id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public LigneArret ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Integer getTempsTrajetDepart() {
        return this.tempsTrajetDepart;
    }

    public LigneArret tempsTrajetDepart(Integer tempsTrajetDepart) {
        this.setTempsTrajetDepart(tempsTrajetDepart);
        return this;
    }

    public void setTempsTrajetDepart(Integer tempsTrajetDepart) {
        this.tempsTrajetDepart = tempsTrajetDepart;
    }

    public BigDecimal getDistanceDepart() {
        return this.distanceDepart;
    }

    public LigneArret distanceDepart(BigDecimal distanceDepart) {
        this.setDistanceDepart(distanceDepart);
        return this;
    }

    public void setDistanceDepart(BigDecimal distanceDepart) {
        this.distanceDepart = distanceDepart;
    }

    public Integer getTempsArretMoyen() {
        return this.tempsArretMoyen;
    }

    public LigneArret tempsArretMoyen(Integer tempsArretMoyen) {
        this.setTempsArretMoyen(tempsArretMoyen);
        return this;
    }

    public void setTempsArretMoyen(Integer tempsArretMoyen) {
        this.tempsArretMoyen = tempsArretMoyen;
    }

    public Boolean getArretPhysique() {
        return this.arretPhysique;
    }

    public LigneArret arretPhysique(Boolean arretPhysique) {
        this.setArretPhysique(arretPhysique);
        return this;
    }

    public void setArretPhysique(Boolean arretPhysique) {
        this.arretPhysique = arretPhysique;
    }

    public Ligne getLigne() {
        return this.ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public LigneArret ligne(Ligne ligne) {
        this.setLigne(ligne);
        return this;
    }

    public Arret getArret() {
        return this.arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
    }

    public LigneArret arret(Arret arret) {
        this.setArret(arret);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneArret)) {
            return false;
        }
        return getId() != null && getId().equals(((LigneArret) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneArret{" +
            "id=" + getId() +
            ", ordre=" + getOrdre() +
            ", tempsTrajetDepart=" + getTempsTrajetDepart() +
            ", distanceDepart=" + getDistanceDepart() +
            ", tempsArretMoyen=" + getTempsArretMoyen() +
            ", arretPhysique='" + getArretPhysique() + "'" +
            "}";
    }
}
