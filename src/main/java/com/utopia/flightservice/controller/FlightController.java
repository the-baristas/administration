package com.utopia.flightservice.controller;

import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    // get flights with pagination
    @GetMapping("/flights")
    public ResponseEntity<Page<Flight>> getPagedFlights(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy) {
        Page<Flight> flights = flightService.getPagedFlights(pageNo, pageSize, sortBy);
        if (!flights.hasContent()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // create new flight
    @PostMapping("/routes/{routeId}/flights")
    public ResponseEntity<String> createFlight(@PathVariable(value = "routeId") Integer routeId, @RequestBody Flight flight) throws FlightNotSavedException {
    try{
        Route route = routeService.getRouteById(routeId).get();
        flight.setRoute(route);
        Integer flightId = flightService.saveFlight(flight);
        Flight addedFlight = flightService.getFlightById(flightId).get();
        return new ResponseEntity(addedFlight, HttpStatus.CREATED);
    } catch (FlightNotSavedException e) {
                e.printStackTrace();
                return new ResponseEntity("Flight Not Saved!", HttpStatus.BAD_REQUEST);
            }
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
        return new ResponseEntity(update, HttpStatus.OK);
    }

    // delete a flight
    @DeleteMapping("flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id) throws FlightNotSavedException {
        String isRemoved = flightService.deleteFlight(id);
        return new ResponseEntity("Flight deleted", HttpStatus.NO_CONTENT);
    }

}
