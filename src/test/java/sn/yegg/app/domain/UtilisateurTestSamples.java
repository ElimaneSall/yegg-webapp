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
            .matricule("matricule1")
            .telephone("telephone1")
            .fcmToken("fcmToken1")
            .langue("langue1")
            .numeroPermis("numeroPermis1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur()
            .id(2L)
            .matricule("matricule2")
            .telephone("telephone2")
            .fcmToken("fcmToken2")
            .langue("langue2")
            .numeroPermis("numeroPermis2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur()
            .id(longCount.incrementAndGet())
            .matricule(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .fcmToken(UUID.randomUUID().toString())
            .langue(UUID.randomUUID().toString())
            .numeroPermis(UUID.randomUUID().toString());
    }
}
