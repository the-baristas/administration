package com.utopia.administration.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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
        public void getAllAirplanes_JsonArray() throws Exception {
                Airplane airplane = new Airplane();
                airplane.setId(1L);
                airplane.setFirstClassSeatsMax(1L);
                airplane.setBusinessClassSeatsMax(1L);
                airplane.setEconomyClassSeatsMax(1L);
                List<Airplane> allAirplanes = Arrays.asList(airplane);
                when(airplaneService.getAllAirplanes())
                                .thenReturn(allAirplanes);

                Integer airplaneId = Math.toIntExact(airplane.getId());
                Integer airplaneFirstClassSeatsMax = Math
                                .toIntExact(airplane.getFirstClassSeatsMax());
                mockMvc.perform(get("/airplanes")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id", is(airplaneId)))
                                .andExpect(jsonPath("$[0].firstClassSeatsMax",
                                                is(airplaneFirstClassSeatsMax)));
        }

        @Test
        public void createAirplane_AirplaneWithId1_AirplaneWithId1() throws Exception {
                mockMvc.perform(post("/airplanes")).andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L));
        }
}
