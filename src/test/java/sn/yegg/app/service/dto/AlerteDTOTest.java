package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteDTO.class);
        AlerteDTO alerteDTO1 = new AlerteDTO();
        alerteDTO1.setId(1L);
        AlerteDTO alerteDTO2 = new AlerteDTO();
        assertThat(alerteDTO1).isNotEqualTo(alerteDTO2);
        alerteDTO2.setId(alerteDTO1.getId());
        assertThat(alerteDTO1).isEqualTo(alerteDTO2);
        alerteDTO2.setId(2L);
        assertThat(alerteDTO1).isNotEqualTo(alerteDTO2);
        alerteDTO1.setId(null);
        assertThat(alerteDTO1).isNotEqualTo(alerteDTO2);
    }
}
