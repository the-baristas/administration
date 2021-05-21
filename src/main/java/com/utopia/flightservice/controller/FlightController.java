package com.utopia.flightservice.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.dto.FlightDto;
import com.utopia.flightservice.dto.RouteDto;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.exception.ModelMapperFailedException;
import com.utopia.flightservice.service.AirplaneService;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.RouteService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class FlightController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirplaneService airplaneService;

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
        @PostMapping("/flights")
        public ResponseEntity<FlightDto> createFlight(@RequestBody FlightDto flightDTO, UriComponentsBuilder builder) throws FlightNotSavedException {

            HttpHeaders responseHeaders = new HttpHeaders();
            URI location = builder.path("/flights/{id}").buildAndExpand(flightDTO.getId()).toUri();
            responseHeaders.setLocation(location);
            Flight flight;

            try {
                flight = convertToEntity(flightDTO);
            } catch (ParseException e) {
                throw new ModelMapperFailedException(e);
            }
            Integer flightID = flightService.saveFlight(flight);

            Flight createdFlight = flightService.getFlightById(flightID).get();
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(createdFlight));
        }

        // get single flight
        @GetMapping("/flights/{id}")
        public ResponseEntity<Flight> getFlight(@PathVariable Integer id) {
            Optional<Flight> flight = flightService.getFlightById(id);
            return new ResponseEntity(flight, HttpStatus.OK);
        }

        // update flight
        @PutMapping("flights/{id}")
        public ResponseEntity<String> updateFlight(@PathVariable Integer id, @RequestBody FlightDto flightDTO) throws FlightNotSavedException {
            Flight flight;

            try {
                flight = convertToEntity(flightDTO);
            } catch (ParseException e) {
                throw new ModelMapperFailedException(e);
            }

            Integer update = flightService.updateFlight(id, flight);
            return new ResponseEntity(update, HttpStatus.OK);
        }

        // delete a flight
        @DeleteMapping("flights/{id}")
        public ResponseEntity<String> deleteFlight(@PathVariable Integer id) throws FlightNotSavedException {
            String isRemoved = flightService.deleteFlight(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        public FlightDto convertToDto(Flight flight) {
            return modelMapper.map(flight, FlightDto.class);
        }

        public Flight convertToEntity(FlightDto flightDTO)
            throws ParseException {

            Flight flight = modelMapper.map(flightDTO, Flight.class);
            Airplane airplane = airplaneService.findAirplaneById(Long.valueOf(flightDTO.getAirplaneId()));
            Route route = routeService.getRouteById(flightDTO.getRouteId()).get();
            Timestamp departureTime = flightDTO.getDepartureTime();
            Timestamp arrivalTime = flightDTO.getArrivalTime();

            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setAirplane(airplane);
            flight.setRoute(route);
            return flight;
        }

    }
