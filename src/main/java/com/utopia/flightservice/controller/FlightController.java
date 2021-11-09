package com.utopia.flightservice.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.utopia.flightservice.dto.FlightDto;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.FlightQuery;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.exception.ModelMapperFailedException;
import com.utopia.flightservice.service.AirplaneService;
import com.utopia.flightservice.service.AirportService;
import com.utopia.flightservice.service.AwsS3Service;
import com.utopia.flightservice.service.FlightService;
import com.utopia.flightservice.service.RouteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@RestController
@SecurityScheme(name = "bearer", // can be set to anything
        type = SecuritySchemeType.HTTP, scheme = "bearer")
@OpenAPIDefinition(info = @Info(title = "Flight Service", version = "v1"),
        security = @SecurityRequirement(name = "bearer"))
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FlightService flightService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirportService airportService;

    @Autowired
    private AwsS3Service s3Service;

    @GetMapping("/health")
    public String checkHealth() {
        return "In the words of the poet SZA, 'I be looking good, I've been feeling nice, working on my aura.'";
    }

    // get flights with pagination
    @GetMapping("")
    public ResponseEntity<Page<Flight>> getPagedFlights(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(name = "activeOnly",
                    required = false) Boolean activeOnly) {

        Page<Flight> flights;
        if (activeOnly == null || !activeOnly) {
            flights = flightService.getPagedFlights(pageNo, pageSize, sortBy);
        } else {
            flights = flightService.getPagedFlightsFilterActive(pageNo,
                    pageSize, true, sortBy);
        }
        if (!flights.hasContent()) {
            return new ResponseEntity("No flights found in database.",
                    HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }

    }

    // get all flights based on location info
    @GetMapping("/search")
    public ResponseEntity<Page<Flight>> getFlightsByRouteId(
            @RequestParam(name = "originId") String originId,
            @RequestParam(name = "destinationId") String destinationId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(name = "activeOnly",
                    required = false) Boolean activeOnly) {

        List<Route> routes = routeService.getRouteByLocationInfo(originId,
                destinationId);

        Page<Flight> flights;

        if (activeOnly == null || !activeOnly) {
            flights = flightService.getFlightsByRoute(pageNo, pageSize, sortBy,
                    routes);
        } else {
            flights = flightService.getFlightsByRoute(pageNo, pageSize, true,
                    sortBy, routes);
        }

        if (flights.isEmpty()) {
            return new ResponseEntity("No flights found in database.",
                    HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(flights, HttpStatus.OK);
        }
    }

    // get all flights based on location info and date
    @PostMapping("query")
    public ResponseEntity<Page<Flight>> getFlightsByRouteAndDate(
            @RequestParam String originId, @RequestParam String destinationId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @Valid @RequestBody FlightQuery flightQuery)
            throws ResponseStatusException {

        List<Route> routes = routeService.getRouteByLocationInfo(originId,
                destinationId);

        try {
            Page<Flight> flights = flightService.getFlightsByRouteAndDate(
                    pageNo, pageSize, sortBy, routes, flightQuery);
            return ResponseEntity.ok(flights);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find flights for those locations/dates. Try again.");
        }
    }

    // create new flight
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<FlightDto> createFlight(
            @RequestBody FlightDto flightDTO, UriComponentsBuilder builder)
            throws FlightNotSavedException {

        Flight flight;

        try {
            flight = convertToEntity(flightDTO);
        } catch (ParseException e) {
            throw new ModelMapperFailedException(e);
        }
        Integer flightID = flightService.saveFlight(flight);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = builder.path("/flights/{id}").buildAndExpand(flightID)
                .toUri();
        responseHeaders.setLocation(location);

        Flight createdFlight = flightService.getFlightById(flightID).get();
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(responseHeaders).body(convertToDto(createdFlight));
    }

    // get single flight
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Integer id) {
        Optional<Flight> flight = flightService.getFlightById(id);
        return new ResponseEntity(flight, HttpStatus.OK);
    }

    // update flight
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Integer id,
            @RequestBody FlightDto flightDTO) throws FlightNotSavedException {
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id)
            throws FlightNotSavedException {
        String isRemoved = flightService.deleteFlight(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // email flight details to all users that have booked tickets for that
    // flight
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/email/{flightId}")
    public ResponseEntity<String> emailFlightDetailsToAll(
            @PathVariable Integer flightId) {
        flightService.emailFlightDetailsToAllBookedUsers(
                flightService.getFlightById(flightId).get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/new-search")
    public ResponseEntity<Page<List<Flight>>> getFlightsWithLayovers(
            @RequestParam String originId, @RequestParam String destinationId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @Valid @RequestBody FlightQuery flightQuery)
            throws ResponseStatusException {

        Integer month = flightQuery.getMonth();
        Integer date = flightQuery.getDate();
        Integer year = flightQuery.getYear();
        Integer hour = 0;
        Integer min = 0;

        LocalDateTime dateTime = LocalDateTime.of(year, month, date, hour, min);

        try {
            Airport origin = airportService.getAirportByIdOrCity(originId)
                    .get(0);
            Airport dest = airportService.getAirportByIdOrCity(destinationId)
                    .get(0);
            Page<List<Flight>> trips = flightService.searchFlights(origin, dest,
                    dateTime, sortBy, flightQuery, pageNo, pageSize);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find flights based on query.");
        }
    }

    // Upload a file (a csv file with flight information to AWS S3 which will
    // then be handled by a lambda
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/csv")
    public ResponseEntity<String> uploadFlightCsv(
            @RequestParam("file") MultipartFile file) throws IOException {
        s3Service.uploadFlightCsv(file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // converters

    public FlightDto convertToDto(Flight flight) {
        return modelMapper.map(flight, FlightDto.class);
    }

    public Flight convertToEntity(FlightDto flightDTO) throws ParseException {

        Flight flight = modelMapper.map(flightDTO, Flight.class);
        Airplane airplane = airplaneService
                .findById(Long.valueOf(flightDTO.getAirplaneId()));
        Route route = routeService.getRouteById(flightDTO.getRouteId()).get();
        LocalDateTime departureTime = flightDTO.getDepartureTime();
        LocalDateTime arrivalTime = flightDTO.getArrivalTime();

        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setAirplane(airplane);
        flight.setRoute(route);
        return flight;
    }

}
