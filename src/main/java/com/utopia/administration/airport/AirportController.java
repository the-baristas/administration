package com.utopia.administration.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping("/airport")
	public ResponseEntity<String> createAirport(@RequestBody Airport airport) throws AirportNotSavedException {
		String theAirport = airportService.saveAirport(airport);
			return new ResponseEntity(airport, HttpStatus.OK);
		}
	
	// update airport
	@PutMapping("/airport/{id}")
	public ResponseEntity<String> updateAirport(@PathVariable String id, @RequestBody Airport airport) throws AirportNotSavedException {
		String update = airportService.updateAirport(id, airport);
			return new ResponseEntity("Airport Updated!", HttpStatus.OK);
	}
		
		// delete an airport
	@DeleteMapping("/airport/{id}")
	public ResponseEntity<String> deleteAirport(@PathVariable String id) throws AirportNotSavedException {
		String isRemoved = airportService.deleteAirport(id);
			return new ResponseEntity("airport deleted", HttpStatus.OK);	
	}

}
