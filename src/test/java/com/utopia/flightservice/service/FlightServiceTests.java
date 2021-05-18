package com.utopia.flightservice.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.repository.FlightDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FlightServiceTests {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirplaneService airplaneService;

    @MockBean
    private FlightDao flightDao;

    @Test
    public void findAllFlights_FindsFlights() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Flight flight = new Flight();
        flight.setId(101);

        Route route = routeService.getRouteById(5).get();
        Airplane airplane = airplaneService.findAirplaneById(7L);

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
    public void findFlightById_FindsFlight() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Optional<Flight> flight = Optional.ofNullable(new Flight());
        flight.get().setId(101);

        Route route = routeService.getRouteById(5).get();
        Airplane airplane = airplaneService.findAirplaneById(7L);

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
        assertThat(flight.get().getAirplane(), is(foundFlight.get().getAirplane()));
        assertThat(flight.get().getDepartureTime(), is(foundFlight.get().getDepartureTime()));
        assertThat(flight.get().getArrivalTime(), is(foundFlight.get().getArrivalTime()));
        assertThat(flight.get().getFirstReserved(), is(foundFlight.get().getFirstReserved()));
        assertThat(flight.get().getFirstPrice(), is(foundFlight.get().getFirstPrice()));
        assertThat(flight.get().getBusinessReserved(), is(foundFlight.get().getBusinessReserved()));
        assertThat(flight.get().getBusinessPrice(), is(foundFlight.get().getBusinessPrice()));
        assertThat(flight.get().getEconomyReserved(), is(foundFlight.get().getEconomyReserved()));
        assertThat(flight.get().getEconomyPrice(), is(foundFlight.get().getEconomyPrice()));
        assertThat(flight.get().getIsActive(), is(foundFlight.get().getIsActive()));
    }


    @Test
    public void addAirport_AndSaveIt()
            throws AirportNotSavedException, FlightNotSavedException {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Flight flight = new Flight();
        flight.setId(101);

        Route route = routeService.getRouteById(5).get();
        Airplane airplane = airplaneService.findAirplaneById(7L);

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

}
