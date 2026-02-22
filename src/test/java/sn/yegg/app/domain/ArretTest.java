package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.ArretTestSamples.*;
import static sn.yegg.app.domain.LigneArretTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class ArretTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arret.class);
        Arret arret1 = getArretSample1();
        Arret arret2 = new Arret();
        assertThat(arret1).isNotEqualTo(arret2);

        arret2.setId(arret1.getId());
        assertThat(arret1).isEqualTo(arret2);

        arret2 = getArretSample2();
        assertThat(arret1).isNotEqualTo(arret2);
    }

    @Test
    void ligneArretsTest() {
        Arret arret = getArretRandomSampleGenerator();
        LigneArret ligneArretBack = getLigneArretRandomSampleGenerator();

        arret.addLigneArrets(ligneArretBack);
        assertThat(arret.getLigneArrets()).containsOnly(ligneArretBack);
        assertThat(ligneArretBack.getArret()).isEqualTo(arret);

        arret.removeLigneArrets(ligneArretBack);
        assertThat(arret.getLigneArrets()).doesNotContain(ligneArretBack);
        assertThat(ligneArretBack.getArret()).isNull();

        arret.ligneArrets(new HashSet<>(Set.of(ligneArretBack)));
        assertThat(arret.getLigneArrets()).containsOnly(ligneArretBack);
        assertThat(ligneArretBack.getArret()).isEqualTo(arret);

        arret.setLigneArrets(new HashSet<>());
        assertThat(arret.getLigneArrets()).doesNotContain(ligneArretBack);
        assertThat(ligneArretBack.getArret()).isNull();
    }
}
