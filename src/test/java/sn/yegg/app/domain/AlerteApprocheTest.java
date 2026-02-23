package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.AlerteApprocheTestSamples.*;
import static sn.yegg.app.domain.AlerteLigneArretTestSamples.*;
import static sn.yegg.app.domain.HistoriqueAlerteTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteApprocheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteApproche.class);
        AlerteApproche alerteApproche1 = getAlerteApprocheSample1();
        AlerteApproche alerteApproche2 = new AlerteApproche();
        assertThat(alerteApproche1).isNotEqualTo(alerteApproche2);

        alerteApproche2.setId(alerteApproche1.getId());
        assertThat(alerteApproche1).isEqualTo(alerteApproche2);

        alerteApproche2 = getAlerteApprocheSample2();
        assertThat(alerteApproche1).isNotEqualTo(alerteApproche2);
    }

    @Test
    void alerteLigneArretTest() {
        AlerteApproche alerteApproche = getAlerteApprocheRandomSampleGenerator();
        AlerteLigneArret alerteLigneArretBack = getAlerteLigneArretRandomSampleGenerator();

        alerteApproche.addAlerteLigneArret(alerteLigneArretBack);
        assertThat(alerteApproche.getAlerteLigneArrets()).containsOnly(alerteLigneArretBack);
        assertThat(alerteLigneArretBack.getAlerteApproche()).isEqualTo(alerteApproche);

        alerteApproche.removeAlerteLigneArret(alerteLigneArretBack);
        assertThat(alerteApproche.getAlerteLigneArrets()).doesNotContain(alerteLigneArretBack);
        assertThat(alerteLigneArretBack.getAlerteApproche()).isNull();

        alerteApproche.alerteLigneArrets(new HashSet<>(Set.of(alerteLigneArretBack)));
        assertThat(alerteApproche.getAlerteLigneArrets()).containsOnly(alerteLigneArretBack);
        assertThat(alerteLigneArretBack.getAlerteApproche()).isEqualTo(alerteApproche);

        alerteApproche.setAlerteLigneArrets(new HashSet<>());
        assertThat(alerteApproche.getAlerteLigneArrets()).doesNotContain(alerteLigneArretBack);
        assertThat(alerteLigneArretBack.getAlerteApproche()).isNull();
    }

    @Test
    void historiqueAlertesTest() {
        AlerteApproche alerteApproche = getAlerteApprocheRandomSampleGenerator();
        HistoriqueAlerte historiqueAlerteBack = getHistoriqueAlerteRandomSampleGenerator();

        alerteApproche.addHistoriqueAlertes(historiqueAlerteBack);
        assertThat(alerteApproche.getHistoriqueAlertes()).containsOnly(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getAlerteApproche()).isEqualTo(alerteApproche);

        alerteApproche.removeHistoriqueAlertes(historiqueAlerteBack);
        assertThat(alerteApproche.getHistoriqueAlertes()).doesNotContain(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getAlerteApproche()).isNull();

        alerteApproche.historiqueAlertes(new HashSet<>(Set.of(historiqueAlerteBack)));
        assertThat(alerteApproche.getHistoriqueAlertes()).containsOnly(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getAlerteApproche()).isEqualTo(alerteApproche);

        alerteApproche.setHistoriqueAlertes(new HashSet<>());
        assertThat(alerteApproche.getHistoriqueAlertes()).doesNotContain(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getAlerteApproche()).isNull();
    }

    @Test
    void utilisateurTest() {
        AlerteApproche alerteApproche = getAlerteApprocheRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        alerteApproche.setUtilisateur(utilisateurBack);
        assertThat(alerteApproche.getUtilisateur()).isEqualTo(utilisateurBack);

        alerteApproche.utilisateur(null);
        assertThat(alerteApproche.getUtilisateur()).isNull();
    }
}
