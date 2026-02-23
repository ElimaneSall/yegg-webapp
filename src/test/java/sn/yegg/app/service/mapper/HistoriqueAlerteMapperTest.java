package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.HistoriqueAlerteAsserts.*;
import static sn.yegg.app.domain.HistoriqueAlerteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueAlerteMapperTest {

    private HistoriqueAlerteMapper historiqueAlerteMapper;

    @BeforeEach
    void setUp() {
        historiqueAlerteMapper = new HistoriqueAlerteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueAlerteSample1();
        var actual = historiqueAlerteMapper.toEntity(historiqueAlerteMapper.toDto(expected));
        assertHistoriqueAlerteAllPropertiesEquals(expected, actual);
    }
}
