package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.LigneArretAsserts.*;
import static sn.yegg.app.domain.LigneArretTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneArretMapperTest {

    private LigneArretMapper ligneArretMapper;

    @BeforeEach
    void setUp() {
        ligneArretMapper = new LigneArretMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLigneArretSample1();
        var actual = ligneArretMapper.toEntity(ligneArretMapper.toDto(expected));
        assertLigneArretAllPropertiesEquals(expected, actual);
    }
}
