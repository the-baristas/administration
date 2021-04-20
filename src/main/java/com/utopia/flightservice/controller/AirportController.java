package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.service.AirportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AirportController {

    @Autowired
    private AirportService airportService;

    // get all airports
    @GetMapping("/airports")
    public ResponseEntity<List<Airport>> getAllAirports() {
        List<Airport> airports = airportService.getAllAirports();
        if (airports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No airports found in database");
        } else {
            return ResponseEntity.ok(airports);
        }
    }

    // get single airport
    @GetMapping("/airports/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable String id) {
        Airport airport = airportService.getAirportById(id);
        if (airport == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No airport with that ID found!");
        } else {
            return ResponseEntity.ok(airport);
        }
    }

    // find airport containing letter
    @GetMapping("/airports-containing")
    public ResponseEntity<List<Airport>> getAirportContaining(
            @RequestParam(name = "contains") String contains) {

        List<Airport> airports = airportService
                .findByCityContainingLetter(contains);

        if (airports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No airports found in database matching that query.");
        } else {
            return ResponseEntity.ok(airports);
        }
    }

    // create a new airport, set up controller to catch exceptions from service
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/airports/{id}")
    public ResponseEntity<String> createAirport(@RequestBody Airport airport,
            UriComponentsBuilder builder) throws AirportNotSavedException {
        airportService.saveAirport(airport);
        return ResponseEntity
                .created(builder.path("/airports/{id}")
                        .build(airport.getIataId()))
                .build();
    }

    // update airport
    @PutMapping("/airports/{id}")
    public ResponseEntity<String> updateAirport(@PathVariable String id,
            @RequestBody Airport airport) throws AirportNotSavedException {
        airportService.updateAirport(id, airport);
        return ResponseEntity.ok().build();
    }

    // delete an airport
    @DeleteMapping("/airports/{id}")
    public ResponseEntity<String> deleteAirport(@PathVariable String id)
            throws AirportNotSavedException {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
