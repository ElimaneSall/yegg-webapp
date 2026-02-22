package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.BusAsserts.*;
import static sn.yegg.app.domain.BusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusMapperTest {

    private BusMapper busMapper;

    @BeforeEach
    void setUp() {
        busMapper = new BusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBusSample1();
        var actual = busMapper.toEntity(busMapper.toDto(expected));
        assertBusAllPropertiesEquals(expected, actual);
    }
}
