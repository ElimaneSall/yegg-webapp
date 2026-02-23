package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoriqueAlerteCriteriaTest {

    @Test
    void newHistoriqueAlerteCriteriaHasAllFiltersNullTest() {
        var historiqueAlerteCriteria = new HistoriqueAlerteCriteria();
        assertThat(historiqueAlerteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historiqueAlerteCriteriaFluentMethodsCreatesFiltersTest() {
        var historiqueAlerteCriteria = new HistoriqueAlerteCriteria();

        setAllFilters(historiqueAlerteCriteria);

        assertThat(historiqueAlerteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historiqueAlerteCriteriaCopyCreatesNullFilterTest() {
        var historiqueAlerteCriteria = new HistoriqueAlerteCriteria();
        var copy = historiqueAlerteCriteria.copy();

        assertThat(historiqueAlerteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueAlerteCriteria)
        );
    }

    @Test
    void historiqueAlerteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historiqueAlerteCriteria = new HistoriqueAlerteCriteria();
        setAllFilters(historiqueAlerteCriteria);

        var copy = historiqueAlerteCriteria.copy();

        assertThat(historiqueAlerteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueAlerteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historiqueAlerteCriteria = new HistoriqueAlerteCriteria();

        assertThat(historiqueAlerteCriteria).hasToString("HistoriqueAlerteCriteria{}");
    }

    private static void setAllFilters(HistoriqueAlerteCriteria historiqueAlerteCriteria) {
        historiqueAlerteCriteria.id();
        historiqueAlerteCriteria.dateDeclenchement();
        historiqueAlerteCriteria.busNumero();
        historiqueAlerteCriteria.distanceReelle();
        historiqueAlerteCriteria.tempsReel();
        historiqueAlerteCriteria.typeDeclenchement();
        historiqueAlerteCriteria.notificationEnvoyee();
        historiqueAlerteCriteria.dateLecture();
        historiqueAlerteCriteria.busId();
        historiqueAlerteCriteria.alerteApprocheId();
        historiqueAlerteCriteria.utilisateurId();
        historiqueAlerteCriteria.distinct();
    }

    private static Condition<HistoriqueAlerteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDeclenchement()) &&
                condition.apply(criteria.getBusNumero()) &&
                condition.apply(criteria.getDistanceReelle()) &&
                condition.apply(criteria.getTempsReel()) &&
                condition.apply(criteria.getTypeDeclenchement()) &&
                condition.apply(criteria.getNotificationEnvoyee()) &&
                condition.apply(criteria.getDateLecture()) &&
                condition.apply(criteria.getBusId()) &&
                condition.apply(criteria.getAlerteApprocheId()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoriqueAlerteCriteria> copyFiltersAre(
        HistoriqueAlerteCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDeclenchement(), copy.getDateDeclenchement()) &&
                condition.apply(criteria.getBusNumero(), copy.getBusNumero()) &&
                condition.apply(criteria.getDistanceReelle(), copy.getDistanceReelle()) &&
                condition.apply(criteria.getTempsReel(), copy.getTempsReel()) &&
                condition.apply(criteria.getTypeDeclenchement(), copy.getTypeDeclenchement()) &&
                condition.apply(criteria.getNotificationEnvoyee(), copy.getNotificationEnvoyee()) &&
                condition.apply(criteria.getDateLecture(), copy.getDateLecture()) &&
                condition.apply(criteria.getBusId(), copy.getBusId()) &&
                condition.apply(criteria.getAlerteApprocheId(), copy.getAlerteApprocheId()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
