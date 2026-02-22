package sn.yegg.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BusCriteriaTest {

    @Test
    void newBusCriteriaHasAllFiltersNullTest() {
        var busCriteria = new BusCriteria();
        assertThat(busCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void busCriteriaFluentMethodsCreatesFiltersTest() {
        var busCriteria = new BusCriteria();

        setAllFilters(busCriteria);

        assertThat(busCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void busCriteriaCopyCreatesNullFilterTest() {
        var busCriteria = new BusCriteria();
        var copy = busCriteria.copy();

        assertThat(busCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(busCriteria)
        );
    }

    @Test
    void busCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var busCriteria = new BusCriteria();
        setAllFilters(busCriteria);

        var copy = busCriteria.copy();

        assertThat(busCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(busCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var busCriteria = new BusCriteria();

        assertThat(busCriteria).hasToString("BusCriteria{}");
    }

    private static void setAllFilters(BusCriteria busCriteria) {
        busCriteria.id();
        busCriteria.numeroVehicule();
        busCriteria.plaque();
        busCriteria.modele();
        busCriteria.capacite();
        busCriteria.anneeFabrication();
        busCriteria.gpsDeviceId();
        busCriteria.gpsStatus();
        busCriteria.gpsLastPing();
        busCriteria.gpsBatteryLevel();
        busCriteria.currentLatitude();
        busCriteria.currentLongitude();
        busCriteria.currentVitesse();
        busCriteria.currentCap();
        busCriteria.positionUpdatedAt();
        busCriteria.statut();
        busCriteria.utilisateurId();
        busCriteria.ligneId();
        busCriteria.distinct();
    }

    private static Condition<BusCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroVehicule()) &&
                condition.apply(criteria.getPlaque()) &&
                condition.apply(criteria.getModele()) &&
                condition.apply(criteria.getCapacite()) &&
                condition.apply(criteria.getAnneeFabrication()) &&
                condition.apply(criteria.getGpsDeviceId()) &&
                condition.apply(criteria.getGpsStatus()) &&
                condition.apply(criteria.getGpsLastPing()) &&
                condition.apply(criteria.getGpsBatteryLevel()) &&
                condition.apply(criteria.getCurrentLatitude()) &&
                condition.apply(criteria.getCurrentLongitude()) &&
                condition.apply(criteria.getCurrentVitesse()) &&
                condition.apply(criteria.getCurrentCap()) &&
                condition.apply(criteria.getPositionUpdatedAt()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getLigneId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BusCriteria> copyFiltersAre(BusCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroVehicule(), copy.getNumeroVehicule()) &&
                condition.apply(criteria.getPlaque(), copy.getPlaque()) &&
                condition.apply(criteria.getModele(), copy.getModele()) &&
                condition.apply(criteria.getCapacite(), copy.getCapacite()) &&
                condition.apply(criteria.getAnneeFabrication(), copy.getAnneeFabrication()) &&
                condition.apply(criteria.getGpsDeviceId(), copy.getGpsDeviceId()) &&
                condition.apply(criteria.getGpsStatus(), copy.getGpsStatus()) &&
                condition.apply(criteria.getGpsLastPing(), copy.getGpsLastPing()) &&
                condition.apply(criteria.getGpsBatteryLevel(), copy.getGpsBatteryLevel()) &&
                condition.apply(criteria.getCurrentLatitude(), copy.getCurrentLatitude()) &&
                condition.apply(criteria.getCurrentLongitude(), copy.getCurrentLongitude()) &&
                condition.apply(criteria.getCurrentVitesse(), copy.getCurrentVitesse()) &&
                condition.apply(criteria.getCurrentCap(), copy.getCurrentCap()) &&
                condition.apply(criteria.getPositionUpdatedAt(), copy.getPositionUpdatedAt()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getLigneId(), copy.getLigneId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
