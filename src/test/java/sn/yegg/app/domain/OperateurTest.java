package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.LigneTestSamples.*;
import static sn.yegg.app.domain.OperateurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class OperateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operateur.class);
        Operateur operateur1 = getOperateurSample1();
        Operateur operateur2 = new Operateur();
        assertThat(operateur1).isNotEqualTo(operateur2);

        operateur2.setId(operateur1.getId());
        assertThat(operateur1).isEqualTo(operateur2);

        operateur2 = getOperateurSample2();
        assertThat(operateur1).isNotEqualTo(operateur2);
    }

    @Test
    void lignesTest() {
        Operateur operateur = getOperateurRandomSampleGenerator();
        Ligne ligneBack = getLigneRandomSampleGenerator();

        operateur.addLignes(ligneBack);
        assertThat(operateur.getLignes()).containsOnly(ligneBack);
        assertThat(ligneBack.getOperateur()).isEqualTo(operateur);

        operateur.removeLignes(ligneBack);
        assertThat(operateur.getLignes()).doesNotContain(ligneBack);
        assertThat(ligneBack.getOperateur()).isNull();

        operateur.lignes(new HashSet<>(Set.of(ligneBack)));
        assertThat(operateur.getLignes()).containsOnly(ligneBack);
        assertThat(ligneBack.getOperateur()).isEqualTo(operateur);

        operateur.setLignes(new HashSet<>());
        assertThat(operateur.getLignes()).doesNotContain(ligneBack);
        assertThat(ligneBack.getOperateur()).isNull();
    }
}
