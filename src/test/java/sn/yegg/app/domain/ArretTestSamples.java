package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArretTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Arret getArretSample1() {
        return new Arret().id(1L).nom("nom1").code("code1").adresse("adresse1").zoneTarifaire("zoneTarifaire1");
    }

    public static Arret getArretSample2() {
        return new Arret().id(2L).nom("nom2").code("code2").adresse("adresse2").zoneTarifaire("zoneTarifaire2");
    }

    public static Arret getArretRandomSampleGenerator() {
        return new Arret()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .zoneTarifaire(UUID.randomUUID().toString());
    }
}
