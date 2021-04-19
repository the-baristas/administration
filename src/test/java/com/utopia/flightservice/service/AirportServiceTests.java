package com.utopia.flightservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.repository.AirportDao;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AirportServiceTests {
	
    @Autowired
    private AirportService airportService;

    @MockBean
    private AirportDao airportDao;
    
    @Test
    public void findAllAirports_FindsAirports() {
    	Airport airport = new Airport();
    	airport.setIataId("TA4");
    	airport.setCity("Test Aiport 4");
    	airport.setIsActive(1);
    	List<Airport> allAirports = Arrays.asList(airport);
    	when(airportDao.findAll()).thenReturn(allAirports);
    	
        List<Airport> foundAirports = airportService.getAllAirports();
        assertEquals(allAirports, foundAirports);
    }
    
    @Test
    public void findAirportById_FindsAirport() {
    	Optional<Airport> airport = Optional.ofNullable(new Airport());
    	airport.get().setIataId("TA5");
    	airport.get().setCity("Test City 5");
    	airport.get().setIsActive(1);
    	

        when(airportDao.findByIataId("TA5")).thenReturn(airport.get());

        Airport foundAirport = airportService.getAirportById("TA5");
        assertThat(airport.get().getIataId(), is(foundAirport.getIataId()));
    }
    
    @Test
    public void addAirport_AndSaveIt() throws AirportNotSavedException {
    	Airport airport = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Test City 6");
        airport.setIsActive(1);
        when(airportDao.save(airport)).thenReturn(airport);

        String savedAirportID = airportService.saveAirport(airport);
        assertThat(airport.getIataId(), is(savedAirportID));
    }

}
