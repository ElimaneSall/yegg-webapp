package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrackingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tracking getTrackingSample1() {
        return new Tracking().id(1L).cap(1).precision(1).evenement("evenement1");
    }

    public static Tracking getTrackingSample2() {
        return new Tracking().id(2L).cap(2).precision(2).evenement("evenement2");
    }

    public static Tracking getTrackingRandomSampleGenerator() {
        return new Tracking()
            .id(longCount.incrementAndGet())
            .cap(intCount.incrementAndGet())
            .precision(intCount.incrementAndGet())
            .evenement(UUID.randomUUID().toString());
    }
}
