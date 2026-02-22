package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.NotificationTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void utilisateurTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        notification.setUtilisateur(utilisateurBack);
        assertThat(notification.getUtilisateur()).isEqualTo(utilisateurBack);

        notification.utilisateur(null);
        assertThat(notification.getUtilisateur()).isNull();
    }
}
