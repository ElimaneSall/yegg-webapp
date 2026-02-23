package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class HistoriqueAlerteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueAlerteDTO.class);
        HistoriqueAlerteDTO historiqueAlerteDTO1 = new HistoriqueAlerteDTO();
        historiqueAlerteDTO1.setId(1L);
        HistoriqueAlerteDTO historiqueAlerteDTO2 = new HistoriqueAlerteDTO();
        assertThat(historiqueAlerteDTO1).isNotEqualTo(historiqueAlerteDTO2);
        historiqueAlerteDTO2.setId(historiqueAlerteDTO1.getId());
        assertThat(historiqueAlerteDTO1).isEqualTo(historiqueAlerteDTO2);
        historiqueAlerteDTO2.setId(2L);
        assertThat(historiqueAlerteDTO1).isNotEqualTo(historiqueAlerteDTO2);
        historiqueAlerteDTO1.setId(null);
        assertThat(historiqueAlerteDTO1).isNotEqualTo(historiqueAlerteDTO2);
    }
}
