package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlerteLigneArret.
 */
@Entity
@Table(name = "alerte_ligne_arret")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteLigneArret implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sens")
    private String sens;

    @Column(name = "actif")
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets", "operateur" }, allowSetters = true)
    private Ligne ligne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ligneArrets" }, allowSetters = true)
    private Arret arret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alerteLigneArrets", "historiqueAlertes", "utilisateur" }, allowSetters = true)
    private AlerteApproche alerteApproche;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlerteLigneArret id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSens() {
        return this.sens;
    }

    public AlerteLigneArret sens(String sens) {
        this.setSens(sens);
        return this;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public AlerteLigneArret actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Ligne getLigne() {
        return this.ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public AlerteLigneArret ligne(Ligne ligne) {
        this.setLigne(ligne);
        return this;
    }

    public Arret getArret() {
        return this.arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
    }

    public AlerteLigneArret arret(Arret arret) {
        this.setArret(arret);
        return this;
    }

    public AlerteApproche getAlerteApproche() {
        return this.alerteApproche;
    }

    public void setAlerteApproche(AlerteApproche alerteApproche) {
        this.alerteApproche = alerteApproche;
    }

    public AlerteLigneArret alerteApproche(AlerteApproche alerteApproche) {
        this.setAlerteApproche(alerteApproche);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlerteLigneArret)) {
            return false;
        }
        return getId() != null && getId().equals(((AlerteLigneArret) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteLigneArret{" +
            "id=" + getId() +
            ", sens='" + getSens() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
