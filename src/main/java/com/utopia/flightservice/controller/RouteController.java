package com.utopia.flightservice.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.dto.RouteDto;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.ModelMapperFailedException;
import com.utopia.flightservice.exception.RouteNotFoundException;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.service.AirportService;
import com.utopia.flightservice.service.RouteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final ModelMapper modelMapper;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirportService airportService;

    public RouteController(RouteService routeService, ModelMapper modelMapper) {
        this.routeService = routeService;
        this.modelMapper = modelMapper;
    }

    // get all routes
    @GetMapping("/all")
    public ResponseEntity<List<Route>> getAllRoutes()
            throws ResponseStatusException {
        List<Route> routes = routeService.getAllRoutes();
        if (routes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No routes found in database");
        } else {
            return ResponseEntity.ok(routes);
        }
    }

    @GetMapping("")
    public ResponseEntity<Page<Route>> getPagedRoutes(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<Route> routes = routeService.getPagedRoutes(pageNo, pageSize,
                sortBy);
        if (!routes.hasContent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No routes found in database");
        } else {
            return ResponseEntity.ok(routes);
        }
    }

    // get single route
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable Integer id) {
        Optional<Route> route = routeService.getRouteById(id);
        return ResponseEntity.ok(route.get());
    }

    // get single route with location data
    @GetMapping("/{originId}/{destinationId}")
    public ResponseEntity<List<Route>> getRouteByLocationInfo(
            @PathVariable String originId, @PathVariable String destinationId) {
        List<Route> routes = routeService.getRouteByLocationInfo(originId,
                destinationId);
        return ResponseEntity.ok(routes);
    }

    // get routes where origin or destination match query
    @GetMapping("/routes-query")
    public ResponseEntity<Page<Route>> getRoutesWithQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
            throws RouteNotFoundException {

        Page<Route> routes = routeService
                .getByOriginAirportOrDestinationAirport(pageNo, pageSize,
                        sortBy, query, query);
        if (routes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No routes found in database matching this query!");
        } else {
            return ResponseEntity.ok(routes);
        }
    }

    // create a new route
    @PostMapping("")
    public ResponseEntity<RouteDto> createRoute(@RequestBody RouteDto routeDTO,
            UriComponentsBuilder builder) throws RouteNotSavedException {
        Route route;

        try {
            route = convertToEntity(routeDTO);
        } catch (ParseException e) {
            throw new ModelMapperFailedException(e);
        }
        Integer routeID = routeService.saveRoute(route);
        Route createdRoute = routeService.getRouteById(routeID).get();
        RouteDto routeDto = convertToDto(createdRoute);
        return ResponseEntity
                .created(builder.path("/routes/{id}").build(routeDTO.getId()))
                .body(routeDto);
    }

    // update route
    @PutMapping("/{id}")
    public ResponseEntity<RouteDto> updateRoute(@PathVariable Integer id,
            @RequestBody RouteDto routeDTO, UriComponentsBuilder builder)
            throws RouteNotSavedException {
        Route route;

        try {
            route = convertToEntity(routeDTO);
        } catch (ParseException e) {
            throw new ModelMapperFailedException(e);
        }
        Integer routeId = routeService.updateRoute(id, route);
        Route updatedRoute = routeService.getRouteById(routeId).get();
        return ResponseEntity.ok(convertToDto(updatedRoute));
    }

    // delete a route
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Integer id)
            throws RouteNotSavedException {
        String isRemoved = routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    private RouteDto convertToDto(Route route) {
        return modelMapper.map(route, RouteDto.class);
    }

    private Route convertToEntity(RouteDto routeDTO) throws ParseException {
        Route route = modelMapper.map(routeDTO, Route.class);
        route.setOriginAirport(
                airportService.getAirportById(routeDTO.getOriginId()));
        route.setDestinationAirport(
                airportService.getAirportById(routeDTO.getDestinationId()));
        return route;
    }

}
