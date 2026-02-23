package sn.yegg.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import sn.yegg.app.domain.enumeration.ReportFormat;
import sn.yegg.app.domain.enumeration.ReportType;

/**
 * A DTO for the {@link sn.yegg.app.domain.Rapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private ReportType type;

    @NotNull
    private LocalDate periodeDebut;

    @NotNull
    private LocalDate periodeFin;

    private ReportFormat format;

    @Lob
    private String contenu;

    @NotNull
    private Instant dateGeneration;

    private String generePar;

    private OperateurDTO operateur;

    private UtilisateurDTO admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public LocalDate getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(LocalDate periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public LocalDate getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(LocalDate periodeFin) {
        this.periodeFin = periodeFin;
    }

    public ReportFormat getFormat() {
        return format;
    }

    public void setFormat(ReportFormat format) {
        this.format = format;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getGenerePar() {
        return generePar;
    }

    public void setGenerePar(String generePar) {
        this.generePar = generePar;
    }

    public OperateurDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurDTO operateur) {
        this.operateur = operateur;
    }

    public UtilisateurDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UtilisateurDTO admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportDTO)) {
            return false;
        }

        RapportDTO rapportDTO = (RapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", periodeDebut='" + getPeriodeDebut() + "'" +
            ", periodeFin='" + getPeriodeFin() + "'" +
            ", format='" + getFormat() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", generePar='" + getGenerePar() + "'" +
            ", operateur=" + getOperateur() +
            ", admin=" + getAdmin() +
            "}";
    }
}
