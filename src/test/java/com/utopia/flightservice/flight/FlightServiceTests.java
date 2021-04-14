package com.utopia.flightservice.flight;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.airport.Airport;
import com.utopia.flightservice.airport.AirportDao;
import com.utopia.flightservice.airport.AirportNotSavedException;
import com.utopia.flightservice.airport.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FlightServiceTests {

    @Autowired
    private FlightService flightService;

    @MockBean
    private FlightDao flightDao;

    @Test
    public void findAllFlights_FindsFlights() {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRouteId(5);
        flight.setAirplaneId(7);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(1);
        List<Flight> allFlights = Arrays.asList(flight);
        when(flightDao.findAll()).thenReturn(allFlights);

        List<Flight> foundFlights = flightService.getAllFlights();
        assertEquals(allFlights, foundFlights);
    }

    @Test
    public void findFlightById_FindsFlight() {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Optional<Flight> flight = Optional.ofNullable(new Flight());
        flight.get().setId(101);
        flight.get().setRouteId(5);
        flight.get().setAirplaneId(7);
        flight.get().setDepartureTime(departureTime);
        flight.get().setArrivalTime(arrivalTime);
        flight.get().setFirstReserved(0);
        flight.get().setFirstPrice(350.00f);
        flight.get().setBusinessReserved(0);
        flight.get().setBusinessPrice(300.00f);
        flight.get().setEconomyReserved(0);
        flight.get().setEconomyPrice(200.00f);
        flight.get().setIsActive(1);

        when(flightDao.findById(101)).thenReturn(flight);

        Optional<Flight> foundFlight = flightService.getFlightById(101);
        assertThat(flight.get().getId(), is(foundFlight.get().getId()));
        assertThat(flight.get().getRouteId(), is(foundFlight.get().getRouteId()));
        assertThat(flight.get().getAirplaneId(), is(foundFlight.get().getAirplaneId()));
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
    public void addAirport_AndSaveIt() throws AirportNotSavedException, FlightNotSavedException {
        String str1 ="2020-09-01 09:01:15";
        String str2 ="2020-09-01 11:01:15";
        Timestamp departureTime = Timestamp.valueOf(str1);
        Timestamp arrivalTime = Timestamp.valueOf(str2);

        Flight flight = new Flight();
        flight.setId(101);
        flight.setRouteId(5);
        flight.setAirplaneId(7);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setFirstReserved(0);
        flight.setFirstPrice(350.00f);
        flight.setBusinessReserved(0);
        flight.setBusinessPrice(300.00f);
        flight.setEconomyReserved(0);
        flight.setEconomyPrice(200.00f);
        flight.setIsActive(1);
        when(flightDao.save(flight)).thenReturn(flight);

        Integer savedAirportID = flightService.saveFlight(flight);
        assertThat(flight.getId(), is(savedAirportID));
    }

}
