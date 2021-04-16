package com.utopia.flightservice.airport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.utopia.flightservice.airport.Airport;
import com.utopia.flightservice.airport.AirportDao;

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
		airport.setIsActive(1);
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
		airport.setIsActive(1);
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
		airport.setIataId("TS7");
		airport.setCity("Test City 7");
		airport.setIsActive(1);
		entityManager.persist(airport);
		entityManager.flush();
		
		dao.delete(airport);
		Airport airportFromDB = dao.findByIataId(airport.getIataId());
		assertThat(airportFromDB).isNull();
	}
	

}
