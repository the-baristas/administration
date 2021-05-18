package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.utopia.flightservice.entity.Airport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
        airport.setIsActive(true);
    }


    @Test
    public void testUpdateAirport() {
        Airport airport = new Airport();
        airport.setIataId("TS6");
        airport.setCity("Test City 6");
        airport.setIsActive(true);
        entityManager.persist(airport);
        entityManager.flush();

        Airport airportFromDB = dao.findByIataId(airport.getIataId());
		assertThat(airportFromDB.getIataId(), is("TS6"));
		assertThat(airportFromDB.getCity(), is("Test City 6"));
		assertThat(airportFromDB.getIsActive(), is(true));
	}
	
	@Test
	public void testDeleteAirport() {
		Airport airport = new Airport();
		airport.setIataId("TS7");
		airport.setCity("Test City 7");
		airport.setIsActive(true);
		entityManager.persist(airport);
		entityManager.flush();

		dao.delete(airport);
        Airport airportFromDB = dao.findByIataId(airport.getIataId());
		assertThat(airportFromDB, is(null));
	}

}
