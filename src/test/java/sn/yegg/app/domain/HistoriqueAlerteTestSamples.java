package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueAlerteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HistoriqueAlerte getHistoriqueAlerteSample1() {
        return new HistoriqueAlerte().id(1L).busNumero("busNumero1").distanceReelle(1).tempsReel(1);
    }

    public static HistoriqueAlerte getHistoriqueAlerteSample2() {
        return new HistoriqueAlerte().id(2L).busNumero("busNumero2").distanceReelle(2).tempsReel(2);
    }

    public static HistoriqueAlerte getHistoriqueAlerteRandomSampleGenerator() {
        return new HistoriqueAlerte()
            .id(longCount.incrementAndGet())
            .busNumero(UUID.randomUUID().toString())
            .distanceReelle(intCount.incrementAndGet())
            .tempsReel(intCount.incrementAndGet());
    }
}
