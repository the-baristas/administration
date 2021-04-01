package com.utopia.controller;

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

import com.utopia.entity.Airport;
import com.utopia.service.AirportService;

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
	
	// create a new airport
	@PostMapping("/airport")
	public ResponseEntity<String> createAirport(@RequestBody Airport airport) {
		String theAirport = airportService.saveAirport(airport);
		if (theAirport == airport.getIataId()) {
			return new ResponseEntity(airport, HttpStatus.OK);
		} else {
			return new ResponseEntity("Failed to create airport", HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PutMapping("/airport/{id}")
	public ResponseEntity<String> updateAirport(@PathVariable String id, @RequestBody Airport airport) {
		String update = airportService.updateAirport(id, airport);
		if (update == airport.getIataId()) {
			return new ResponseEntity("Airport Updated!", HttpStatus.OK);
		} else {
			return new ResponseEntity("Failed to Update!", HttpStatus.EXPECTATION_FAILED);
		}
		
	}
		
		// delete an airport
	@DeleteMapping("/airport/{id}")
	public ResponseEntity<String> deleteAirport(@PathVariable String id) {
		String isRemoved = airportService.deleteAirport(id);
		if (isRemoved.isBlank()) {
			return new ResponseEntity("Failed to delete airport", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity("airport deleted", HttpStatus.OK);
		}
		
	}
		
		
	
	
//	
//	@DeleteMapping("/airport")
//	public Airport deleteAirport(@RequestBody Airport airport) {
//	    return airportDao.delete(airport);
//	}

}
