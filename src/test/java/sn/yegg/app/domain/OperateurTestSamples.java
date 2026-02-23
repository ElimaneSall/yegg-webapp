package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OperateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Operateur getOperateurSample1() {
        return new Operateur().id(1L).nom("nom1").email("email1").telephone("telephone1").siteWeb("siteWeb1").siret("siret1");
    }

    public static Operateur getOperateurSample2() {
        return new Operateur().id(2L).nom("nom2").email("email2").telephone("telephone2").siteWeb("siteWeb2").siret("siret2");
    }

    public static Operateur getOperateurRandomSampleGenerator() {
        return new Operateur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .siteWeb(UUID.randomUUID().toString())
            .siret(UUID.randomUUID().toString());
    }
}
