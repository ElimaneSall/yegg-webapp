package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlerteApprocheCriteriaTest {

    @Test
    void newAlerteApprocheCriteriaHasAllFiltersNullTest() {
        var alerteApprocheCriteria = new AlerteApprocheCriteria();
        assertThat(alerteApprocheCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alerteApprocheCriteriaFluentMethodsCreatesFiltersTest() {
        var alerteApprocheCriteria = new AlerteApprocheCriteria();

        setAllFilters(alerteApprocheCriteria);

        assertThat(alerteApprocheCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alerteApprocheCriteriaCopyCreatesNullFilterTest() {
        var alerteApprocheCriteria = new AlerteApprocheCriteria();
        var copy = alerteApprocheCriteria.copy();

        assertThat(alerteApprocheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteApprocheCriteria)
        );
    }

    @Test
    void alerteApprocheCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alerteApprocheCriteria = new AlerteApprocheCriteria();
        setAllFilters(alerteApprocheCriteria);

        var copy = alerteApprocheCriteria.copy();

        assertThat(alerteApprocheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alerteApprocheCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alerteApprocheCriteria = new AlerteApprocheCriteria();

        assertThat(alerteApprocheCriteria).hasToString("AlerteApprocheCriteria{}");
    }

    private static void setAllFilters(AlerteApprocheCriteria alerteApprocheCriteria) {
        alerteApprocheCriteria.id();
        alerteApprocheCriteria.nom();
        alerteApprocheCriteria.seuilDistance();
        alerteApprocheCriteria.seuilTemps();
        alerteApprocheCriteria.typeSeuil();
        alerteApprocheCriteria.joursActivation();
        alerteApprocheCriteria.heureDebut();
        alerteApprocheCriteria.heureFin();
        alerteApprocheCriteria.statut();
        alerteApprocheCriteria.dateCreation();
        alerteApprocheCriteria.dateModification();
        alerteApprocheCriteria.dernierDeclenchement();
        alerteApprocheCriteria.nombreDeclenchements();
        alerteApprocheCriteria.alerteLigneArretId();
        alerteApprocheCriteria.historiqueAlertesId();
        alerteApprocheCriteria.utilisateurId();
        alerteApprocheCriteria.distinct();
    }

    private static Condition<AlerteApprocheCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getSeuilDistance()) &&
                condition.apply(criteria.getSeuilTemps()) &&
                condition.apply(criteria.getTypeSeuil()) &&
                condition.apply(criteria.getJoursActivation()) &&
                condition.apply(criteria.getHeureDebut()) &&
                condition.apply(criteria.getHeureFin()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDateModification()) &&
                condition.apply(criteria.getDernierDeclenchement()) &&
                condition.apply(criteria.getNombreDeclenchements()) &&
                condition.apply(criteria.getAlerteLigneArretId()) &&
                condition.apply(criteria.getHistoriqueAlertesId()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlerteApprocheCriteria> copyFiltersAre(
        AlerteApprocheCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getSeuilDistance(), copy.getSeuilDistance()) &&
                condition.apply(criteria.getSeuilTemps(), copy.getSeuilTemps()) &&
                condition.apply(criteria.getTypeSeuil(), copy.getTypeSeuil()) &&
                condition.apply(criteria.getJoursActivation(), copy.getJoursActivation()) &&
                condition.apply(criteria.getHeureDebut(), copy.getHeureDebut()) &&
                condition.apply(criteria.getHeureFin(), copy.getHeureFin()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDateModification(), copy.getDateModification()) &&
                condition.apply(criteria.getDernierDeclenchement(), copy.getDernierDeclenchement()) &&
                condition.apply(criteria.getNombreDeclenchements(), copy.getNombreDeclenchements()) &&
                condition.apply(criteria.getAlerteLigneArretId(), copy.getAlerteLigneArretId()) &&
                condition.apply(criteria.getHistoriqueAlertesId(), copy.getHistoriqueAlertesId()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
