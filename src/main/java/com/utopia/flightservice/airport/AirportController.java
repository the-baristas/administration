package com.utopia.flightservice.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/utopia_airlines")
public class AirportController {
	
	@Autowired
	private AirportService airportService;
	
	// get all airports
	@GetMapping("/airport")
	public ResponseEntity<List<Airport>> getAllAirports() {
			List<Airport> airports = airportService.getAllAirports();			
			if (airports.isEmpty()) {
				return new ResponseEntity("No airports found in database", HttpStatus.NO_CONTENT);
			} else {
			    return new ResponseEntity(airports, HttpStatus.OK);
			}
	}
	
	// get single airport
	@GetMapping("/airport/{id}")
	public ResponseEntity<Airport> getAirport(@PathVariable String id) {
		Airport airport = airportService.getAirportById(id);
		if (airport == null) {
			return new ResponseEntity("No airport with that ID found!", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(airport, HttpStatus.OK);
		}
	}
	
	// create a new airport, set up controller to catch exceptions from service
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/airport")
	public ResponseEntity<String> createAirport(@RequestBody Airport airport, UriComponentsBuilder builder) throws AirportNotSavedException {
			airportService.saveAirport(airport);
			return ResponseEntity.created(builder.path("/utopia_airlines/airport/{id}").build(airport.getIataId())).build();
		}

	// update airport
	@PutMapping("/airport/{id}")
	public ResponseEntity<String> updateAirport(@PathVariable String id, @RequestBody Airport airport) throws AirportNotSavedException {
		Airport foundAirport = airportService.getAirportById(id);
		if (foundAirport != null) {
			airportService.updateAirport(id, airport);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
		
		// delete an airport
	@DeleteMapping("/airport/{id}")
	public ResponseEntity<String> deleteAirport(@PathVariable String id) throws AirportNotSavedException {
		String isRemoved = airportService.deleteAirport(id);
			return new ResponseEntity("airport deleted", HttpStatus.NO_CONTENT);
	}

}
