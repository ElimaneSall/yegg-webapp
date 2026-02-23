package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OperateurCriteriaTest {

    @Test
    void newOperateurCriteriaHasAllFiltersNullTest() {
        var operateurCriteria = new OperateurCriteria();
        assertThat(operateurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void operateurCriteriaFluentMethodsCreatesFiltersTest() {
        var operateurCriteria = new OperateurCriteria();

        setAllFilters(operateurCriteria);

        assertThat(operateurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void operateurCriteriaCopyCreatesNullFilterTest() {
        var operateurCriteria = new OperateurCriteria();
        var copy = operateurCriteria.copy();

        assertThat(operateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(operateurCriteria)
        );
    }

    @Test
    void operateurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var operateurCriteria = new OperateurCriteria();
        setAllFilters(operateurCriteria);

        var copy = operateurCriteria.copy();

        assertThat(operateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(operateurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var operateurCriteria = new OperateurCriteria();

        assertThat(operateurCriteria).hasToString("OperateurCriteria{}");
    }

    private static void setAllFilters(OperateurCriteria operateurCriteria) {
        operateurCriteria.id();
        operateurCriteria.nom();
        operateurCriteria.email();
        operateurCriteria.telephone();
        operateurCriteria.siteWeb();
        operateurCriteria.siret();
        operateurCriteria.dateCreation();
        operateurCriteria.actif();
        operateurCriteria.lignesId();
        operateurCriteria.distinct();
    }

    private static Condition<OperateurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getSiteWeb()) &&
                condition.apply(criteria.getSiret()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getLignesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OperateurCriteria> copyFiltersAre(OperateurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getSiteWeb(), copy.getSiteWeb()) &&
                condition.apply(criteria.getSiret(), copy.getSiret()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getLignesId(), copy.getLignesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
