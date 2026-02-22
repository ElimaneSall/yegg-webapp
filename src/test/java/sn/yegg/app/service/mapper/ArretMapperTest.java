package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.ArretAsserts.*;
import static sn.yegg.app.domain.ArretTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArretMapperTest {

    private ArretMapper arretMapper;

    @BeforeEach
    void setUp() {
        arretMapper = new ArretMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArretSample1();
        var actual = arretMapper.toEntity(arretMapper.toDto(expected));
        assertArretAllPropertiesEquals(expected, actual);
    }
}
