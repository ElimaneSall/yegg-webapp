package sn.yegg.app.service.mapper;

import static sn.yegg.app.domain.FavoriAsserts.*;
import static sn.yegg.app.domain.FavoriTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoriMapperTest {

    private FavoriMapper favoriMapper;

    @BeforeEach
    void setUp() {
        favoriMapper = new FavoriMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFavoriSample1();
        var actual = favoriMapper.toEntity(favoriMapper.toDto(expected));
        assertFavoriAllPropertiesEquals(expected, actual);
    }
}
