package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.LigneAsserts.*;
import static sn.yegg.app.domain.LigneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneMapperTest {

    private LigneMapper ligneMapper;

    @BeforeEach
    void setUp() {
        ligneMapper = new LigneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLigneSample1();
        var actual = ligneMapper.toEntity(ligneMapper.toDto(expected));
        assertLigneAllPropertiesEquals(expected, actual);
    }
}
