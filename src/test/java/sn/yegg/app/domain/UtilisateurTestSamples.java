package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utilisateur getUtilisateurSample1() {
        return new Utilisateur()
            .id(1L)
            .prenom("prenom1")
            .nom("nom1")
            .email("email1")
            .telephone("telephone1")
            .motDePasse("motDePasse1")
            .matricule("matricule1")
            .fcmToken("fcmToken1")
            .langue("langue1")
            .numeroPermis("numeroPermis1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur()
            .id(2L)
            .prenom("prenom2")
            .nom("nom2")
            .email("email2")
            .telephone("telephone2")
            .motDePasse("motDePasse2")
            .matricule("matricule2")
            .fcmToken("fcmToken2")
            .langue("langue2")
            .numeroPermis("numeroPermis2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur()
            .id(longCount.incrementAndGet())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .motDePasse(UUID.randomUUID().toString())
            .matricule(UUID.randomUUID().toString())
            .fcmToken(UUID.randomUUID().toString())
            .langue(UUID.randomUUID().toString())
            .numeroPermis(UUID.randomUUID().toString());
    }
}
