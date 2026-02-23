package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrackingCriteriaTest {

    @Test
    void newTrackingCriteriaHasAllFiltersNullTest() {
        var trackingCriteria = new TrackingCriteria();
        assertThat(trackingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trackingCriteriaFluentMethodsCreatesFiltersTest() {
        var trackingCriteria = new TrackingCriteria();

        setAllFilters(trackingCriteria);

        assertThat(trackingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trackingCriteriaCopyCreatesNullFilterTest() {
        var trackingCriteria = new TrackingCriteria();
        var copy = trackingCriteria.copy();

        assertThat(trackingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trackingCriteria)
        );
    }

    @Test
    void trackingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trackingCriteria = new TrackingCriteria();
        setAllFilters(trackingCriteria);

        var copy = trackingCriteria.copy();

        assertThat(trackingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trackingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trackingCriteria = new TrackingCriteria();

        assertThat(trackingCriteria).hasToString("TrackingCriteria{}");
    }

    private static void setAllFilters(TrackingCriteria trackingCriteria) {
        trackingCriteria.id();
        trackingCriteria.latitude();
        trackingCriteria.longitude();
        trackingCriteria.vitesse();
        trackingCriteria.cap();
        trackingCriteria.precision();
        trackingCriteria.timestamp();
        trackingCriteria.source();
        trackingCriteria.evenement();
        trackingCriteria.busId();
        trackingCriteria.distinct();
    }

    private static Condition<TrackingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getVitesse()) &&
                condition.apply(criteria.getCap()) &&
                condition.apply(criteria.getPrecision()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getEvenement()) &&
                condition.apply(criteria.getBusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrackingCriteria> copyFiltersAre(TrackingCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getVitesse(), copy.getVitesse()) &&
                condition.apply(criteria.getCap(), copy.getCap()) &&
                condition.apply(criteria.getPrecision(), copy.getPrecision()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getEvenement(), copy.getEvenement()) &&
                condition.apply(criteria.getBusId(), copy.getBusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
