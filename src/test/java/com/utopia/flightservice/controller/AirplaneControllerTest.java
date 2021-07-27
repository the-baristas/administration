package com.utopia.flightservice.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.dto.AirplaneDto;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.service.AirplaneService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

@WebMvcTest(AirplaneController.class)
public class AirplaneControllerTest {
    private WebTestClient webTestClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private AirplaneService airplaneService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
    }

    @Test
    public void findAll_AirplanesPageFound() throws JsonProcessingException {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        Page<Airplane> foundAirplanesPage = new PageImpl<Airplane>(
                Arrays.asList(airplane));
        Integer pageIndex = 0;
        Integer pageSize = 1;
        when(airplaneService.findAll(pageIndex, pageSize))
                .thenReturn(foundAirplanesPage);
        Page<AirplaneDto> foundAirplaneDtosPage = foundAirplanesPage
                .map((Airplane a) -> modelMapper.map(a, AirplaneDto.class));

        webTestClient.get()
                .uri("/airplanes?index={pageIndex}&size={pageSize}", pageIndex,
                        pageSize)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(objectMapper.writeValueAsString(foundAirplaneDtosPage));
    }

    @Test
    public void findAirplaneById_AirplaneFound() throws Exception {
        Airplane foundAirplane = new Airplane();
        foundAirplane.setId(1L);
        foundAirplane.setFirstClassSeatsMax(0L);
        foundAirplane.setBusinessClassSeatsMax(0L);
        foundAirplane.setEconomyClassSeatsMax(0L);
        when(airplaneService.findById(foundAirplane.getId()))
                .thenReturn(foundAirplane);
        AirplaneDto foundAirplaneDto = modelMapper.map(foundAirplane,
                AirplaneDto.class);

        webTestClient.get().uri("/airplanes/{id}", foundAirplane.getId())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AirplaneDto.class).isEqualTo(foundAirplaneDto);
    }

    @Test
    public void createAirplane_Airplane_AirplaneFound() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        when(airplaneService.create(airplane)).thenReturn(airplane);
        AirplaneDto airplaneDto = modelMapper.map(airplane, AirplaneDto.class);

        webTestClient.post().uri("/airplanes")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(airplane)
                .exchange().expectStatus().isCreated().expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(AirplaneDto.class).isEqualTo(airplaneDto);
    }

    @Test
    public void updateAirplane_ValidAirplaneId_AirplaneUpdated() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneService.update(airplane)).thenReturn(airplane);
        AirplaneDto airplaneDto = modelMapper.map(airplane, AirplaneDto.class);

        webTestClient.put().uri("/airplanes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).bodyValue(airplane)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(AirplaneDto.class).isEqualTo(airplaneDto);

    }

    @Test
    public void deleteById_ValidAirplaneId_AirplaneDeleted() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);

        airplaneService.deleteById(airplane.getId());
        verify(airplaneService, times(1)).deleteById(airplane.getId());
    }

    @Test
    public void foundPlane_ByModel() throws JsonProcessingException {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        airplane.setModel("Airbus A220");

        Airplane airplane2 = new Airplane();
        airplane2.setId(2L);
        airplane2.setFirstClassSeatsMax(1L);
        airplane2.setBusinessClassSeatsMax(1L);
        airplane2.setEconomyClassSeatsMax(1L);
        airplane2.setModel("Airbus A220");

        Page<Airplane> foundAirplanesPage = new PageImpl<Airplane>(
                Arrays.asList(airplane, airplane2));
        Integer pageIndex = 0;
        Integer pageSize = 1;
        String term = "Airbus A220";

        when(airplaneService.search(term, pageIndex,
                pageSize))
                .thenReturn(foundAirplanesPage);

        Page<AirplaneDto> foundAirplaneDtosPage = foundAirplanesPage
                .map((Airplane a) -> modelMapper.map(a, AirplaneDto.class));

        webTestClient.get()
                .uri("/airplanes/search?term={term}&index={pageIndex}&size={pageSize}", term, pageIndex,
                        pageSize)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(objectMapper.writeValueAsString(foundAirplaneDtosPage));

    }

    @Test
    public void foundDistinct_ByModel() throws JsonProcessingException {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        airplane.setModel("Airbus A220");

        Airplane airplane2 = new Airplane();
        airplane2.setId(2L);
        airplane2.setFirstClassSeatsMax(1L);
        airplane2.setBusinessClassSeatsMax(1L);
        airplane2.setEconomyClassSeatsMax(1L);
        airplane2.setModel("Airbus A220");

        Page<Airplane> foundAirplanesPage = new PageImpl<Airplane>(
                Arrays.asList(airplane, airplane2));
        Integer pageIndex = 0;
        Integer pageSize = 1;
        String term = "Airbus A220";

        when(airplaneService.findDistinctByModelContaining(term, pageIndex,
                pageSize))
                .thenReturn(foundAirplanesPage);

        Page<AirplaneDto> foundAirplaneDtosPage = foundAirplanesPage
                .map((Airplane a) -> modelMapper.map(a, AirplaneDto.class));

        webTestClient.get()
                .uri("/airplanes/distinct_search?term={term}&index={pageIndex}&size={pageSize}", term, pageIndex,
                        pageSize)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(objectMapper.writeValueAsString(foundAirplaneDtosPage));

    }



}