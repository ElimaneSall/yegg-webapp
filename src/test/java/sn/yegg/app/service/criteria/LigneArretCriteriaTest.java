package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LigneArretCriteriaTest {

    @Test
    void newLigneArretCriteriaHasAllFiltersNullTest() {
        var ligneArretCriteria = new LigneArretCriteria();
        assertThat(ligneArretCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ligneArretCriteriaFluentMethodsCreatesFiltersTest() {
        var ligneArretCriteria = new LigneArretCriteria();

        setAllFilters(ligneArretCriteria);

        assertThat(ligneArretCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ligneArretCriteriaCopyCreatesNullFilterTest() {
        var ligneArretCriteria = new LigneArretCriteria();
        var copy = ligneArretCriteria.copy();

        assertThat(ligneArretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneArretCriteria)
        );
    }

    @Test
    void ligneArretCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ligneArretCriteria = new LigneArretCriteria();
        setAllFilters(ligneArretCriteria);

        var copy = ligneArretCriteria.copy();

        assertThat(ligneArretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneArretCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ligneArretCriteria = new LigneArretCriteria();

        assertThat(ligneArretCriteria).hasToString("LigneArretCriteria{}");
    }

    private static void setAllFilters(LigneArretCriteria ligneArretCriteria) {
        ligneArretCriteria.id();
        ligneArretCriteria.ordre();
        ligneArretCriteria.tempsTrajetDepart();
        ligneArretCriteria.distanceDepart();
        ligneArretCriteria.tempsArretMoyen();
        ligneArretCriteria.arretPhysique();
        ligneArretCriteria.ligneId();
        ligneArretCriteria.arretId();
        ligneArretCriteria.distinct();
    }

    private static Condition<LigneArretCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrdre()) &&
                condition.apply(criteria.getTempsTrajetDepart()) &&
                condition.apply(criteria.getDistanceDepart()) &&
                condition.apply(criteria.getTempsArretMoyen()) &&
                condition.apply(criteria.getArretPhysique()) &&
                condition.apply(criteria.getLigneId()) &&
                condition.apply(criteria.getArretId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LigneArretCriteria> copyFiltersAre(LigneArretCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrdre(), copy.getOrdre()) &&
                condition.apply(criteria.getTempsTrajetDepart(), copy.getTempsTrajetDepart()) &&
                condition.apply(criteria.getDistanceDepart(), copy.getDistanceDepart()) &&
                condition.apply(criteria.getTempsArretMoyen(), copy.getTempsArretMoyen()) &&
                condition.apply(criteria.getArretPhysique(), copy.getArretPhysique()) &&
                condition.apply(criteria.getLigneId(), copy.getLigneId()) &&
                condition.apply(criteria.getArretId(), copy.getArretId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
