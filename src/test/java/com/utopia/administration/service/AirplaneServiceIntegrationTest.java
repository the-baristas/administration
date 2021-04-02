package com.utopia.administration.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.utopia.administration.dao.AirplaneDao;
import com.utopia.administration.entity.Airplane;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AirplaneServiceIntegrationTest {
    // @TestConfiguration
    // public static class AirplaneServiceTestContextConfig {
    //     @Bean
    //     public AirplaneService aiplaneService() {
    //         return new AirplaneService();
    //     }
    // }

    @Autowired
    private AirplaneService airplaneService;

    @MockBean
    private AirplaneDao airplaneDao;

    @BeforeEach
    public void setUp() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);

        when(airplaneDao.findById(airplane.getId()).get()).thenReturn(airplane);
    }

    @Test
    public void getAirplaneById_ValidId_AirplaneFound() {
        Long id = 1L;
        Airplane airplane = airplaneService.getAirplaneById(id);

        assertThat(id, is(airplane.getId()));
    }
}
