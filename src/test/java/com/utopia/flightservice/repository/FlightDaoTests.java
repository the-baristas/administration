package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Timestamp;
import java.util.Optional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;

import com.utopia.flightservice.entity.Route;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Disabled
@DataJpaTest
public class FlightDaoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightDao dao;

    @Test
    public void testCreateAndGetFlightById() {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");

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
        entityManager.persist(flight);
        entityManager.flush();


        Optional<Flight> flightFromDB = dao.findById(flight.getId());
        assertThat(flightFromDB.get().getId(), is(101));
        assertThat(flightFromDB.get().getRoute(), is(route1));
        assertThat(flightFromDB.get().getAirplane(), is(airplane));
        assertThat(flightFromDB.get().getIsActive(), is(1));
    }

    @Test
    public void testUpdateFlight() {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");


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
        entityManager.persist(flight);
        entityManager.flush();

        Flight flightFromDB = dao.findById(flight.getId()).get();
        flightFromDB.setAirplane(airplane);
        dao.save(flightFromDB);
        entityManager.persist(flightFromDB);
        entityManager.flush();

        assertThat(flightFromDB.getAirplane(), is(airplane));

    }

    @Test
    public void testDeleteFlight() {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);
        Route route1 = new Route(1, originAirport, destinationAirport, true);

        Airplane airplane = new Airplane(1L, 200L, 100L, 50L, "Boeing 787");


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
        entityManager.persist(flight);
        entityManager.flush();

        dao.delete(flight);
        Optional<Flight> flightFromDB = dao.findById(flight.getId());
        assertThat(flightFromDB.isPresent(), is(false));
    }
}