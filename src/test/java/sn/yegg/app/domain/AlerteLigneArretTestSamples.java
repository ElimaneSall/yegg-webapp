package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteLigneArretTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlerteLigneArret getAlerteLigneArretSample1() {
        return new AlerteLigneArret().id(1L).sens("sens1");
    }

    public static AlerteLigneArret getAlerteLigneArretSample2() {
        return new AlerteLigneArret().id(2L).sens("sens2");
    }

    public static AlerteLigneArret getAlerteLigneArretRandomSampleGenerator() {
        return new AlerteLigneArret().id(longCount.incrementAndGet()).sens(UUID.randomUUID().toString());
    }
}
