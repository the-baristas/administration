package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.FlightQuery;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.service.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/search")
public class FlightSearchController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    // get all flights
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

    // get all flights based on location info
    @GetMapping("/flightsbylocation")
    public ResponseEntity<List<Flight>> getFlightsByRouteId(
            @RequestParam(name = "originId") String originId,
            @RequestParam(name = "destinationId") String destinationId) {

        Route route = routeService.getRouteByLocationInfo(originId,
                destinationId);
        Integer routeId = route.getId();

        List<Flight> flights = flightService.getFlightsByRoute(routeId);
        if (flights.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No flights found in database.");
        } else {
            return new ResponseEntity<List<Flight>>(flights, HttpStatus.OK);
        }
    }

    // get all flights based on location info and date
    @PostMapping("/flights-query")
    public ResponseEntity<List<Flight>> getFlightsByRouteAndLocation(
            @RequestParam(name = "originId") String originId,
            @RequestParam(name = "destinationId") String destinationId,
            @RequestBody FlightQuery flightQuery) {

        Route route = routeService.getRouteByLocationInfo(originId,
                destinationId);
        Integer routeId = route.getId();

        List<Flight> flights = flightService.getFlightsByRouteAndDate(routeId,
                flightQuery);
        if (flights.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No flights found in database.");
        } else {
            return new ResponseEntity<List<Flight>>(flights, HttpStatus.OK);
        }
    }
}
