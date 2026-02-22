package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.LigneArretTestSamples.*;
import static sn.yegg.app.domain.LigneTestSamples.*;
import static sn.yegg.app.domain.OperateurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class LigneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ligne.class);
        Ligne ligne1 = getLigneSample1();
        Ligne ligne2 = new Ligne();
        assertThat(ligne1).isNotEqualTo(ligne2);

        ligne2.setId(ligne1.getId());
        assertThat(ligne1).isEqualTo(ligne2);

        ligne2 = getLigneSample2();
        assertThat(ligne1).isNotEqualTo(ligne2);
    }

    @Test
    void ligneArretsTest() {
        Ligne ligne = getLigneRandomSampleGenerator();
        LigneArret ligneArretBack = getLigneArretRandomSampleGenerator();

        ligne.addLigneArrets(ligneArretBack);
        assertThat(ligne.getLigneArrets()).containsOnly(ligneArretBack);
        assertThat(ligneArretBack.getLigne()).isEqualTo(ligne);

        ligne.removeLigneArrets(ligneArretBack);
        assertThat(ligne.getLigneArrets()).doesNotContain(ligneArretBack);
        assertThat(ligneArretBack.getLigne()).isNull();

        ligne.ligneArrets(new HashSet<>(Set.of(ligneArretBack)));
        assertThat(ligne.getLigneArrets()).containsOnly(ligneArretBack);
        assertThat(ligneArretBack.getLigne()).isEqualTo(ligne);

        ligne.setLigneArrets(new HashSet<>());
        assertThat(ligne.getLigneArrets()).doesNotContain(ligneArretBack);
        assertThat(ligneArretBack.getLigne()).isNull();
    }

    @Test
    void operateurTest() {
        Ligne ligne = getLigneRandomSampleGenerator();
        Operateur operateurBack = getOperateurRandomSampleGenerator();

        ligne.setOperateur(operateurBack);
        assertThat(ligne.getOperateur()).isEqualTo(operateurBack);

        ligne.operateur(null);
        assertThat(ligne.getOperateur()).isNull();
    }
}
