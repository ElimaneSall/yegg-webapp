package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.AlerteApprocheAsserts.*;
import static sn.yegg.app.domain.AlerteApprocheTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlerteApprocheMapperTest {

    private AlerteApprocheMapper alerteApprocheMapper;

    @BeforeEach
    void setUp() {
        alerteApprocheMapper = new AlerteApprocheMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlerteApprocheSample1();
        var actual = alerteApprocheMapper.toEntity(alerteApprocheMapper.toDto(expected));
        assertAlerteApprocheAllPropertiesEquals(expected, actual);
    }
}
