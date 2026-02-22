package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LigneCriteriaTest {

    @Test
    void newLigneCriteriaHasAllFiltersNullTest() {
        var ligneCriteria = new LigneCriteria();
        assertThat(ligneCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ligneCriteriaFluentMethodsCreatesFiltersTest() {
        var ligneCriteria = new LigneCriteria();

        setAllFilters(ligneCriteria);

        assertThat(ligneCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ligneCriteriaCopyCreatesNullFilterTest() {
        var ligneCriteria = new LigneCriteria();
        var copy = ligneCriteria.copy();

        assertThat(ligneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneCriteria)
        );
    }

    @Test
    void ligneCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ligneCriteria = new LigneCriteria();
        setAllFilters(ligneCriteria);

        var copy = ligneCriteria.copy();

        assertThat(ligneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ligneCriteria = new LigneCriteria();

        assertThat(ligneCriteria).hasToString("LigneCriteria{}");
    }

    private static void setAllFilters(LigneCriteria ligneCriteria) {
        ligneCriteria.id();
        ligneCriteria.numero();
        ligneCriteria.nom();
        ligneCriteria.direction();
        ligneCriteria.couleur();
        ligneCriteria.distanceKm();
        ligneCriteria.dureeMoyenne();
        ligneCriteria.statut();
        ligneCriteria.ligneArretsId();
        ligneCriteria.operateurId();
        ligneCriteria.distinct();
    }

    private static Condition<LigneCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDirection()) &&
                condition.apply(criteria.getCouleur()) &&
                condition.apply(criteria.getDistanceKm()) &&
                condition.apply(criteria.getDureeMoyenne()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getLigneArretsId()) &&
                condition.apply(criteria.getOperateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LigneCriteria> copyFiltersAre(LigneCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDirection(), copy.getDirection()) &&
                condition.apply(criteria.getCouleur(), copy.getCouleur()) &&
                condition.apply(criteria.getDistanceKm(), copy.getDistanceKm()) &&
                condition.apply(criteria.getDureeMoyenne(), copy.getDureeMoyenne()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getLigneArretsId(), copy.getLigneArretsId()) &&
                condition.apply(criteria.getOperateurId(), copy.getOperateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
