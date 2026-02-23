package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UtilisateurCriteriaTest {

    @Test
    void newUtilisateurCriteriaHasAllFiltersNullTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        assertThat(utilisateurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void utilisateurCriteriaFluentMethodsCreatesFiltersTest() {
        var utilisateurCriteria = new UtilisateurCriteria();

        setAllFilters(utilisateurCriteria);

        assertThat(utilisateurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void utilisateurCriteriaCopyCreatesNullFilterTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        var copy = utilisateurCriteria.copy();

        assertThat(utilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(utilisateurCriteria)
        );
    }

    @Test
    void utilisateurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        setAllFilters(utilisateurCriteria);

        var copy = utilisateurCriteria.copy();

        assertThat(utilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(utilisateurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var utilisateurCriteria = new UtilisateurCriteria();

        assertThat(utilisateurCriteria).hasToString("UtilisateurCriteria{}");
    }

    private static void setAllFilters(UtilisateurCriteria utilisateurCriteria) {
        utilisateurCriteria.id();
        utilisateurCriteria.prenom();
        utilisateurCriteria.nom();
        utilisateurCriteria.email();
        utilisateurCriteria.telephone();
        utilisateurCriteria.motDePasse();
        utilisateurCriteria.role();
        utilisateurCriteria.matricule();
        utilisateurCriteria.fcmToken();
        utilisateurCriteria.notificationsPush();
        utilisateurCriteria.notificationsSms();
        utilisateurCriteria.langue();
        utilisateurCriteria.dateCreation();
        utilisateurCriteria.derniereConnexion();
        utilisateurCriteria.actif();
        utilisateurCriteria.dateEmbauche();
        utilisateurCriteria.numeroPermis();
        utilisateurCriteria.favorisId();
        utilisateurCriteria.notificationsId();
        utilisateurCriteria.feedbacksId();
        utilisateurCriteria.historiqueAlertesId();
        utilisateurCriteria.distinct();
    }

    private static Condition<UtilisateurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getMotDePasse()) &&
                condition.apply(criteria.getRole()) &&
                condition.apply(criteria.getMatricule()) &&
                condition.apply(criteria.getFcmToken()) &&
                condition.apply(criteria.getNotificationsPush()) &&
                condition.apply(criteria.getNotificationsSms()) &&
                condition.apply(criteria.getLangue()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDerniereConnexion()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getDateEmbauche()) &&
                condition.apply(criteria.getNumeroPermis()) &&
                condition.apply(criteria.getFavorisId()) &&
                condition.apply(criteria.getNotificationsId()) &&
                condition.apply(criteria.getFeedbacksId()) &&
                condition.apply(criteria.getHistoriqueAlertesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UtilisateurCriteria> copyFiltersAre(UtilisateurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getMotDePasse(), copy.getMotDePasse()) &&
                condition.apply(criteria.getRole(), copy.getRole()) &&
                condition.apply(criteria.getMatricule(), copy.getMatricule()) &&
                condition.apply(criteria.getFcmToken(), copy.getFcmToken()) &&
                condition.apply(criteria.getNotificationsPush(), copy.getNotificationsPush()) &&
                condition.apply(criteria.getNotificationsSms(), copy.getNotificationsSms()) &&
                condition.apply(criteria.getLangue(), copy.getLangue()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDerniereConnexion(), copy.getDerniereConnexion()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getDateEmbauche(), copy.getDateEmbauche()) &&
                condition.apply(criteria.getNumeroPermis(), copy.getNumeroPermis()) &&
                condition.apply(criteria.getFavorisId(), copy.getFavorisId()) &&
                condition.apply(criteria.getNotificationsId(), copy.getNotificationsId()) &&
                condition.apply(criteria.getFeedbacksId(), copy.getFeedbacksId()) &&
                condition.apply(criteria.getHistoriqueAlertesId(), copy.getHistoriqueAlertesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
