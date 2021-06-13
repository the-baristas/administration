package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.utopia.flightservice.entity.Airport;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Disabled
@DataJpaTest
public class AirportDaoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirportDao dao;

    @Test
    public void testCreateAndGetAirportById() {
        Airport airport = new Airport();
        airport.setIataId("TS6");
        airport.setCity("Test City 6");
        airport.setIsActive(Boolean.TRUE);
        entityManager.persist(airport);
        entityManager.flush();

        Airport airportFromDB = dao.findByIataId(airport.getIataId());
        assertThat(airportFromDB.getIataId(), is("TS6"));
        assertThat(airportFromDB.getCity(), is("Test City 6"));
        assertThat(airportFromDB.getIsActive(), is(1));
    }

    @Test
    public void testUpdateAirport() {
        Airport airport = new Airport();
        airport.setIataId("TS6");
        airport.setCity("Test City 6");
        airport.setIsActive(Boolean.TRUE);
        entityManager.persist(airport);
        entityManager.flush();

        Airport airportFromDB = dao.findByIataId(airport.getIataId());
        airportFromDB.setCity("Updated Test City");
        dao.save(airportFromDB);
        entityManager.persist(airportFromDB);
        entityManager.flush();

        assertThat(airportFromDB.getCity(), is("Updated Test City"));

    }

    @Test
    public void testDeleteAirport() {
        Airport airport = new Airport();
        String iataId = "TS7";
        airport.setIataId(iataId);
        airport.setCity("Test City 7");
        airport.setIsActive(Boolean.TRUE);
        entityManager.persist(airport);
        entityManager.flush();

        dao.delete(airport);
        Airport airportFromDB = dao.findByIataId(iataId);
        assertThat(airportFromDB, is(nullValue()));
    }

}
