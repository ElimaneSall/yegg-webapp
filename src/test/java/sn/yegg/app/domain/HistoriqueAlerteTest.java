package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.AlerteApprocheTestSamples.*;
import static sn.yegg.app.domain.BusTestSamples.*;
import static sn.yegg.app.domain.HistoriqueAlerteTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class HistoriqueAlerteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueAlerte.class);
        HistoriqueAlerte historiqueAlerte1 = getHistoriqueAlerteSample1();
        HistoriqueAlerte historiqueAlerte2 = new HistoriqueAlerte();
        assertThat(historiqueAlerte1).isNotEqualTo(historiqueAlerte2);

        historiqueAlerte2.setId(historiqueAlerte1.getId());
        assertThat(historiqueAlerte1).isEqualTo(historiqueAlerte2);

        historiqueAlerte2 = getHistoriqueAlerteSample2();
        assertThat(historiqueAlerte1).isNotEqualTo(historiqueAlerte2);
    }

    @Test
    void busTest() {
        HistoriqueAlerte historiqueAlerte = getHistoriqueAlerteRandomSampleGenerator();
        Bus busBack = getBusRandomSampleGenerator();

        historiqueAlerte.setBus(busBack);
        assertThat(historiqueAlerte.getBus()).isEqualTo(busBack);

        historiqueAlerte.bus(null);
        assertThat(historiqueAlerte.getBus()).isNull();
    }

    @Test
    void alerteApprocheTest() {
        HistoriqueAlerte historiqueAlerte = getHistoriqueAlerteRandomSampleGenerator();
        AlerteApproche alerteApprocheBack = getAlerteApprocheRandomSampleGenerator();

        historiqueAlerte.setAlerteApproche(alerteApprocheBack);
        assertThat(historiqueAlerte.getAlerteApproche()).isEqualTo(alerteApprocheBack);

        historiqueAlerte.alerteApproche(null);
        assertThat(historiqueAlerte.getAlerteApproche()).isNull();
    }

    @Test
    void utilisateurTest() {
        HistoriqueAlerte historiqueAlerte = getHistoriqueAlerteRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        historiqueAlerte.setUtilisateur(utilisateurBack);
        assertThat(historiqueAlerte.getUtilisateur()).isEqualTo(utilisateurBack);

        historiqueAlerte.utilisateur(null);
        assertThat(historiqueAlerte.getUtilisateur()).isNull();
    }
}
