package sn.yegg.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.yegg.app.domain.enumeration.TrackingSource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.yegg.app.domain.Tracking} entity. This class is used
 * in {@link sn.yegg.app.web.rest.TrackingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trackings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TrackingSource
     */
    public static class TrackingSourceFilter extends Filter<TrackingSource> {

        public TrackingSourceFilter() {}

        public TrackingSourceFilter(TrackingSourceFilter filter) {
            super(filter);
        }

        @Override
        public TrackingSourceFilter copy() {
            return new TrackingSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private BigDecimalFilter vitesse;

    private IntegerFilter cap;

    private IntegerFilter precision;

    private InstantFilter timestamp;

    private TrackingSourceFilter source;

    private StringFilter evenement;

    private LongFilter busId;

    private Boolean distinct;

    public TrackingCriteria() {}

    public TrackingCriteria(TrackingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(BigDecimalFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(BigDecimalFilter::copy).orElse(null);
        this.vitesse = other.optionalVitesse().map(BigDecimalFilter::copy).orElse(null);
        this.cap = other.optionalCap().map(IntegerFilter::copy).orElse(null);
        this.precision = other.optionalPrecision().map(IntegerFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.source = other.optionalSource().map(TrackingSourceFilter::copy).orElse(null);
        this.evenement = other.optionalEvenement().map(StringFilter::copy).orElse(null);
        this.busId = other.optionalBusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrackingCriteria copy() {
        return new TrackingCriteria(this);
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

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public Optional<BigDecimalFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public BigDecimalFilter latitude() {
        if (latitude == null) {
            setLatitude(new BigDecimalFilter());
        }
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public Optional<BigDecimalFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public BigDecimalFilter longitude() {
        if (longitude == null) {
            setLongitude(new BigDecimalFilter());
        }
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public BigDecimalFilter getVitesse() {
        return vitesse;
    }

    public Optional<BigDecimalFilter> optionalVitesse() {
        return Optional.ofNullable(vitesse);
    }

    public BigDecimalFilter vitesse() {
        if (vitesse == null) {
            setVitesse(new BigDecimalFilter());
        }
        return vitesse;
    }

    public void setVitesse(BigDecimalFilter vitesse) {
        this.vitesse = vitesse;
    }

    public IntegerFilter getCap() {
        return cap;
    }

    public Optional<IntegerFilter> optionalCap() {
        return Optional.ofNullable(cap);
    }

    public IntegerFilter cap() {
        if (cap == null) {
            setCap(new IntegerFilter());
        }
        return cap;
    }

    public void setCap(IntegerFilter cap) {
        this.cap = cap;
    }

    public IntegerFilter getPrecision() {
        return precision;
    }

    public Optional<IntegerFilter> optionalPrecision() {
        return Optional.ofNullable(precision);
    }

    public IntegerFilter precision() {
        if (precision == null) {
            setPrecision(new IntegerFilter());
        }
        return precision;
    }

    public void setPrecision(IntegerFilter precision) {
        this.precision = precision;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public TrackingSourceFilter getSource() {
        return source;
    }

    public Optional<TrackingSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public TrackingSourceFilter source() {
        if (source == null) {
            setSource(new TrackingSourceFilter());
        }
        return source;
    }

    public void setSource(TrackingSourceFilter source) {
        this.source = source;
    }

    public StringFilter getEvenement() {
        return evenement;
    }

    public Optional<StringFilter> optionalEvenement() {
        return Optional.ofNullable(evenement);
    }

    public StringFilter evenement() {
        if (evenement == null) {
            setEvenement(new StringFilter());
        }
        return evenement;
    }

    public void setEvenement(StringFilter evenement) {
        this.evenement = evenement;
    }

    public LongFilter getBusId() {
        return busId;
    }

    public Optional<LongFilter> optionalBusId() {
        return Optional.ofNullable(busId);
    }

    public LongFilter busId() {
        if (busId == null) {
            setBusId(new LongFilter());
        }
        return busId;
    }

    public void setBusId(LongFilter busId) {
        this.busId = busId;
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
        final TrackingCriteria that = (TrackingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(vitesse, that.vitesse) &&
            Objects.equals(cap, that.cap) &&
            Objects.equals(precision, that.precision) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(source, that.source) &&
            Objects.equals(evenement, that.evenement) &&
            Objects.equals(busId, that.busId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, vitesse, cap, precision, timestamp, source, evenement, busId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalVitesse().map(f -> "vitesse=" + f + ", ").orElse("") +
            optionalCap().map(f -> "cap=" + f + ", ").orElse("") +
            optionalPrecision().map(f -> "precision=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalEvenement().map(f -> "evenement=" + f + ", ").orElse("") +
            optionalBusId().map(f -> "busId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
