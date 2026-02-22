package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.FavoriTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class FavoriTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favori.class);
        Favori favori1 = getFavoriSample1();
        Favori favori2 = new Favori();
        assertThat(favori1).isNotEqualTo(favori2);

        favori2.setId(favori1.getId());
        assertThat(favori1).isEqualTo(favori2);

        favori2 = getFavoriSample2();
        assertThat(favori1).isNotEqualTo(favori2);
    }

    @Test
    void utilisateurTest() {
        Favori favori = getFavoriRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        favori.setUtilisateur(utilisateurBack);
        assertThat(favori.getUtilisateur()).isEqualTo(utilisateurBack);

        favori.utilisateur(null);
        assertThat(favori.getUtilisateur()).isNull();
    }
}
