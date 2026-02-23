package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rapport getRapportSample1() {
        return new Rapport().id(1L).nom("nom1").generePar("generePar1");
    }

    public static Rapport getRapportSample2() {
        return new Rapport().id(2L).nom("nom2").generePar("generePar2");
    }

    public static Rapport getRapportRandomSampleGenerator() {
        return new Rapport().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).generePar(UUID.randomUUID().toString());
    }
}
