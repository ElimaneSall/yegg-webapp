package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Alerte getAlerteSample1() {
        return new Alerte()
            .id(1L)
            .typeCible("typeCible1")
            .cibleId(1L)
            .seuilMinutes(1)
            .joursActivation("joursActivation1")
            .statut("statut1")
            .nombreDeclenchements(1);
    }

    public static Alerte getAlerteSample2() {
        return new Alerte()
            .id(2L)
            .typeCible("typeCible2")
            .cibleId(2L)
            .seuilMinutes(2)
            .joursActivation("joursActivation2")
            .statut("statut2")
            .nombreDeclenchements(2);
    }

    public static Alerte getAlerteRandomSampleGenerator() {
        return new Alerte()
            .id(longCount.incrementAndGet())
            .typeCible(UUID.randomUUID().toString())
            .cibleId(longCount.incrementAndGet())
            .seuilMinutes(intCount.incrementAndGet())
            .joursActivation(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString())
            .nombreDeclenchements(intCount.incrementAndGet());
    }
}
