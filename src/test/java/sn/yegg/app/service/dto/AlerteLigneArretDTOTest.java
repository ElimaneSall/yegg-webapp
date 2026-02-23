package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class AlerteLigneArretDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlerteLigneArretDTO.class);
        AlerteLigneArretDTO alerteLigneArretDTO1 = new AlerteLigneArretDTO();
        alerteLigneArretDTO1.setId(1L);
        AlerteLigneArretDTO alerteLigneArretDTO2 = new AlerteLigneArretDTO();
        assertThat(alerteLigneArretDTO1).isNotEqualTo(alerteLigneArretDTO2);
        alerteLigneArretDTO2.setId(alerteLigneArretDTO1.getId());
        assertThat(alerteLigneArretDTO1).isEqualTo(alerteLigneArretDTO2);
        alerteLigneArretDTO2.setId(2L);
        assertThat(alerteLigneArretDTO1).isNotEqualTo(alerteLigneArretDTO2);
        alerteLigneArretDTO1.setId(null);
        assertThat(alerteLigneArretDTO1).isNotEqualTo(alerteLigneArretDTO2);
    }
}
