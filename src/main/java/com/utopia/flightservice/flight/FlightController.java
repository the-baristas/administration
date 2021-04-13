package com.utopia.flightservice.flight;

import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.route.Route;
import com.utopia.flightservice.route.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

<<<<<<< HEAD:src/main/java/com/utopia/flightservice/flight/FlightController.java
    // get all flights
=======
    @Autowired
    private RouteService routeService;

    // get all flights admin endpoint
>>>>>>> 8be00d0e223c2b662cafe1b58bce9ce3356d25d5:src/main/java/com/utopia/administration/flight/FlightController.java
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // create new flight
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/flights")
    public ResponseEntity<String> createFlight(@RequestBody Flight flight, UriComponentsBuilder builder) throws FlightNotSavedException {
        flightService.saveFlight(flight);
        return ResponseEntity.created(builder.path("/flights/{id}").build(flight.getId())).build();
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
        return new ResponseEntity("Flight Updated!", HttpStatus.OK);
    }

    // delete a flight
    @DeleteMapping("flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id) throws FlightNotSavedException {
        String isRemoved = flightService.deleteFlight(id);
        return new ResponseEntity("Flight deleted", HttpStatus.NO_CONTENT);
    }


}
