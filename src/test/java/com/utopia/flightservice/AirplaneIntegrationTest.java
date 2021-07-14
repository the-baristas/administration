package com.utopia.flightservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.repository.AirplaneRepository;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestPropertySource(locations =
// "classpath:application-integrationtest.properties")
public class AirplaneIntegrationTest {
    @TestConfiguration
    static class TestContextConfig {
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findAll_AirplanesPageFound() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        airplane.setModel("model");
        airplaneRepository.save(airplane);

        String jsonString = "{\"content\":[{\"id\":1,\"firstClassSeatsMax\":0,"
                + "\"businessClassSeatsMax\":0,\"economyClassSeatsMax\":0,"
                + "\"model\":\"model\"}],"
                + "\"pageable\":{\"sort\":{\"sorted\":false,"
                + "\"unsorted\":true,\"empty\":true},\"offset\":0,"
                + "\"pageNumber\":0,\"pageSize\":1,\"paged\":true,"
                + "\"unpaged\":false},\"last\":true,\"totalElements\":1,"
                + "\"totalPages\":1,\"size\":1,\"number\":0,"
                + "\"sort\":{\"sorted\":false,\"unsorted\":true,"
                + "\"empty\":true},\"first\":true,\"numberOfElements\":1,"
                + "\"empty\":false}";
        webTestClient.get().uri("/airplanes?index=0&size=1")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(jsonString);
    }

    @Test
    public void findAirplaneById_ValidId_AirplaneFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        airplaneRepository.save(airplane);

        webTestClient.get().uri("/airplanes/{id}", airplane.getId())
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

        webTestClient.post().uri("/airplanes")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(airplane)
                .exchange().expectStatus().isCreated().expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(Airplane.class).isEqualTo(airplane);
    }
}