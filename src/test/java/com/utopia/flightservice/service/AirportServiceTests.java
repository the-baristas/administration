package com.utopia.flightservice.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.repository.AirportDao;

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
    	airport.setIsActive(true);
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
    	airport.get().setIsActive(true);


        when(airportDao.findByIataId("TA5")).thenReturn(airport.get());

        Airport foundAirport = airportService.getAirportById("TA5");
        assertThat(airport.get().getIataId(), is(foundAirport.getIataId()));
    }

    @Test
    public void addAirport_AndSaveIt() throws AirportNotSavedException {
        Airport airport = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Test City 6");
        airport.setIsActive(true);
        when(airportDao.save(airport)).thenReturn(airport);

        String savedAirportID = airportService.saveAirport(airport);
        assertThat(airport.getIataId(), is(savedAirportID));
    }

    @Test
    public void test_findByCityContaining() {

        Airport airport = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Test City 6");
        airport.setIsActive(true);
        List<Airport> airports = Arrays.asList(airport);
        when(airportDao.findByCityContaining("Test")).thenReturn(airports);

        List <Airport> foundAirports = airportService.findByCityContainingLetter("Test");
        assertEquals(airports, foundAirports);
    }

    @Test
    public void test_updateAirport() throws AirportNotSavedException {
        Airport airport = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Test City 6");
        airport.setIsActive(true);

        Airport airport2 = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Updated Test City 6");
        airport.setIsActive(true);

        when(airportDao.findByIataId("TA6")).thenReturn(airport);
        doNothing().when(airportDao).updateAirport("TA6", "Updated Test City 6", true);

        String msg = airportService.updateAirport("TA6", airport);
        assertEquals("TA6", msg);
    }

    @Test
    public void test_deleteAirport() throws AirportNotSavedException {

        Airport airport = new Airport();
        airport.setIataId("TA6");
        airport.setCity("Test City 6");
        airport.setIsActive(true);

        when(airportDao.findByIataId("TA6")).thenReturn(airport);
        doNothing().when(airportDao).delete(airport);

        String msg = airportService.deleteAirport("TA6");
        assertEquals("Airport Deleted!", msg);
    }
}
