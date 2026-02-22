package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.UtilisateurAsserts.*;
import static sn.yegg.app.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilisateurMapperTest {

    private UtilisateurMapper utilisateurMapper;

    @BeforeEach
    void setUp() {
        utilisateurMapper = new UtilisateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtilisateurSample1();
        var actual = utilisateurMapper.toEntity(utilisateurMapper.toDto(expected));
        assertUtilisateurAllPropertiesEquals(expected, actual);
    }
}
