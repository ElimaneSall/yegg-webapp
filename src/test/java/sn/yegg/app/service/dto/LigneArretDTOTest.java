package sn.yegg.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.yegg.app.web.rest.TestUtil;

class LigneArretDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneArretDTO.class);
        LigneArretDTO ligneArretDTO1 = new LigneArretDTO();
        ligneArretDTO1.setId(1L);
        LigneArretDTO ligneArretDTO2 = new LigneArretDTO();
        assertThat(ligneArretDTO1).isNotEqualTo(ligneArretDTO2);
        ligneArretDTO2.setId(ligneArretDTO1.getId());
        assertThat(ligneArretDTO1).isEqualTo(ligneArretDTO2);
        ligneArretDTO2.setId(2L);
        assertThat(ligneArretDTO1).isNotEqualTo(ligneArretDTO2);
        ligneArretDTO1.setId(null);
        assertThat(ligneArretDTO1).isNotEqualTo(ligneArretDTO2);
    }
}
