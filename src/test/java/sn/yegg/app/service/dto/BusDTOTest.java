package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class BusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusDTO.class);
        BusDTO busDTO1 = new BusDTO();
        busDTO1.setId(1L);
        BusDTO busDTO2 = new BusDTO();
        assertThat(busDTO1).isNotEqualTo(busDTO2);
        busDTO2.setId(busDTO1.getId());
        assertThat(busDTO1).isEqualTo(busDTO2);
        busDTO2.setId(2L);
        assertThat(busDTO1).isNotEqualTo(busDTO2);
        busDTO1.setId(null);
        assertThat(busDTO1).isNotEqualTo(busDTO2);
    }
}
