package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArretCriteriaTest {

    @Test
    void newArretCriteriaHasAllFiltersNullTest() {
        var arretCriteria = new ArretCriteria();
        assertThat(arretCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void arretCriteriaFluentMethodsCreatesFiltersTest() {
        var arretCriteria = new ArretCriteria();

        setAllFilters(arretCriteria);

        assertThat(arretCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void arretCriteriaCopyCreatesNullFilterTest() {
        var arretCriteria = new ArretCriteria();
        var copy = arretCriteria.copy();

        assertThat(arretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(arretCriteria)
        );
    }

    @Test
    void arretCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var arretCriteria = new ArretCriteria();
        setAllFilters(arretCriteria);

        var copy = arretCriteria.copy();

        assertThat(arretCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(arretCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var arretCriteria = new ArretCriteria();

        assertThat(arretCriteria).hasToString("ArretCriteria{}");
    }

    private static void setAllFilters(ArretCriteria arretCriteria) {
        arretCriteria.id();
        arretCriteria.nom();
        arretCriteria.code();
        arretCriteria.latitude();
        arretCriteria.longitude();
        arretCriteria.altitude();
        arretCriteria.adresse();
        arretCriteria.ville();
        arretCriteria.codePostal();
        arretCriteria.zoneTarifaire();
        arretCriteria.accessiblePMR();
        arretCriteria.actif();
        arretCriteria.ligneArretsId();
        arretCriteria.distinct();
    }

    private static Condition<ArretCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getAltitude()) &&
                condition.apply(criteria.getAdresse()) &&
                condition.apply(criteria.getVille()) &&
                condition.apply(criteria.getCodePostal()) &&
                condition.apply(criteria.getZoneTarifaire()) &&
                condition.apply(criteria.getAccessiblePMR()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getLigneArretsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArretCriteria> copyFiltersAre(ArretCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getAltitude(), copy.getAltitude()) &&
                condition.apply(criteria.getAdresse(), copy.getAdresse()) &&
                condition.apply(criteria.getVille(), copy.getVille()) &&
                condition.apply(criteria.getCodePostal(), copy.getCodePostal()) &&
                condition.apply(criteria.getZoneTarifaire(), copy.getZoneTarifaire()) &&
                condition.apply(criteria.getAccessiblePMR(), copy.getAccessiblePMR()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getLigneArretsId(), copy.getLigneArretsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
