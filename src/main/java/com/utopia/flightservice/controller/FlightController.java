package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.service.FlightService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    // get all flights admin endpoint
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No flights found in database.");
        } else {
            return new ResponseEntity<List<Flight>>(flights, HttpStatus.OK);
        }
    }

    // create new flight
    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight)
            throws FlightNotSavedException {
        flightService.saveFlight(flight);
        return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
    }

    // get single flight
    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Integer id) {
        Flight flight = flightService.getFlightById(id).get();
        return new ResponseEntity<Flight>(flight, HttpStatus.OK);
    }

    // update flight
    @PutMapping("flights/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Integer id,
            @RequestBody Flight flight) throws FlightNotSavedException {
        flightService.updateFlight(id, flight);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    // delete a flight
    @DeleteMapping("flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id)
            throws FlightNotSavedException {
        flightService.deleteFlight(id);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
