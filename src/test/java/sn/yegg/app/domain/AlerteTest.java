package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.AlerteTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alerte.class);
        Alerte alerte1 = getAlerteSample1();
        Alerte alerte2 = new Alerte();
        assertThat(alerte1).isNotEqualTo(alerte2);

        alerte2.setId(alerte1.getId());
        assertThat(alerte1).isEqualTo(alerte2);

        alerte2 = getAlerteSample2();
        assertThat(alerte1).isNotEqualTo(alerte2);
    }

    @Test
    void utilisateurTest() {
        Alerte alerte = getAlerteRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        alerte.setUtilisateur(utilisateurBack);
        assertThat(alerte.getUtilisateur()).isEqualTo(utilisateurBack);

        alerte.utilisateur(null);
        assertThat(alerte.getUtilisateur()).isNull();
    }
}
