package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FlightDaoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightDao dao;

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testCreateAndGetFlightById() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");

        Flight flight = new Flight();
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
        entityManager.merge(flight);
        entityManager.flush();
        dao.save(flight);

        // Flight ID is automatically incremented starting at 1.
        Integer flightId = 1;
        Flight flightFromDB = dao.findById(flightId).get();
        assertThat(flightFromDB.getId(), is(flightId));
        assertThat(flightFromDB.getRoute(), is(route1));
        assertThat(flightFromDB.getAirplane(), is(airplane));
        assertThat(flightFromDB.getIsActive(), is(Boolean.TRUE));
    }

    @Test
    public void testUpdateFlight() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");

        Flight flight = new Flight();
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
        entityManager.merge(flight);
        entityManager.flush();
        dao.save(flight);

        Flight flightFromDB = dao.findById(flight.getId()).get();
        flightFromDB.setFirstReserved(1);
        dao.save(flightFromDB);

        assertThat(flightFromDB.getAirplane(), is(airplane));

    }

    @Test
    public void testDeleteFlight() {
        String str1 = "2020-09-01 09:01:15";
        String str2 = "2020-09-01 11:01:15";
        LocalDateTime departureTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(str2, formatter);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");

        Flight flight = new Flight();
        Integer flightId = 1;
        flight.setId(flightId);
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
        entityManager.merge(flight);
        entityManager.flush();
        dao.save(flight);

        dao.delete(flight);
        Optional<Flight> flightFromDB = dao.findById(flightId);
        assertThat(flightFromDB.isPresent(), is(false));
    }
}
