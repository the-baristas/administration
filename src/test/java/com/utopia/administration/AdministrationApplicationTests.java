package com.utopia.administration;

import static org.assertj.core.api.Assertions.assertThat;

import com.utopia.administration.controller.AirplaneController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class AdministrationApplicationTests {
    @Autowired
    private AirplaneController airplaneController;

    @Test
    void contextLoads() {
        assertThat(airplaneController).isNotNull();
    }
}
