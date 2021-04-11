package fr.polytech.info4.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourierMapperTest {

    private CourierMapper courierMapper;

    @BeforeEach
    public void setUp() {
        courierMapper = new CourierMapperImpl();
    }
}
