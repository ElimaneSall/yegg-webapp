package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class RapportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportDTO.class);
        RapportDTO rapportDTO1 = new RapportDTO();
        rapportDTO1.setId(1L);
        RapportDTO rapportDTO2 = new RapportDTO();
        assertThat(rapportDTO1).isNotEqualTo(rapportDTO2);
        rapportDTO2.setId(rapportDTO1.getId());
        assertThat(rapportDTO1).isEqualTo(rapportDTO2);
        rapportDTO2.setId(2L);
        assertThat(rapportDTO1).isNotEqualTo(rapportDTO2);
        rapportDTO1.setId(null);
        assertThat(rapportDTO1).isNotEqualTo(rapportDTO2);
    }
}
