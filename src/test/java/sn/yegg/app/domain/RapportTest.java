package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.OperateurTestSamples.*;
import static sn.yegg.app.domain.RapportTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class RapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rapport.class);
        Rapport rapport1 = getRapportSample1();
        Rapport rapport2 = new Rapport();
        assertThat(rapport1).isNotEqualTo(rapport2);

        rapport2.setId(rapport1.getId());
        assertThat(rapport1).isEqualTo(rapport2);

        rapport2 = getRapportSample2();
        assertThat(rapport1).isNotEqualTo(rapport2);
    }

    @Test
    void operateurTest() {
        Rapport rapport = getRapportRandomSampleGenerator();
        Operateur operateurBack = getOperateurRandomSampleGenerator();

        rapport.setOperateur(operateurBack);
        assertThat(rapport.getOperateur()).isEqualTo(operateurBack);

        rapport.operateur(null);
        assertThat(rapport.getOperateur()).isNull();
    }

    @Test
    void adminTest() {
        Rapport rapport = getRapportRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        rapport.setAdmin(utilisateurBack);
        assertThat(rapport.getAdmin()).isEqualTo(utilisateurBack);

        rapport.admin(null);
        assertThat(rapport.getAdmin()).isNull();
    }
}
