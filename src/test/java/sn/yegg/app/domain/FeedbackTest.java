package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.FeedbackTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void utilisateurTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        feedback.setUtilisateur(utilisateurBack);
        assertThat(feedback.getUtilisateur()).isEqualTo(utilisateurBack);

        feedback.utilisateur(null);
        assertThat(feedback.getUtilisateur()).isNull();
    }
}
