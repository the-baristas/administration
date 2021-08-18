package com.utopia.flightservice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.dto.FlightDto;
import com.utopia.flightservice.entity.*;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.service.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FlightController.class)
@WithMockUser(authorities = { "ROLE_ADMIN" })
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

    @MockBean
    private AirportService airportService;

    @MockBean
    private AwsS3Service s3Service;

    @Autowired
    private FlightController controller;

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    // Flight Controller Is Not Null
    @Test
    public void controllerLoads() throws Exception {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    public void test_getAllFlights_statusOkAndListLength() throws Exception {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

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
        Flight flight1 = new Flight(100, airplane, departureTime, "A1", arrivalTime, "B1",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route, null);
        Flight flight2 = new Flight(101, airplane2, departureTime, "A2", arrivalTime, "B2",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route2, null);

        flights.add(flight1);
        flights.add(flight2);

        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        when(flightService.getPagedFlights(0, 10, "id")).thenReturn(flightPage);

        mockMvc.perform(get("/flights?pageNo=0&pageSize=10&sortBy=id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    public void shouldGetFlightById() throws Exception {

        Flight flight = makeFlight();
        Optional<Flight> optional = Optional.of(flight);

        when(flightService.getFlightById(100)).thenReturn(optional);

        mockMvc.perform(
                get("/flights/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(flight.getId()))
                .andExpect(jsonPath("$.airplane").value(flight.getAirplane()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateFlight() throws Exception, FlightNotSavedException {

        Flight flight = makeFlight();
        FlightDto flightDTO = makeFlightDTO();

        when(flightService.saveFlight(flight)).thenReturn(flight.getId());
        when(flightService.getFlightById(flight.getId()))
                .thenReturn(Optional.of(flight));
        when(airplaneService
                .findById(Long.valueOf(flightDTO.getAirplaneId())))
                        .thenReturn(flight.getAirplane());
        when(routeService.getRouteById(flightDTO.getRouteId()))
                .thenReturn(Optional.of(flight.getRoute()));

        mockMvc.perform(post("/flights").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(flightDTO))).andDo(print())
                .andExpect(status().isCreated());

        verify(flightService).saveFlight(flight);
    }

    @Test
    public void shouldUpdateflight() throws Exception, FlightNotSavedException {

        FlightDto flightDTO = makeFlightDTO();

        Flight updatedFlight = makeFlight();
        FlightDto updatedFlightDTO = makeFlightDTO();

        when(airplaneService
                .findById(Long.valueOf(flightDTO.getAirplaneId())))
                        .thenReturn(updatedFlight.getAirplane());
        when(routeService.getRouteById(flightDTO.getRouteId()))
                .thenReturn(Optional.of(updatedFlight.getRoute()));
        when(flightService.getFlightById(flightDTO.getId()))
                .thenReturn(Optional.of(updatedFlight));
        when(flightService.updateFlight(flightDTO.getId(), updatedFlight))
                .thenReturn(updatedFlight.getId());

        mockMvc.perform(
                put("/flights/{id}", updatedFlightDTO.getId(), updatedFlightDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(updatedFlightDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteFlight() throws FlightNotSavedException, Exception {

        Flight flight = makeFlight();
        FlightDto flightDTO = makeFlightDTO();

        flightService.saveFlight(flight);

        when(flightService.deleteFlight(flight.getId()))
                .thenReturn("Flight Deleted!");

        mockMvc.perform(
                delete("/flights/100").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flight)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetFlightsByRouteId() throws Exception {

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

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
        routes.add(route);

        User user1 = new User(1L, "testuser1", "test@gmail.com", "347-283-1078");
        User user2 = new User(2L, "testuser2", "testuser2@gmail.com", "212-202-3454");
        HashSet<User> bookedUsers = new HashSet<User>();
        bookedUsers.add(user1);
        bookedUsers.add(user2);

        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(100, airplane, departureTime, "A1", arrivalTime, "B1",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route, bookedUsers);
        Flight flight2 = new Flight(101, airplane2, departureTime, "A1", arrivalTime, "B2",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route, bookedUsers);

        flights.add(flight1);
        flights.add(flight2);

        when(routeService.getRouteByLocationInfo("TC1", "TC2"))
                .thenReturn(routes);

        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        when(flightService.getFlightsByRoute(0, 10,
                "id", routes)).thenReturn(flightPage);

        mockMvc.perform(get("/flights/search?originId=TC1&destinationId=TC2&pageNo=0&pageSize=10&sortBy=id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    public void shouldGetFlightsByRouteAndDate() throws Exception {

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

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

        List<Airport> originAirports = new ArrayList<>();
        originAirports.add(originAirport);
        List<Airport> destAirports = new ArrayList<>();
        destAirports.add(destinationAirport);

        List<Route> routes = new ArrayList<>();
        Route route = new Route(1, originAirport, destinationAirport, true);
        routes.add(route);

        User user1 = new User(1L, "testuser1", "test@gmail.com", "347-283-1078");
        User user2 = new User(2L, "testuser2", "testuser2@gmail.com", "212-202-3454");
        HashSet<User> bookedUsers = new HashSet<User>();
        bookedUsers.add(user1);
        bookedUsers.add(user2);

        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(100, airplane, departureTime, "A1", arrivalTime,"B1",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route, bookedUsers);
        Flight flight2 = new Flight(101, airplane2, departureTime, "A2", arrivalTime, "B2",
                0, 300.00f, 0, 250.00f, 0, 200.00f, true, route, bookedUsers);

        flights.add(flight1);
        flights.add(flight2);

        when(routeService.getRouteByLocationInfo("TC1", "TC2"))
                .thenReturn(routes);

        when(airportService.getAirportByIdOrCity("TC1")).thenReturn(originAirports);
        when(airportService.getAirportByIdOrCity("TC2")).thenReturn(destAirports);

        Page<Flight> flightPage = new PageImpl<Flight>(flights);
        FlightQuery flightQuery = new FlightQuery(9, 1, 2020, "all");

        when(flightService.getFlightsByRouteAndDate(0, 10,
                "economyPrice", routes, flightQuery)).thenReturn(flightPage);

        mockMvc.perform(post("/flights/query?originId=TC1&destinationId=TC2&pageNo=0&pageSize=10&sortBy=id", flightQuery)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(flightQuery))).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testEmailFlightDetailsToAll() throws Exception {
        mockMvc.perform(get("/flights/1")).andExpect(status().isOk());
    }

    @Test void testUploadFlightCsv() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

       mockMvc.perform(multipart("/flights/csv").file(file))
                .andExpect(status().isOk());
    }

    // utility functions

    private Flight makeFlight() {
        Flight flight = new Flight();
        flight.setId(100);

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");
        flight.setAirplane(airplane);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        flight.setRoute(route);

        flight.setFirstReserved(0);
        flight.setFirstPrice(300.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(200.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(100.00f);
        flight.setIsActive(true);
        return flight;
    }

    private FlightDto makeFlightDTO() {
        FlightDto flightDTO = new FlightDto();
        flightDTO.setId(100);

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);
        flightDTO.setDepartureTime(departureTime);
        flightDTO.setArrivalTime(arrivalTime);

        flightDTO.setAirplaneId(1);
        flightDTO.setRouteId(1);

        flightDTO.setFirstReserved(0);
        flightDTO.setFirstPrice(300.00f);
        flightDTO.setBusinessReserved(0);
        flightDTO.setBusinessPrice(200.00f);
        flightDTO.setEconomyReserved(0);
        flightDTO.setEconomyPrice(100.00f);
        flightDTO.setIsActive(true);

        return flightDTO;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
