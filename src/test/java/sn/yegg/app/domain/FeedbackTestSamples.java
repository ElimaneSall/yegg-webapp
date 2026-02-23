package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback().id(1L).note(1).typeObjet("typeObjet1").objetId(1L);
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback().id(2L).note(2).typeObjet("typeObjet2").objetId(2L);
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback()
            .id(longCount.incrementAndGet())
            .note(intCount.incrementAndGet())
            .typeObjet(UUID.randomUUID().toString())
            .objetId(longCount.incrementAndGet());
    }
}
