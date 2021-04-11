package fr.polytech.info4.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourierDTO.class);
        CourierDTO courierDTO1 = new CourierDTO();
        courierDTO1.setId(1L);
        CourierDTO courierDTO2 = new CourierDTO();
        assertThat(courierDTO1).isNotEqualTo(courierDTO2);
        courierDTO2.setId(courierDTO1.getId());
        assertThat(courierDTO1).isEqualTo(courierDTO2);
        courierDTO2.setId(2L);
        assertThat(courierDTO1).isNotEqualTo(courierDTO2);
        courierDTO1.setId(null);
        assertThat(courierDTO1).isNotEqualTo(courierDTO2);
    }
}
