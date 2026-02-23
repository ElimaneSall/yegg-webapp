package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlerteLigneArretCriteriaTest {

    @Test
    void newAlerteLigneArretCriteriaHasAllFiltersNullTest() {
        var alerteLigneArretCriteria = new AlerteLigneArretCriteria();
        assertThat(alerteLigneArretCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alerteLigneArretCriteriaFluentMethodsCreatesFiltersTest() {
        var alerteLigneArretCriteria = new AlerteLigneArretCriteria();

        setAllFilters(alerteLigneArretCriteria);

        assertThat(alerteLigneArretCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alerteLigneArretCriteriaCopyCreatesNullFilterTest() {
        var alerteLigneArretCriteria = new AlerteLigneArretCriteria();
        var copy = alerteLigneArretCriteria.copy();

        assertThat(alerteLigneArretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteLigneArretCriteria)
        );
    }

    @Test
    void alerteLigneArretCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alerteLigneArretCriteria = new AlerteLigneArretCriteria();
        setAllFilters(alerteLigneArretCriteria);

        var copy = alerteLigneArretCriteria.copy();

        assertThat(alerteLigneArretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteLigneArretCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alerteLigneArretCriteria = new AlerteLigneArretCriteria();

        assertThat(alerteLigneArretCriteria).hasToString("AlerteLigneArretCriteria{}");
    }

    private static void setAllFilters(AlerteLigneArretCriteria alerteLigneArretCriteria) {
        alerteLigneArretCriteria.id();
        alerteLigneArretCriteria.sens();
        alerteLigneArretCriteria.actif();
        alerteLigneArretCriteria.ligneId();
        alerteLigneArretCriteria.arretId();
        alerteLigneArretCriteria.alerteApprocheId();
        alerteLigneArretCriteria.distinct();
    }

    private static Condition<AlerteLigneArretCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSens()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getLigneId()) &&
                condition.apply(criteria.getArretId()) &&
                condition.apply(criteria.getAlerteApprocheId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlerteLigneArretCriteria> copyFiltersAre(
        AlerteLigneArretCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSens(), copy.getSens()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getLigneId(), copy.getLigneId()) &&
                condition.apply(criteria.getArretId(), copy.getArretId()) &&
                condition.apply(criteria.getAlerteApprocheId(), copy.getAlerteApprocheId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
