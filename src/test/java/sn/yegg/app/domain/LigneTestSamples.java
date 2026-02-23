package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LigneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ligne getLigneSample1() {
        return new Ligne().id(1L).numero("numero1").nom("nom1").direction("direction1").couleur("couleur1").dureeMoyenne(1).frequence(1);
    }

    public static Ligne getLigneSample2() {
        return new Ligne().id(2L).numero("numero2").nom("nom2").direction("direction2").couleur("couleur2").dureeMoyenne(2).frequence(2);
    }

    public static Ligne getLigneRandomSampleGenerator() {
        return new Ligne()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .direction(UUID.randomUUID().toString())
            .couleur(UUID.randomUUID().toString())
            .dureeMoyenne(intCount.incrementAndGet())
            .frequence(intCount.incrementAndGet());
    }
}
