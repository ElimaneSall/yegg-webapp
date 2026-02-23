package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.AlerteApprocheTestSamples.*;
import static sn.yegg.app.domain.AlerteLigneArretTestSamples.*;
import static sn.yegg.app.domain.ArretTestSamples.*;
import static sn.yegg.app.domain.LigneTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteLigneArretTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteLigneArret.class);
        AlerteLigneArret alerteLigneArret1 = getAlerteLigneArretSample1();
        AlerteLigneArret alerteLigneArret2 = new AlerteLigneArret();
        assertThat(alerteLigneArret1).isNotEqualTo(alerteLigneArret2);

        alerteLigneArret2.setId(alerteLigneArret1.getId());
        assertThat(alerteLigneArret1).isEqualTo(alerteLigneArret2);

        alerteLigneArret2 = getAlerteLigneArretSample2();
        assertThat(alerteLigneArret1).isNotEqualTo(alerteLigneArret2);
    }

    @Test
    void ligneTest() {
        AlerteLigneArret alerteLigneArret = getAlerteLigneArretRandomSampleGenerator();
        Ligne ligneBack = getLigneRandomSampleGenerator();

        alerteLigneArret.setLigne(ligneBack);
        assertThat(alerteLigneArret.getLigne()).isEqualTo(ligneBack);

        alerteLigneArret.ligne(null);
        assertThat(alerteLigneArret.getLigne()).isNull();
    }

    @Test
    void arretTest() {
        AlerteLigneArret alerteLigneArret = getAlerteLigneArretRandomSampleGenerator();
        Arret arretBack = getArretRandomSampleGenerator();

        alerteLigneArret.setArret(arretBack);
        assertThat(alerteLigneArret.getArret()).isEqualTo(arretBack);

        alerteLigneArret.arret(null);
        assertThat(alerteLigneArret.getArret()).isNull();
    }

    @Test
    void alerteApprocheTest() {
        AlerteLigneArret alerteLigneArret = getAlerteLigneArretRandomSampleGenerator();
        AlerteApproche alerteApprocheBack = getAlerteApprocheRandomSampleGenerator();

        alerteLigneArret.setAlerteApproche(alerteApprocheBack);
        assertThat(alerteLigneArret.getAlerteApproche()).isEqualTo(alerteApprocheBack);

        alerteLigneArret.alerteApproche(null);
        assertThat(alerteLigneArret.getAlerteApproche()).isNull();
    }
}
