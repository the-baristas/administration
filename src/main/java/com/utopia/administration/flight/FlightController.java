package com.utopia.administration.flight;

import java.util.List;
import java.util.Optional;

import com.utopia.administration.airport.Airport;
import com.utopia.administration.airport.AirportService;
import com.utopia.administration.route.Route;
import com.utopia.administration.route.RouteNotSavedException;
import com.utopia.administration.route.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/flightservice")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    // get all airports
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // create new flight
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/flights")
    public ResponseEntity<String> createFlight(@RequestBody Flight flight, UriComponentsBuilder builder) throws FlightNotSavedException {
        flightService.saveFlight(flight);
        return ResponseEntity.created(builder.path("/flightadmin/flights/{id}").build(flight.getId())).build();
    }

    // get single flight
    @GetMapping("/flights/{id}")
    public ResponseEntity<Route> getFlight(@PathVariable Integer id) {
        Optional<Flight> flight = flightService.getFlightById(id);
        return new ResponseEntity(flight, HttpStatus.OK);
    }

    // update flight
    @PutMapping("flights/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Integer id, @RequestBody Flight flight) throws FlightNotSavedException {
        Integer update = flightService.updateFlight(id, flight);
        return new ResponseEntity("Flight Updated!", HttpStatus.OK);
    }

    // delete a route
    @DeleteMapping("flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id) throws FlightNotSavedException {
        String isRemoved = flightService.deleteFlight(id);
        return new ResponseEntity("Flight deleted", HttpStatus.NO_CONTENT);
    }


}
