package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.AlerteLigneArretAsserts.*;
import static sn.yegg.app.domain.AlerteLigneArretTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlerteLigneArretMapperTest {

    private AlerteLigneArretMapper alerteLigneArretMapper;

    @BeforeEach
    void setUp() {
        alerteLigneArretMapper = new AlerteLigneArretMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlerteLigneArretSample1();
        var actual = alerteLigneArretMapper.toEntity(alerteLigneArretMapper.toDto(expected));
        assertAlerteLigneArretAllPropertiesEquals(expected, actual);
    }
}
