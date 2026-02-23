package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.BusTestSamples.*;
import static sn.yegg.app.domain.LigneTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class BusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bus.class);
        Bus bus1 = getBusSample1();
        Bus bus2 = new Bus();
        assertThat(bus1).isNotEqualTo(bus2);

        bus2.setId(bus1.getId());
        assertThat(bus1).isEqualTo(bus2);

        bus2 = getBusSample2();
        assertThat(bus1).isNotEqualTo(bus2);
    }

    @Test
    void ligneTest() {
        Bus bus = getBusRandomSampleGenerator();
        Ligne ligneBack = getLigneRandomSampleGenerator();

        bus.setLigne(ligneBack);
        assertThat(bus.getLigne()).isEqualTo(ligneBack);

        bus.ligne(null);
        assertThat(bus.getLigne()).isNull();
    }

    @Test
    void chauffeurTest() {
        Bus bus = getBusRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        bus.setChauffeur(utilisateurBack);
        assertThat(bus.getChauffeur()).isEqualTo(utilisateurBack);

        bus.chauffeur(null);
        assertThat(bus.getChauffeur()).isNull();
    }
}
