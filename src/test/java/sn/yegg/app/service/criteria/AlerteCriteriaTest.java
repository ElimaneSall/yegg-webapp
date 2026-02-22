package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlerteCriteriaTest {

    @Test
    void newAlerteCriteriaHasAllFiltersNullTest() {
        var alerteCriteria = new AlerteCriteria();
        assertThat(alerteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alerteCriteriaFluentMethodsCreatesFiltersTest() {
        var alerteCriteria = new AlerteCriteria();

        setAllFilters(alerteCriteria);

        assertThat(alerteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alerteCriteriaCopyCreatesNullFilterTest() {
        var alerteCriteria = new AlerteCriteria();
        var copy = alerteCriteria.copy();

        assertThat(alerteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteCriteria)
        );
    }

    @Test
    void alerteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alerteCriteria = new AlerteCriteria();
        setAllFilters(alerteCriteria);

        var copy = alerteCriteria.copy();

        assertThat(alerteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alerteCriteria = new AlerteCriteria();

        assertThat(alerteCriteria).hasToString("AlerteCriteria{}");
    }

    private static void setAllFilters(AlerteCriteria alerteCriteria) {
        alerteCriteria.id();
        alerteCriteria.typeCible();
        alerteCriteria.cibleId();
        alerteCriteria.seuilMinutes();
        alerteCriteria.joursActivation();
        alerteCriteria.heureDebut();
        alerteCriteria.heureFin();
        alerteCriteria.statut();
        alerteCriteria.dernierDeclenchement();
        alerteCriteria.nombreDeclenchements();
        alerteCriteria.utilisateurId();
        alerteCriteria.distinct();
    }

    private static Condition<AlerteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTypeCible()) &&
                condition.apply(criteria.getCibleId()) &&
                condition.apply(criteria.getSeuilMinutes()) &&
                condition.apply(criteria.getJoursActivation()) &&
                condition.apply(criteria.getHeureDebut()) &&
                condition.apply(criteria.getHeureFin()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDernierDeclenchement()) &&
                condition.apply(criteria.getNombreDeclenchements()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlerteCriteria> copyFiltersAre(AlerteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeCible(), copy.getTypeCible()) &&
                condition.apply(criteria.getCibleId(), copy.getCibleId()) &&
                condition.apply(criteria.getSeuilMinutes(), copy.getSeuilMinutes()) &&
                condition.apply(criteria.getJoursActivation(), copy.getJoursActivation()) &&
                condition.apply(criteria.getHeureDebut(), copy.getHeureDebut()) &&
                condition.apply(criteria.getHeureFin(), copy.getHeureFin()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDernierDeclenchement(), copy.getDernierDeclenchement()) &&
                condition.apply(criteria.getNombreDeclenchements(), copy.getNombreDeclenchements()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
