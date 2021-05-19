package com.utopia.flightservice.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.controller.RouteController;
import com.utopia.flightservice.dto.FlightDto;
import com.utopia.flightservice.entity.Airplane;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.service.AirplaneService;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.service.RouteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(FlightController.class)
public class FlightControllerTests {

    // import mock mvc
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private FlightService flightService;

    @MockBean
    private RouteService routeService;

    @MockBean
    private AirplaneService airplaneService;

    @Autowired
    private FlightController controller;

    // Flight Controller Is Not Null
    @Test
    public void controllerLoads() throws Exception {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    public void test_getAllFlights_statusOkAndListLength() throws Exception {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        List<Airplane> airplanes = new ArrayList<>();
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");
        Airplane airplane2 = new Airplane(1l, 100l, 100l, 100l, "Model 2");
        airplanes.add(airplane);
        airplanes.add(airplane2);

        List<Airport> airports = new ArrayList<>();
        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        airports.add(originAirport);
        airports.add(destinationAirport);

        List<Route> routes = new ArrayList<>();
        Route route = new Route(1, originAirport, destinationAirport, true);
        Route route2 = new Route(2, originAirport, destinationAirport, true);
        routes.add(route);
        routes.add(route2);

        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(100, airplane, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, true, route);
        Flight flight2 = new Flight(101, airplane2, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, true, route2);

        flights.add(flight1);
        flights.add(flight2);

        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        when(flightService.getPagedFlights(0, 10, "id")).thenReturn(flightPage);

        mockMvc.perform(get("/flights?pageNo=0&pageSize=10&sortBy=id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

//    @Test
//    public void shouldCreateFlight() throws Exception, FlightNotSavedException {
//        String str1 = "2020-09-01 09:01:15";
//        String str2 = "2020-09-01 11:01:15";
//        Timestamp departureTime = Timestamp.valueOf(str1);
//        Timestamp arrivalTime = Timestamp.valueOf(str2);
//
//        Flight mockFlight = new Flight();
//        mockFlight.setId(101);
//
//        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");
//        Airport originAirport = new Airport("TC1", "Test City 1", true);
//        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
//        Route route = new Route(1, originAirport, destinationAirport, true);
//
//        mockFlight.setRoute(route);
//        mockFlight.setAirplane(airplane);
//        mockFlight.setDepartureTime(departureTime);
//        mockFlight.setArrivalTime(arrivalTime);
//        mockFlight.setFirstReserved(0);
//        mockFlight.setFirstPrice(350.00f);
//        mockFlight.setBusinessReserved(0);
//        mockFlight.setBusinessPrice(300.00f);
//        mockFlight.setEconomyReserved(0);
//        mockFlight.setEconomyPrice(200.00f);
//        mockFlight.setIsActive(true);
//
//        System.out.println(mockFlight);
//        FlightDto flightDTO = controller.convertToDto(mockFlight);
//        when(flightService.saveFlight(controller.convertToEntity(flightDTO)))
//                .thenReturn(mockFlight.getId());
//
//        mockMvc.perform(post("/flights").contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(flightDTO)))
//                .andExpect(status().isCreated());
//    }

//    @Test
//    public void shouldUpdateFlight() throws Exception, FlightNotSavedException {
//        String str1 = "2020-09-01 09:01:15";
//        String str2 = "2020-09-01 11:01:15";
//        Timestamp departureTime = Timestamp.valueOf(str1);
//        Timestamp arrivalTime = Timestamp.valueOf(str2);
//
//
//        Airplane airplane = airplaneService.findAirplaneById(7L);
//        Route route = routeService.getRouteById(5).get();
//
//        Flight flight = new Flight(101, airplane, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, true, route);
//
//        flightService.saveFlight(flight);
//        Flight updatedFlight = new Flight(101, airplane, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, false, route);
//
//        when(flightService.updateFlight(flight.getId(), updatedFlight))
//                .thenReturn(updatedFlight.getId());
//
//        mockMvc.perform(
//                put("/flights/101").contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(updatedFlight)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void shouldDeleteFlight() throws Exception, FlightNotSavedException {
//        String str1 = "2020-09-01 09:01:15";
//        String str2 = "2020-09-01 11:01:15";
//        Timestamp departureTime = Timestamp.valueOf(str1);
//        Timestamp arrivalTime = Timestamp.valueOf(str2);
//
//        Airplane airplane = airplaneService.findAirplaneById(7L);
//
//        Route route = routeService.getRouteById(5).get();
//
//        Flight flight = new Flight(101, airplane, departureTime, arrivalTime, 0, 300.00f, 0, 250.00f, 0, 200.00f, true, route);
//
//        flightService.saveFlight(flight);
//        when(flightService.deleteFlight(flight.getId()))
//                .thenReturn(flight.getId().toString());
//
//        mockMvc.perform(
//                delete("/flights/101").contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(flight)))
//                .andExpect(status().isNoContent());
//    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
