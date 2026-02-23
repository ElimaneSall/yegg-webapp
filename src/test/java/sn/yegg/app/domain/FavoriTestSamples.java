package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FavoriTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Favori getFavoriSample1() {
        return new Favori().id(1L).cibleId(1L).nomPersonnalise("nomPersonnalise1").ordre(1);
    }

    public static Favori getFavoriSample2() {
        return new Favori().id(2L).cibleId(2L).nomPersonnalise("nomPersonnalise2").ordre(2);
    }

    public static Favori getFavoriRandomSampleGenerator() {
        return new Favori()
            .id(longCount.incrementAndGet())
            .cibleId(longCount.incrementAndGet())
            .nomPersonnalise(UUID.randomUUID().toString())
            .ordre(intCount.incrementAndGet());
    }
}
