package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.ReportFormat;
import sn.yegg.app.domain.enumeration.ReportType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Rapport} entity. This class is used
 * in {@link sn.yegg.app.web.rest.RapportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rapports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReportType
     */
    public static class ReportTypeFilter extends Filter<ReportType> {

        public ReportTypeFilter() {}

        public ReportTypeFilter(ReportTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReportTypeFilter copy() {
            return new ReportTypeFilter(this);
        }
    }

    /**
     * Class for filtering ReportFormat
     */
    public static class ReportFormatFilter extends Filter<ReportFormat> {

        public ReportFormatFilter() {}

        public ReportFormatFilter(ReportFormatFilter filter) {
            super(filter);
        }

        @Override
        public ReportFormatFilter copy() {
            return new ReportFormatFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private ReportTypeFilter type;

    private LocalDateFilter periodeDebut;

    private LocalDateFilter periodeFin;

    private ReportFormatFilter format;

    private InstantFilter dateGeneration;

    private StringFilter generePar;

    private LongFilter operateurId;

    private LongFilter adminId;

    private Boolean distinct;

    public RapportCriteria() {}

    public RapportCriteria(RapportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(ReportTypeFilter::copy).orElse(null);
        this.periodeDebut = other.optionalPeriodeDebut().map(LocalDateFilter::copy).orElse(null);
        this.periodeFin = other.optionalPeriodeFin().map(LocalDateFilter::copy).orElse(null);
        this.format = other.optionalFormat().map(ReportFormatFilter::copy).orElse(null);
        this.dateGeneration = other.optionalDateGeneration().map(InstantFilter::copy).orElse(null);
        this.generePar = other.optionalGenerePar().map(StringFilter::copy).orElse(null);
        this.operateurId = other.optionalOperateurId().map(LongFilter::copy).orElse(null);
        this.adminId = other.optionalAdminId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RapportCriteria copy() {
        return new RapportCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public ReportTypeFilter getType() {
        return type;
    }

    public Optional<ReportTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public ReportTypeFilter type() {
        if (type == null) {
            setType(new ReportTypeFilter());
        }
        return type;
    }

    public void setType(ReportTypeFilter type) {
        this.type = type;
    }

    public LocalDateFilter getPeriodeDebut() {
        return periodeDebut;
    }

    public Optional<LocalDateFilter> optionalPeriodeDebut() {
        return Optional.ofNullable(periodeDebut);
    }

    public LocalDateFilter periodeDebut() {
        if (periodeDebut == null) {
            setPeriodeDebut(new LocalDateFilter());
        }
        return periodeDebut;
    }

    public void setPeriodeDebut(LocalDateFilter periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public LocalDateFilter getPeriodeFin() {
        return periodeFin;
    }

    public Optional<LocalDateFilter> optionalPeriodeFin() {
        return Optional.ofNullable(periodeFin);
    }

    public LocalDateFilter periodeFin() {
        if (periodeFin == null) {
            setPeriodeFin(new LocalDateFilter());
        }
        return periodeFin;
    }

    public void setPeriodeFin(LocalDateFilter periodeFin) {
        this.periodeFin = periodeFin;
    }

    public ReportFormatFilter getFormat() {
        return format;
    }

    public Optional<ReportFormatFilter> optionalFormat() {
        return Optional.ofNullable(format);
    }

    public ReportFormatFilter format() {
        if (format == null) {
            setFormat(new ReportFormatFilter());
        }
        return format;
    }

    public void setFormat(ReportFormatFilter format) {
        this.format = format;
    }

    public InstantFilter getDateGeneration() {
        return dateGeneration;
    }

    public Optional<InstantFilter> optionalDateGeneration() {
        return Optional.ofNullable(dateGeneration);
    }

    public InstantFilter dateGeneration() {
        if (dateGeneration == null) {
            setDateGeneration(new InstantFilter());
        }
        return dateGeneration;
    }

    public void setDateGeneration(InstantFilter dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public StringFilter getGenerePar() {
        return generePar;
    }

    public Optional<StringFilter> optionalGenerePar() {
        return Optional.ofNullable(generePar);
    }

    public StringFilter generePar() {
        if (generePar == null) {
            setGenerePar(new StringFilter());
        }
        return generePar;
    }

    public void setGenerePar(StringFilter generePar) {
        this.generePar = generePar;
    }

    public LongFilter getOperateurId() {
        return operateurId;
    }

    public Optional<LongFilter> optionalOperateurId() {
        return Optional.ofNullable(operateurId);
    }

    public LongFilter operateurId() {
        if (operateurId == null) {
            setOperateurId(new LongFilter());
        }
        return operateurId;
    }

    public void setOperateurId(LongFilter operateurId) {
        this.operateurId = operateurId;
    }

    public LongFilter getAdminId() {
        return adminId;
    }

    public Optional<LongFilter> optionalAdminId() {
        return Optional.ofNullable(adminId);
    }

    public LongFilter adminId() {
        if (adminId == null) {
            setAdminId(new LongFilter());
        }
        return adminId;
    }

    public void setAdminId(LongFilter adminId) {
        this.adminId = adminId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RapportCriteria that = (RapportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(type, that.type) &&
            Objects.equals(periodeDebut, that.periodeDebut) &&
            Objects.equals(periodeFin, that.periodeFin) &&
            Objects.equals(format, that.format) &&
            Objects.equals(dateGeneration, that.dateGeneration) &&
            Objects.equals(generePar, that.generePar) &&
            Objects.equals(operateurId, that.operateurId) &&
            Objects.equals(adminId, that.adminId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, type, periodeDebut, periodeFin, format, dateGeneration, generePar, operateurId, adminId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPeriodeDebut().map(f -> "periodeDebut=" + f + ", ").orElse("") +
            optionalPeriodeFin().map(f -> "periodeFin=" + f + ", ").orElse("") +
            optionalFormat().map(f -> "format=" + f + ", ").orElse("") +
            optionalDateGeneration().map(f -> "dateGeneration=" + f + ", ").orElse("") +
            optionalGenerePar().map(f -> "generePar=" + f + ", ").orElse("") +
            optionalOperateurId().map(f -> "operateurId=" + f + ", ").orElse("") +
            optionalAdminId().map(f -> "adminId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
