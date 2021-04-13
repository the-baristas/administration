package com.utopia.flightservice.flight;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.utopia.flightservice.route.Route;
import org.junit.jupiter.api.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FlightController.class)
@AutoConfigureMockMvc
public class FlightControllerTests {

    // import mock mvc
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FlightService flightService;

    @Autowired
    private FlightController controller;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void controllerLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void test_getAllFlights_statusOkAndListLength() throws Exception {
        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(100, 5, 15, LocalDateTime.parse("2021-04-09T18:30:00"), LocalDateTime.parse("2021-04-09T21:30:00"), 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );
        Flight flight2 = new Flight(101, 7, 8, LocalDateTime.parse("2021-04-09T18:35:00"), LocalDateTime.parse("2021-04-09T21:35:00"), 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );

        flights.add(flight1);
        flights.add(flight2);
        when(flightService.getAllFlights()).thenReturn(flights);

        // create list of airports, pass it to thenReturn to test that getting back list of airports

        mockMvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    // how does creating the flight work above but not down here, how is this a bad request, i hate it here!!!!

    @Test
    public void shouldCreateFlight() throws Exception, FlightNotSavedException {
        Flight mockFlight = new Flight(101, 7, 8, LocalDateTime.parse("2021-04-09T18:35:00"), LocalDateTime.parse("2021-04-09T21:35:00"), 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );

        when(flightService.saveFlight(mockFlight)).thenReturn(mockFlight.getId());

        mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockFlight)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateFlight() throws Exception, FlightNotSavedException {
        LocalDateTime departureTime = LocalDateTime.of(2015, Month.APRIL, 20, 6, 30);
        LocalDateTime arrivalTime = LocalDateTime.of(2015, Month.APRIL, 20, 8, 30);

        Flight flight = new Flight(101, 7, 8, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );

        flightService.saveFlight(flight);
        Flight updatedFlight = new Flight(101, 7, 8, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, 2 );

        when(flightService.updateFlight(flight.getId(), updatedFlight)).thenReturn(updatedFlight.getId());

        mockMvc.perform(put("/flights/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedFlight)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteFlight() throws Exception, FlightNotSavedException {
        LocalDateTime departureTime = LocalDateTime.of(2015, Month.APRIL, 20, 6, 30);
        LocalDateTime arrivalTime = LocalDateTime.of(2015, Month.APRIL, 20, 8, 30);

        Flight flight = new Flight(101, 7, 8, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );

        flightService.saveFlight(flight);
        when(flightService.deleteFlight(flight.getId())).thenReturn(flight.getId().toString());

        mockMvc.perform(delete("/flights/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(flight)))
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
