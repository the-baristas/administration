package com.utopia.administration.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.administration.entity.Airplane;
import com.utopia.administration.service.AirplaneService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AirplaneController.class)
public class AirplaneControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirplaneService airplaneService;

    @Test
    public void findAllAirplanes_JsonArray() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        List<Airplane> allAirplanes = Arrays.asList(airplane);
        when(airplaneService.findAllAirplanes()).thenReturn(allAirplanes);

        mockMvc.perform(
                get("/airplanes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(airplane.getId()))
                .andExpect(jsonPath("$[0].firstClassSeatsMax")
                        .value(airplane.getFirstClassSeatsMax()))
                .andExpect(jsonPath("$[0].businessClassSeatsMax")
                        .value(airplane.getBusinessClassSeatsMax()))
                .andExpect(jsonPath("$[0].economyClassSeatsMax")
                        .value(airplane.getEconomyClassSeatsMax()))
                .andDo(print());
    }

    @Test
    public void findAirplaneById_AirplaneFound() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneService.findAirplaneById(airplane.getId()))
                .thenReturn(airplane);

        mockMvc.perform(get("/airplanes/{id}", airplane.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createAirplane_Airplane_AirplaneFound() throws Exception {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(1L);
        airplane.setBusinessClassSeatsMax(1L);
        airplane.setEconomyClassSeatsMax(1L);
        when(airplaneService.createAirplane(airplane)).thenReturn(airplane);

        String airplaneJsonString = new ObjectMapper()
                .writeValueAsString(airplane);
        mockMvc.perform(
                post("/airplanes").contentType(MediaType.APPLICATION_JSON)
                        .content(airplaneJsonString))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        containsString("/airplanes")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstClassSeatsMax").value(1L))
                .andExpect(jsonPath("$.businessClassSeatsMax").value(1L))
                .andExpect(jsonPath("$.economyClassSeatsMax").value(1L))
                .andDo(print());
    }
}
