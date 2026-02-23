package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteApprocheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlerteApproche getAlerteApprocheSample1() {
        return new AlerteApproche()
            .id(1L)
            .nom("nom1")
            .seuilDistance(1)
            .seuilTemps(1)
            .joursActivation("joursActivation1")
            .heureDebut("heureDebut1")
            .heureFin("heureFin1")
            .nombreDeclenchements(1);
    }

    public static AlerteApproche getAlerteApprocheSample2() {
        return new AlerteApproche()
            .id(2L)
            .nom("nom2")
            .seuilDistance(2)
            .seuilTemps(2)
            .joursActivation("joursActivation2")
            .heureDebut("heureDebut2")
            .heureFin("heureFin2")
            .nombreDeclenchements(2);
    }

    public static AlerteApproche getAlerteApprocheRandomSampleGenerator() {
        return new AlerteApproche()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .seuilDistance(intCount.incrementAndGet())
            .seuilTemps(intCount.incrementAndGet())
            .joursActivation(UUID.randomUUID().toString())
            .heureDebut(UUID.randomUUID().toString())
            .heureFin(UUID.randomUUID().toString())
            .nombreDeclenchements(intCount.incrementAndGet());
    }
}
