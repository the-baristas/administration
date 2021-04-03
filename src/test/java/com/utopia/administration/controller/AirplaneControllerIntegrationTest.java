package com.utopia.administration.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.utopia.administration.entity.Airplane;
import com.utopia.administration.service.AirplaneService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AirplaneControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirplaneService airplaneService;

    @Test
    public void findAirplanes_Status200() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        List<Airplane> allAirplanes = Arrays.asList(airplane);
        when(airplaneService.findAllAirplanes()).thenReturn(allAirplanes);

        mockMvc.perform(get("/airplanes")).andDo(print())
                .andExpect(status().isOk());

        // client.get().uri("/airplanes")
        //         .accept(MediaType.APPLICATION_JSON)
        //         .exchange().expectStatus().isOk()
        //         .expectHeader().contentType(MediaType.APPLICATION_JSON);
        
        // client.get().uri("/airplanes")
        //         .exchange()
        //         .expectStatus().isOk()
        //         .expectBodyList(Airplane.class).hasSize(1).contains(airplane);
    }
}
