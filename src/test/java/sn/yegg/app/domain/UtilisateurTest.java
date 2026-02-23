package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.FavoriTestSamples.*;
import static sn.yegg.app.domain.FeedbackTestSamples.*;
import static sn.yegg.app.domain.HistoriqueAlerteTestSamples.*;
import static sn.yegg.app.domain.NotificationTestSamples.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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
    void favorisTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Favori favoriBack = getFavoriRandomSampleGenerator();

        utilisateur.addFavoris(favoriBack);
        assertThat(utilisateur.getFavorises()).containsOnly(favoriBack);
        assertThat(favoriBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeFavoris(favoriBack);
        assertThat(utilisateur.getFavorises()).doesNotContain(favoriBack);
        assertThat(favoriBack.getUtilisateur()).isNull();

        utilisateur.favorises(new HashSet<>(Set.of(favoriBack)));
        assertThat(utilisateur.getFavorises()).containsOnly(favoriBack);
        assertThat(favoriBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setFavorises(new HashSet<>());
        assertThat(utilisateur.getFavorises()).doesNotContain(favoriBack);
        assertThat(favoriBack.getUtilisateur()).isNull();
    }

    @Test
    void notificationsTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        utilisateur.addNotifications(notificationBack);
        assertThat(utilisateur.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeNotifications(notificationBack);
        assertThat(utilisateur.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getUtilisateur()).isNull();

        utilisateur.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(utilisateur.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setNotifications(new HashSet<>());
        assertThat(utilisateur.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getUtilisateur()).isNull();
    }

    @Test
    void feedbacksTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        utilisateur.addFeedbacks(feedbackBack);
        assertThat(utilisateur.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeFeedbacks(feedbackBack);
        assertThat(utilisateur.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getUtilisateur()).isNull();

        utilisateur.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(utilisateur.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setFeedbacks(new HashSet<>());
        assertThat(utilisateur.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getUtilisateur()).isNull();
    }

    @Test
    void historiqueAlertesTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        HistoriqueAlerte historiqueAlerteBack = getHistoriqueAlerteRandomSampleGenerator();

        utilisateur.addHistoriqueAlertes(historiqueAlerteBack);
        assertThat(utilisateur.getHistoriqueAlertes()).containsOnly(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.removeHistoriqueAlertes(historiqueAlerteBack);
        assertThat(utilisateur.getHistoriqueAlertes()).doesNotContain(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getUtilisateur()).isNull();

        utilisateur.historiqueAlertes(new HashSet<>(Set.of(historiqueAlerteBack)));
        assertThat(utilisateur.getHistoriqueAlertes()).containsOnly(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.setHistoriqueAlertes(new HashSet<>());
        assertThat(utilisateur.getHistoriqueAlertes()).doesNotContain(historiqueAlerteBack);
        assertThat(historiqueAlerteBack.getUtilisateur()).isNull();
    }
}
