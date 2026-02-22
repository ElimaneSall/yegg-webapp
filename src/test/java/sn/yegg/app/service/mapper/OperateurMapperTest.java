package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.OperateurAsserts.*;
import static sn.yegg.app.domain.OperateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperateurMapperTest {

    private OperateurMapper operateurMapper;

    @BeforeEach
    void setUp() {
        operateurMapper = new OperateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOperateurSample1();
        var actual = operateurMapper.toEntity(operateurMapper.toDto(expected));
        assertOperateurAllPropertiesEquals(expected, actual);
    }
}
