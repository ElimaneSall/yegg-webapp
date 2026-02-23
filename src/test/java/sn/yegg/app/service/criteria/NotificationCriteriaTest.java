package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationCriteriaTest {

    @Test
    void newNotificationCriteriaHasAllFiltersNullTest() {
        var notificationCriteria = new NotificationCriteria();
        assertThat(notificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationCriteria = new NotificationCriteria();

        setAllFilters(notificationCriteria);

        assertThat(notificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationCriteriaCopyCreatesNullFilterTest() {
        var notificationCriteria = new NotificationCriteria();
        var copy = notificationCriteria.copy();

        assertThat(notificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationCriteria)
        );
    }

    @Test
    void notificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationCriteria = new NotificationCriteria();
        setAllFilters(notificationCriteria);

        var copy = notificationCriteria.copy();

        assertThat(notificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationCriteria = new NotificationCriteria();

        assertThat(notificationCriteria).hasToString("NotificationCriteria{}");
    }

    private static void setAllFilters(NotificationCriteria notificationCriteria) {
        notificationCriteria.id();
        notificationCriteria.type();
        notificationCriteria.titre();
        notificationCriteria.priorite();
        notificationCriteria.statut();
        notificationCriteria.dateCreation();
        notificationCriteria.dateEnvoi();
        notificationCriteria.lu();
        notificationCriteria.dateLecture();
        notificationCriteria.utilisateurId();
        notificationCriteria.distinct();
    }

    private static Condition<NotificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getTitre()) &&
                condition.apply(criteria.getPriorite()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDateEnvoi()) &&
                condition.apply(criteria.getLu()) &&
                condition.apply(criteria.getDateLecture()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationCriteria> copyFiltersAre(
        NotificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getTitre(), copy.getTitre()) &&
                condition.apply(criteria.getPriorite(), copy.getPriorite()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDateEnvoi(), copy.getDateEnvoi()) &&
                condition.apply(criteria.getLu(), copy.getLu()) &&
                condition.apply(criteria.getDateLecture(), copy.getDateLecture()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
