package com.utopia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
			List<Airport> airports = airportService.getAll();			
			if (airports.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} else {
			    return new ResponseEntity(airports, HttpStatus.OK);
			}
	}
	
	// create a new airport
	@PostMapping("/airport")
	public ResponseEntity<String> createAirport(@RequestBody Airport airport) {
		String theAirport = airportService.saveAirport(airport);
		if (theAirport == airport.getIataId()) {
			return new ResponseEntity(airport, HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
		}
		
	    
	}
	
//	
//	@DeleteMapping("/airport")
//	public Airport deleteAirport(@RequestBody Airport airport) {
//	    return airportDao.delete(airport);
//	}

}
