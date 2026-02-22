package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.TrackingAsserts.*;
import static sn.yegg.app.domain.TrackingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackingMapperTest {

    private TrackingMapper trackingMapper;

    @BeforeEach
    void setUp() {
        trackingMapper = new TrackingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrackingSample1();
        var actual = trackingMapper.toEntity(trackingMapper.toDto(expected));
        assertTrackingAllPropertiesEquals(expected, actual);
    }
}
