package com.utopia.flightservice.controller;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.utopia.flightservice.controller.FlightController;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.service.FlightService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(100, 5, 15, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );
        Flight flight2 = new Flight(101, 7, 8, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, 1 );

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
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Flight mockFlight = new Flight();
        mockFlight.setId(101);
        mockFlight.setRouteId(5);
        mockFlight.setAirplaneId(7);
        mockFlight.setDepartureTime(departureTime);
        mockFlight.setArrivalTime(arrivalTime);
        mockFlight.setFirstReserved(0);
        mockFlight.setFirstPrice(350.00f);
        mockFlight.setBusinessReserved(0);
        mockFlight.setBusinessPrice(300.00f);
        mockFlight.setEconomyReserved(0);
        mockFlight.setEconomyPrice(200.00f);
        mockFlight.setIsActive(1);

        System.out.println(mockFlight);
        when(flightService.saveFlight(mockFlight)).thenReturn(mockFlight.getId());

        mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockFlight)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateFlight() throws Exception, FlightNotSavedException {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

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
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

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
