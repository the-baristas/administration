package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.FlightQuery;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class FlightSearchController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    // get all flights, no pages
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // get flight pages
    @GetMapping("/flights/page")
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

    // get flights based on search query
    @GetMapping("/location-query")
    public ResponseEntity<List<Flight>> getFlightsByLocationQuery(@RequestParam(name = "query") String query) {
        List<Flight> flights = flightService.getFlightsByLocationQuery(query);

        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // get all flights based on location info
    @GetMapping("/flightsbylocation")
    public ResponseEntity<List<Flight>> getFlightsByRouteId(@RequestParam(name = "originId") String originId,
                                                            @RequestParam(name = "destinationId") String destinationId,
                                                            @RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy) {

        Route route = routeService.getRouteByLocationInfo(originId, destinationId);
        Integer routeId = route.getId();

        Page<Flight> flights = flightService.getFlightsByRoute(pageNo, pageSize, sortBy, routeId);
        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // get all flights based on location info and date
    @PostMapping("/flights-query")
    public ResponseEntity<List<Flight>> getFlightsByRouteAndLocation(@RequestParam(name = "originId") String originId,
                                                                     @RequestParam(name = "destinationId") String destinationId,
                                                                     @RequestBody FlightQuery flightQuery) {


        Route route = routeService.getRouteByLocationInfo(originId, destinationId);
        Integer routeId = route.getId();

        List<Flight> flights = flightService.getFlightsByRouteAndDate(routeId, flightQuery);
        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }


}