package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RapportCriteriaTest {

    @Test
    void newRapportCriteriaHasAllFiltersNullTest() {
        var rapportCriteria = new RapportCriteria();
        assertThat(rapportCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void rapportCriteriaFluentMethodsCreatesFiltersTest() {
        var rapportCriteria = new RapportCriteria();

        setAllFilters(rapportCriteria);

        assertThat(rapportCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void rapportCriteriaCopyCreatesNullFilterTest() {
        var rapportCriteria = new RapportCriteria();
        var copy = rapportCriteria.copy();

        assertThat(rapportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(rapportCriteria)
        );
    }

    @Test
    void rapportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rapportCriteria = new RapportCriteria();
        setAllFilters(rapportCriteria);

        var copy = rapportCriteria.copy();

        assertThat(rapportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(rapportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rapportCriteria = new RapportCriteria();

        assertThat(rapportCriteria).hasToString("RapportCriteria{}");
    }

    private static void setAllFilters(RapportCriteria rapportCriteria) {
        rapportCriteria.id();
        rapportCriteria.nom();
        rapportCriteria.type();
        rapportCriteria.periodeDebut();
        rapportCriteria.periodeFin();
        rapportCriteria.format();
        rapportCriteria.dateGeneration();
        rapportCriteria.generePar();
        rapportCriteria.operateurId();
        rapportCriteria.adminId();
        rapportCriteria.distinct();
    }

    private static Condition<RapportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPeriodeDebut()) &&
                condition.apply(criteria.getPeriodeFin()) &&
                condition.apply(criteria.getFormat()) &&
                condition.apply(criteria.getDateGeneration()) &&
                condition.apply(criteria.getGenerePar()) &&
                condition.apply(criteria.getOperateurId()) &&
                condition.apply(criteria.getAdminId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RapportCriteria> copyFiltersAre(RapportCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPeriodeDebut(), copy.getPeriodeDebut()) &&
                condition.apply(criteria.getPeriodeFin(), copy.getPeriodeFin()) &&
                condition.apply(criteria.getFormat(), copy.getFormat()) &&
                condition.apply(criteria.getDateGeneration(), copy.getDateGeneration()) &&
                condition.apply(criteria.getGenerePar(), copy.getGenerePar()) &&
                condition.apply(criteria.getOperateurId(), copy.getOperateurId()) &&
                condition.apply(criteria.getAdminId(), copy.getAdminId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
