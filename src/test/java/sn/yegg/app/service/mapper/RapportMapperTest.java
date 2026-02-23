package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.RapportAsserts.*;
import static sn.yegg.app.domain.RapportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RapportMapperTest {

    private RapportMapper rapportMapper;

    @BeforeEach
    void setUp() {
        rapportMapper = new RapportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRapportSample1();
        var actual = rapportMapper.toEntity(rapportMapper.toDto(expected));
        assertRapportAllPropertiesEquals(expected, actual);
    }
}
