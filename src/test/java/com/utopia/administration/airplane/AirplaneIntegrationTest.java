package com.utopia.administration.airplane;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestPropertySource(locations =
// "classpath:application-integrationtest.properties")
public class AirplaneIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Test
    public void findAllAirplanes_AirplanesList() {
        Airplane airplane = new Airplane();
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        airplaneRepository.save(airplane);

        webClient.get().uri("/airplanes").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Airplane.class)
                .isEqualTo(Arrays.asList(airplane));
    }

    @Test
    public void findAirplaneById_ValidId_AirplaneFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        airplaneRepository.save(airplane);

        webClient.get().uri("/airplanes/{id}", airplane.getId())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Airplane.class).isEqualTo(airplane);
    }

    @Test
    public void createAirplane_Airplane_AirplaneFound() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        airplaneRepository.save(airplane);

        webClient.post().uri("/airplanes")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(airplane)
                .exchange().expectStatus().isCreated().expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(Airplane.class).isEqualTo(airplane);
    }
}
