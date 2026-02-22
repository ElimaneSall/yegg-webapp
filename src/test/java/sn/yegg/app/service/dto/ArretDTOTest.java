package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class ArretDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArretDTO.class);
        ArretDTO arretDTO1 = new ArretDTO();
        arretDTO1.setId(1L);
        ArretDTO arretDTO2 = new ArretDTO();
        assertThat(arretDTO1).isNotEqualTo(arretDTO2);
        arretDTO2.setId(arretDTO1.getId());
        assertThat(arretDTO1).isEqualTo(arretDTO2);
        arretDTO2.setId(2L);
        assertThat(arretDTO1).isNotEqualTo(arretDTO2);
        arretDTO1.setId(null);
        assertThat(arretDTO1).isNotEqualTo(arretDTO2);
    }
}
