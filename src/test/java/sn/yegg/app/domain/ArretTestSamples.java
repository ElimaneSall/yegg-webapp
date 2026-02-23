package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArretTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Arret getArretSample1() {
        return new Arret()
            .id(1L)
            .nom("nom1")
            .code("code1")
            .altitude(1)
            .adresse("adresse1")
            .ville("ville1")
            .codePostal("codePostal1")
            .zoneTarifaire("zoneTarifaire1");
    }

    public static Arret getArretSample2() {
        return new Arret()
            .id(2L)
            .nom("nom2")
            .code("code2")
            .altitude(2)
            .adresse("adresse2")
            .ville("ville2")
            .codePostal("codePostal2")
            .zoneTarifaire("zoneTarifaire2");
    }

    public static Arret getArretRandomSampleGenerator() {
        return new Arret()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .altitude(intCount.incrementAndGet())
            .adresse(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .codePostal(UUID.randomUUID().toString())
            .zoneTarifaire(UUID.randomUUID().toString());
    }
}
