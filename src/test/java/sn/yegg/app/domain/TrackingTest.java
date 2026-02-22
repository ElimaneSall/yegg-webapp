package sn.yegg.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.yegg.app.domain.BusTestSamples.*;
import static sn.yegg.app.domain.TrackingTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class TrackingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tracking.class);
        Tracking tracking1 = getTrackingSample1();
        Tracking tracking2 = new Tracking();
        assertThat(tracking1).isNotEqualTo(tracking2);

        tracking2.setId(tracking1.getId());
        assertThat(tracking1).isEqualTo(tracking2);

        tracking2 = getTrackingSample2();
        assertThat(tracking1).isNotEqualTo(tracking2);
    }

    @Test
    void busTest() {
        Tracking tracking = getTrackingRandomSampleGenerator();
        Bus busBack = getBusRandomSampleGenerator();

        tracking.setBus(busBack);
        assertThat(tracking.getBus()).isEqualTo(busBack);

        tracking.bus(null);
        assertThat(tracking.getBus()).isNull();
    }
}
