package com.utopia.flightservice.flight;

import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.route.Route;
import com.utopia.flightservice.route.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    // get all flights admin endpoint
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
    @PostMapping("/flights")
    public ResponseEntity<String> createFlight(@RequestBody Flight flight) throws FlightNotSavedException {
        flightService.saveFlight(flight);
        return new ResponseEntity(flight, HttpStatus.CREATED);
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

    // delete a flight
    @DeleteMapping("flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id) throws FlightNotSavedException {
        String isRemoved = flightService.deleteFlight(id);
        return new ResponseEntity("Flight deleted", HttpStatus.NO_CONTENT);
    }


}
