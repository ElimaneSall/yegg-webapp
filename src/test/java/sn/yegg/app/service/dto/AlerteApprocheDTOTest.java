package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteApprocheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteApprocheDTO.class);
        AlerteApprocheDTO alerteApprocheDTO1 = new AlerteApprocheDTO();
        alerteApprocheDTO1.setId(1L);
        AlerteApprocheDTO alerteApprocheDTO2 = new AlerteApprocheDTO();
        assertThat(alerteApprocheDTO1).isNotEqualTo(alerteApprocheDTO2);
        alerteApprocheDTO2.setId(alerteApprocheDTO1.getId());
        assertThat(alerteApprocheDTO1).isEqualTo(alerteApprocheDTO2);
        alerteApprocheDTO2.setId(2L);
        assertThat(alerteApprocheDTO1).isNotEqualTo(alerteApprocheDTO2);
        alerteApprocheDTO1.setId(null);
        assertThat(alerteApprocheDTO1).isNotEqualTo(alerteApprocheDTO2);
    }
}
