package sn.yegg.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LigneArretTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LigneArret getLigneArretSample1() {
        return new LigneArret().id(1L).ordre(1).tempsTrajetDepart(1).tempsArretMoyen(1);
    }

    public static LigneArret getLigneArretSample2() {
        return new LigneArret().id(2L).ordre(2).tempsTrajetDepart(2).tempsArretMoyen(2);
    }

    public static LigneArret getLigneArretRandomSampleGenerator() {
        return new LigneArret()
            .id(longCount.incrementAndGet())
            .ordre(intCount.incrementAndGet())
            .tempsTrajetDepart(intCount.incrementAndGet())
            .tempsArretMoyen(intCount.incrementAndGet());
    }
}
