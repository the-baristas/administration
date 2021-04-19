package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
				return new ResponseEntity("No airports found in database", HttpStatus.NO_CONTENT);
			} else {
			    return new ResponseEntity(airports, HttpStatus.OK);
			}
	}
	
	// get single airport
	@GetMapping("/airports/{id}")
	public ResponseEntity<Airport> getAirport(@PathVariable String id) {
		Airport airport = airportService.getAirportById(id);
		if (airport == null) {
			return new ResponseEntity("No airport with that ID found!", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(airport, HttpStatus.OK);
		}
	}

	// find airport containing letter
	@GetMapping("/airports-containing")
	public ResponseEntity<List<Airport>> getAirportContaining(@RequestParam(name = "contains") String contains) {

		List<Airport> airports = airportService.findByCityContainingLetter(contains);

		if (airports.isEmpty()) {
			return new ResponseEntity("No airports found in database matching that query.", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(airports, HttpStatus.OK);
		}
	}
	
	// create a new airport, set up controller to catch exceptions from service
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/airports")
	public ResponseEntity<String> createAirport(@RequestBody Airport airport, UriComponentsBuilder builder) throws AirportNotSavedException {
			airportService.saveAirport(airport);
			return ResponseEntity.created(builder.path("/utopia_airlines/airport/{id}").build(airport.getIataId())).build();
		}

	// update airport
	@PutMapping("/airports/{id}")
	public ResponseEntity<String> updateAirport(@PathVariable String id, @RequestBody Airport airport) throws AirportNotSavedException {
		airportService.updateAirport(id, airport);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
		
		// delete an airport
	@DeleteMapping("/airports/{id}")
	public ResponseEntity<String> deleteAirport(@PathVariable String id) throws AirportNotSavedException {
		String isRemoved = airportService.deleteAirport(id);
			return new ResponseEntity("airport deleted", HttpStatus.NO_CONTENT);
	}

}
