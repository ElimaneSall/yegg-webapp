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
 * A Arret.
 */
@Entity
@Table(name = "arret")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Arret implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @Column(name = "latitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Column(name = "longitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal longitude;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "zone_tarifaire")
    private String zoneTarifaire;

    @Lob
    @Column(name = "equipements")
    private String equipements;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "arret")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ligne", "arret" }, allowSetters = true)
    private Set<LigneArret> ligneArrets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Arret id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Arret nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return this.code;
    }

    public Arret code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public Arret latitude(BigDecimal latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Arret longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Arret adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getZoneTarifaire() {
        return this.zoneTarifaire;
    }

    public Arret zoneTarifaire(String zoneTarifaire) {
        this.setZoneTarifaire(zoneTarifaire);
        return this;
    }

    public void setZoneTarifaire(String zoneTarifaire) {
        this.zoneTarifaire = zoneTarifaire;
    }

    public String getEquipements() {
        return this.equipements;
    }

    public Arret equipements(String equipements) {
        this.setEquipements(equipements);
        return this;
    }

    public void setEquipements(String equipements) {
        this.equipements = equipements;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Arret actif(Boolean actif) {
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
            this.ligneArrets.forEach(i -> i.setArret(null));
        }
        if (ligneArrets != null) {
            ligneArrets.forEach(i -> i.setArret(this));
        }
        this.ligneArrets = ligneArrets;
    }

    public Arret ligneArrets(Set<LigneArret> ligneArrets) {
        this.setLigneArrets(ligneArrets);
        return this;
    }

    public Arret addLigneArrets(LigneArret ligneArret) {
        this.ligneArrets.add(ligneArret);
        ligneArret.setArret(this);
        return this;
    }

    public Arret removeLigneArrets(LigneArret ligneArret) {
        this.ligneArrets.remove(ligneArret);
        ligneArret.setArret(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arret)) {
            return false;
        }
        return getId() != null && getId().equals(((Arret) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Arret{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", adresse='" + getAdresse() + "'" +
            ", zoneTarifaire='" + getZoneTarifaire() + "'" +
            ", equipements='" + getEquipements() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
