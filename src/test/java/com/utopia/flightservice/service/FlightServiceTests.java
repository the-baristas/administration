package com.utopia.flightservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.*;

import com.utopia.flightservice.email.EmailSender;
import com.utopia.flightservice.entity.*;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.repository.FlightDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class FlightServiceTests {

    @InjectMocks
    private FlightService flightService;

    @InjectMocks
    private RouteService routeService;

    @InjectMocks
    private AirplaneService airplaneService;

    @Mock
    private FlightDao flightDao;

    @Mock
    private EmailSender emailSender;

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void findAllFlights_FindsFlights() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        List<Flight> allFlights = Arrays.asList(flight);
        when(flightDao.findAll()).thenReturn(allFlights);

        List<Flight> foundFlights = flightService.getAllFlights();
        assertEquals(allFlights, foundFlights);
    }

    @Test
    void findFlightById_FindsFlight() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Optional<Flight> flight = Optional.ofNullable(new Flight());
        flight.get().setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.get().setRoute(route);
        flight.get().setAirplane(airplane);
        flight.get().setDepartureTime(departureTime);
        flight.get().setArrivalTime(arrivalTime);
        flight.get().setFirstReserved(0);
        flight.get().setFirstPrice(350.00f);
        flight.get().setBusinessReserved(0);
        flight.get().setBusinessPrice(300.00f);
        flight.get().setEconomyReserved(0);
        flight.get().setEconomyPrice(200.00f);
        flight.get().setIsActive(true);

        when(flightDao.findById(101)).thenReturn(flight);

        Optional<Flight> foundFlight = flightService.getFlightById(101);
        assertThat(flight.get().getId(), is(foundFlight.get().getId()));
        assertThat(flight.get().getRoute(), is(foundFlight.get().getRoute()));
        assertThat(flight.get().getAirplane(),
                is(foundFlight.get().getAirplane()));
        assertThat(flight.get().getDepartureTime(),
                is(foundFlight.get().getDepartureTime()));
        assertThat(flight.get().getArrivalTime(),
                is(foundFlight.get().getArrivalTime()));
        assertThat(flight.get().getFirstReserved(),
                is(foundFlight.get().getFirstReserved()));
        assertThat(flight.get().getFirstPrice(),
                is(foundFlight.get().getFirstPrice()));
        assertThat(flight.get().getBusinessReserved(),
                is(foundFlight.get().getBusinessReserved()));
        assertThat(flight.get().getBusinessPrice(),
                is(foundFlight.get().getBusinessPrice()));
        assertThat(flight.get().getEconomyReserved(),
                is(foundFlight.get().getEconomyReserved()));
        assertThat(flight.get().getEconomyPrice(),
                is(foundFlight.get().getEconomyPrice()));
        assertThat(flight.get().getIsActive(),
                is(foundFlight.get().getIsActive()));
    }

    @Test
    void addFlight_AndSaveIt() throws FlightNotSavedException {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        when(flightDao.save(flight)).thenReturn(flight);

        Integer savedAirportID = flightService.saveFlight(flight);
        assertThat(flight.getId(), is(savedAirportID));
    }

    @Test
    void findAllFlightPages() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        List<Flight> allFlights = Arrays.asList(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<Flight>(allFlights);
        when(flightDao.findAll(paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getPagedFlights(0, 10, "id");
        assertEquals(flightPage, foundFlights);
    }

    @Test
    void findAllFlightPagesFilterActive() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        List<Flight> allFlights = Arrays.asList(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<Flight>(allFlights);

        when(flightDao.findAllActive(true, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getPagedFlightsFilterActive(0, 10, true,"id");
        assertEquals(flightPage, foundFlights);
    }

    @Test
    void shouldGetFlight_ByRouteId() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);

        List<Route> routes = new ArrayList<Route>();
        Route route1 = new Route(1, originAirport, destinationAirport, true);
        Route route2 = new Route(2, originAirport, destinationAirport, true);
        Route route3 = new Route(3, originAirport, destinationAirport, true);
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        List<Flight> allFlights = Arrays.asList(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<Flight>(allFlights);

        when(flightDao.findAllByRouteIn(routes, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRoute(0, 10, "id",
                routes);
        assertEquals(flightPage, foundFlights);
    }

    @Test
    void shouldGetFlight_ByRouteId_Active() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);

        List<Route> routes = new ArrayList<Route>();
        Route route1 = new Route(1, originAirport, destinationAirport, true);
        Route route2 = new Route(2, originAirport, destinationAirport, true);
        Route route3 = new Route(3, originAirport, destinationAirport, true);
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        List<Flight> allFlights = Arrays.asList(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<Flight>(allFlights);

        when(flightDao.findAllByRouteInAndIsActiveEquals(routes, true, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRoute(0, 10, true,"id",
                routes);
        assertEquals(flightPage, foundFlights);
    }

    @Test
    public void shouldGetFlightsByRouteAndDate_FilterAll() {

        //params
        Integer pageNo = 0;
        Integer pageSize = 10;
        String sortBy = "id";

        Airport airport1 = new Airport("LAX", "Test City 1", true);
        Airport airport2 = new Airport("JFK", "Test City 2", true);

        Route route1 = new Route(1, airport1, airport2, true);
        List<Route> routes = new ArrayList<Route>();
        routes.add(route1);

        FlightQuery flightQuery = new FlightQuery(3, 16, 2021, "all");

        // mocks

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        List<Flight> flights = new ArrayList<Flight>();
        flights.add(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by(sortBy));
        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        Integer month = Integer.valueOf(flightQuery.getMonth());
        Integer date = Integer.valueOf(flightQuery.getDate());
        Integer year = Integer.valueOf(flightQuery.getYear());
        Integer hour = 00;
        Integer min = 00;

        LocalDateTime departure = LocalDateTime.of(year, month, date, hour, min);
        LocalDateTime departureHelper = LocalDateTime.of(year, month, date + 1, hour, min);

        when(flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRouteAndDate(pageNo, pageSize, sortBy,
                routes, flightQuery);
        assertEquals(flightPage, foundFlights);

    }

    @Test
    public void shouldGetFlightsByRouteAndDate_FilterMorning() {

        //params
        Integer pageNo = 0;
        Integer pageSize = 10;
        String sortBy = "id";

        Airport airport1 = new Airport("LAX", "Test City 1", true);
        Airport airport2 = new Airport("JFK", "Test City 2", true);

        Route route1 = new Route(1, airport1, airport2, true);
        List<Route> routes = new ArrayList<Route>();
        routes.add(route1);

        FlightQuery flightQuery = new FlightQuery(3, 16, 2021, "morning");

        // mocks

        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        List<Flight> flights = new ArrayList<Flight>();
        flights.add(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by(sortBy));
        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        Integer month = Integer.valueOf(flightQuery.getMonth());
        Integer date = Integer.valueOf(flightQuery.getDate());
        Integer year = Integer.valueOf(flightQuery.getYear());
        Integer hour = 00;
        Integer min = 00;

        LocalDateTime departure = LocalDateTime.of(year, month, date, 04, min);
        LocalDateTime departureHelper = LocalDateTime.of(year, month, date, 12, min);

        when(flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRouteAndDate(pageNo, pageSize, sortBy,
                routes, flightQuery);
        assertEquals(flightPage, foundFlights);

    }

    @Test
    public void shouldGetFlightsByRouteAndDate_FilterAfternoon() {

        //params
        Integer pageNo = 0;
        Integer pageSize = 10;
        String sortBy = "id";

        Airport airport1 = new Airport("LAX", "Test City 1", true);
        Airport airport2 = new Airport("JFK", "Test City 2", true);

        Route route1 = new Route(1, airport1, airport2, true);
        List<Route> routes = new ArrayList<Route>();
        routes.add(route1);

        FlightQuery flightQuery = new FlightQuery(3, 16, 2021, "afternoon");

        // mocks

        String str1 = "2020-09-01 12:01:15";
        String str2 = "2020-09-01 17:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        List<Flight> flights = new ArrayList<Flight>();
        flights.add(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by(sortBy));
        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        Integer month = Integer.valueOf(flightQuery.getMonth());
        Integer date = Integer.valueOf(flightQuery.getDate());
        Integer year = Integer.valueOf(flightQuery.getYear());
        Integer hour = 00;
        Integer min = 00;

        LocalDateTime departure = LocalDateTime.of(year, month, date, 12, min);
        LocalDateTime departureHelper = LocalDateTime.of(year, month, date, 18, min);

        when(flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRouteAndDate(pageNo, pageSize, sortBy,
                routes, flightQuery);
        assertEquals(flightPage, foundFlights);

    }

    @Test
    public void shouldGetFlightsByRouteAndDate_FilterEvening() {

        //params
        Integer pageNo = 0;
        Integer pageSize = 10;
        String sortBy = "id";

        Airport airport1 = new Airport("LAX", "Test City 1", true);
        Airport airport2 = new Airport("JFK", "Test City 2", true);

        Route route1 = new Route(1, airport1, airport2, true);
        List<Route> routes = new ArrayList<Route>();
        routes.add(route1);

        FlightQuery flightQuery = new FlightQuery(3, 16, 2021, "evening");

        // mocks

        String str1 = "2020-09-01 20:01:15";
        String str2 = "2020-09-01 21:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRoute(route1);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        List<Flight> flights = new ArrayList<Flight>();
        flights.add(flight);
        Pageable paging = PageRequest.of(0, 10, Sort.by(sortBy));
        Page<Flight> flightPage = new PageImpl<Flight>(flights);

        Integer month = Integer.valueOf(flightQuery.getMonth());
        Integer date = Integer.valueOf(flightQuery.getDate());
        Integer year = Integer.valueOf(flightQuery.getYear());
        Integer hour = 00;
        Integer min = 00;

        LocalDateTime departure = LocalDateTime.of(year, month, date, 18, min);
        LocalDateTime departureHelper = LocalDateTime.of(year, month, date + 1, 04, min);

        when(flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging)).thenReturn(flightPage);

        Page<Flight> foundFlights = flightService.getFlightsByRouteAndDate(pageNo, pageSize, sortBy,
                routes, flightQuery);
        assertEquals(flightPage, foundFlights);

    }

    @Test
    public void testUpdateflight() throws FlightNotSavedException {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        Flight flight2 = new Flight();
        flight2.setId(101);
        flight2.setRoute(route);
        flight2.setAirplane(airplane);
        flight2.setDepartureTime(departureTime);
        flight2.setArrivalTime(arrivalTime);
        flight2.setFirstReserved(0);
        flight2.setFirstPrice(350.00f);
        flight2.setBusinessReserved(0);
        flight2.setBusinessPrice(300.00f);
        flight2.setEconomyReserved(0);
        flight2.setEconomyPrice(200.00f);
        flight2.setIsActive(false);

        when(flightDao.save(flight)).thenReturn(flight);

        Integer savedFlightId = flightService.saveFlight(flight);
        Integer updatedFlightId = flightService.updateFlight(101, flight2);

        assertThat(flight.getId(), is(updatedFlightId));
    }

    @Test
    public void testDeleteFlight_NotFound() throws FlightNotSavedException {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);
        String flightMsg = flightService.deleteFlight(101);
        Optional<Flight> flightOpt = Optional.of(flight);

        assertThat(flightMsg, is("Flight not found!"));
    }

    @Test
    public void testDeleteFlight_FlightFound() throws FlightNotSavedException {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Flight flight = new Flight();
        flight.setId(101);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route = new Route(1, originAirport, destinationAirport, true);
        Airplane airplane = new Airplane(1l, 100l, 100l, 100l, "Model 1");

        flight.setRoute(route);
        flight.setAirplane(airplane);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(true);

        Optional<Flight> flightOpt = Optional.of(flight);

        when(flightDao.findById(101)).thenReturn(flightOpt);
        doNothing().when(flightDao).delete(flight);

        String deleteMsg = flightService.deleteFlight(101);

        assertThat(deleteMsg, is("Flight Deleted!"));
    }

    @Test
    void testEmailFlightDetailsToAllBookedUsers(){
        Flight flight = new Flight();
        HashSet<User> users = new HashSet<>();
        users.add(new User(1l, "name", "email", "11111111111"));
        users.add(new User(2l, "name2", "email2", "22222222222"));
        flight.setBookedUsers(users);

        assertDoesNotThrow(() -> {flightService.emailFlightDetailsToAllBookedUsers(flight);});
    }

}
