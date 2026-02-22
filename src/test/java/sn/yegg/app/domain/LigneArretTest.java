package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.ArretTestSamples.*;
import static sn.yegg.app.domain.LigneArretTestSamples.*;
import static sn.yegg.app.domain.LigneTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class LigneArretTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneArret.class);
        LigneArret ligneArret1 = getLigneArretSample1();
        LigneArret ligneArret2 = new LigneArret();
        assertThat(ligneArret1).isNotEqualTo(ligneArret2);

        ligneArret2.setId(ligneArret1.getId());
        assertThat(ligneArret1).isEqualTo(ligneArret2);

        ligneArret2 = getLigneArretSample2();
        assertThat(ligneArret1).isNotEqualTo(ligneArret2);
    }

    @Test
    void ligneTest() {
        LigneArret ligneArret = getLigneArretRandomSampleGenerator();
        Ligne ligneBack = getLigneRandomSampleGenerator();

        ligneArret.setLigne(ligneBack);
        assertThat(ligneArret.getLigne()).isEqualTo(ligneBack);

        ligneArret.ligne(null);
        assertThat(ligneArret.getLigne()).isNull();
    }

    @Test
    void arretTest() {
        LigneArret ligneArret = getLigneArretRandomSampleGenerator();
        Arret arretBack = getArretRandomSampleGenerator();

        ligneArret.setArret(arretBack);
        assertThat(ligneArret.getArret()).isEqualTo(arretBack);

        ligneArret.arret(null);
        assertThat(ligneArret.getArret()).isNull();
    }
}
