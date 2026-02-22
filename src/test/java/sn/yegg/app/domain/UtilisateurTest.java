package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.BusTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void busTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Bus busBack = getBusRandomSampleGenerator();

        utilisateur.setBus(busBack);
        assertThat(utilisateur.getBus()).isEqualTo(busBack);
        assertThat(busBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.bus(null);
        assertThat(utilisateur.getBus()).isNull();
        assertThat(busBack.getUtilisateur()).isNull();
    }
}
