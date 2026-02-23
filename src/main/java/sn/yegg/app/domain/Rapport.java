package sn.yegg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.yegg.app.domain.enumeration.ReportFormat;
import sn.yegg.app.domain.enumeration.ReportType;

/**
 * A Rapport.
 */
@Entity
@Table(name = "rapport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReportType type;

    @NotNull
    @Column(name = "periode_debut", nullable = false)
    private LocalDate periodeDebut;

    @NotNull
    @Column(name = "periode_fin", nullable = false)
    private LocalDate periodeFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private ReportFormat format;

    @Lob
    @Column(name = "contenu")
    private String contenu;

    @NotNull
    @Column(name = "date_generation", nullable = false)
    private Instant dateGeneration;

    @Column(name = "genere_par")
    private String generePar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lignes" }, allowSetters = true)
    private Operateur operateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "favorises", "notifications", "feedbacks", "historiqueAlertes" }, allowSetters = true)
    private Utilisateur admin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Rapport nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ReportType getType() {
        return this.type;
    }

    public Rapport type(ReportType type) {
        this.setType(type);
        return this;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public LocalDate getPeriodeDebut() {
        return this.periodeDebut;
    }

    public Rapport periodeDebut(LocalDate periodeDebut) {
        this.setPeriodeDebut(periodeDebut);
        return this;
    }

    public void setPeriodeDebut(LocalDate periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public LocalDate getPeriodeFin() {
        return this.periodeFin;
    }

    public Rapport periodeFin(LocalDate periodeFin) {
        this.setPeriodeFin(periodeFin);
        return this;
    }

    public void setPeriodeFin(LocalDate periodeFin) {
        this.periodeFin = periodeFin;
    }

    public ReportFormat getFormat() {
        return this.format;
    }

    public Rapport format(ReportFormat format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(ReportFormat format) {
        this.format = format;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Rapport contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateGeneration() {
        return this.dateGeneration;
    }

    public Rapport dateGeneration(Instant dateGeneration) {
        this.setDateGeneration(dateGeneration);
        return this;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getGenerePar() {
        return this.generePar;
    }

    public Rapport generePar(String generePar) {
        this.setGenerePar(generePar);
        return this;
    }

    public void setGenerePar(String generePar) {
        this.generePar = generePar;
    }

    public Operateur getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public Rapport operateur(Operateur operateur) {
        this.setOperateur(operateur);
        return this;
    }

    public Utilisateur getAdmin() {
        return this.admin;
    }

    public void setAdmin(Utilisateur utilisateur) {
        this.admin = utilisateur;
    }

    public Rapport admin(Utilisateur utilisateur) {
        this.setAdmin(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rapport)) {
            return false;
        }
        return getId() != null && getId().equals(((Rapport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rapport{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", periodeDebut='" + getPeriodeDebut() + "'" +
            ", periodeFin='" + getPeriodeFin() + "'" +
            ", format='" + getFormat() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", generePar='" + getGenerePar() + "'" +
            "}";
    }
}
