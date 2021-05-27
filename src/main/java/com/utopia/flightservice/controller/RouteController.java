package com.utopia.flightservice.controller;

import java.net.URI;
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

@RestController
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
	public ResponseEntity<List<Route>> getAllRoutes() {
			List<Route> routes = routeService.getAllRoutes();			
			if (routes.isEmpty()) {
				return new ResponseEntity("No routes found in database", HttpStatus.NO_CONTENT);
			} else {
			    return new ResponseEntity(routes, HttpStatus.OK);
			}
	}

	@GetMapping("/routes")
	public ResponseEntity<Page<Route>> getPagedRoutes(@RequestParam(defaultValue = "0") Integer pageNo,
													  @RequestParam(defaultValue = "10") Integer pageSize,
													  @RequestParam(defaultValue = "id") String sortBy) {
		Page<Route> routes = routeService.getPagedRoutes(pageNo, pageSize, sortBy);
		if (!routes.hasContent()) {
			return new ResponseEntity("No flights found in database.", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(routes, HttpStatus.OK);
		}
	}

	// get single route
	@GetMapping("/routes/{id}")
	public ResponseEntity<Route> getRoute(@PathVariable Integer id) {
		Optional<Route> route = routeService.getRouteById(id);
		return new ResponseEntity(route, HttpStatus.OK);
		}

	// get single route with location data
	@GetMapping("/routes/{originId}/{destinationId}")
	public ResponseEntity<Route> getRouteByLocationInfo(@PathVariable String originId, @PathVariable String destinationId) {
		Route route = routeService.getRouteByLocationInfo(originId, destinationId);
		return new ResponseEntity(route, HttpStatus.OK);
	}

	// get routes where origin or destination match query
	@GetMapping("/routes-query")
	public ResponseEntity<Page<Route>> getRoutesWithQuery(@RequestParam String query,
														  @RequestParam(defaultValue = "0") Integer pageNo,
														  @RequestParam(defaultValue = "10") Integer pageSize,
														  @RequestParam(defaultValue = "id") String sortBy
														  ) throws RouteNotFoundException {
		Page<Route> routes = routeService.getByOriginAirportOrDestinationAirport(pageNo, pageSize, sortBy, query, query);
		if (routes.isEmpty()) {
			return new ResponseEntity("No routes found in database matching this query!", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(routes, HttpStatus.OK);
		}
	}
	
	// create a new route
	@PostMapping("/routes")
	public ResponseEntity<RouteDto> createRoute(@RequestBody RouteDto routeDTO, UriComponentsBuilder builder) throws RouteNotSavedException {
		HttpHeaders responseHeaders = new HttpHeaders();
		URI location = builder.path("/routes/{id}").buildAndExpand(routeDTO.getId()).toUri();
		responseHeaders.setLocation(location);
		Route route;

		try {
			route = convertToEntity(routeDTO);
		} catch (ParseException e) {
			throw new ModelMapperFailedException(e);
		}
		Integer routeID = routeService.saveRoute(route);
		Route createdRoute = routeService.getRouteById(routeID).get();
		return ResponseEntity.status(HttpStatus.CREATED)
				.headers(responseHeaders)
				.body(convertToDto(createdRoute));
			}
	
	// update route
	@PutMapping("/routes/{id}")
	public ResponseEntity<RouteDto> updateRoute(@PathVariable Integer id, @RequestBody RouteDto routeDTO, UriComponentsBuilder builder) throws RouteNotSavedException {
		System.out.println(routeDTO);
		Route route;

		try {
			route = convertToEntity(routeDTO);
		} catch (ParseException e) {
			throw new ModelMapperFailedException(e);
		}
		Integer routeId = routeService.updateRoute(id, route);
		Route updatedRoute = routeService.getRouteById(routeId).get();
        return ResponseEntity.status(HttpStatus.OK).body(convertToDto(updatedRoute));
	}
	
	// delete a route
	@DeleteMapping("/routes/{id}")
	public ResponseEntity<String> deleteRoute(@PathVariable Integer id) throws RouteNotSavedException {
		String isRemoved = routeService.deleteRoute(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private RouteDto convertToDto(Route route) {
		return modelMapper.map(route, RouteDto.class);
	}

	private Route convertToEntity(RouteDto routeDTO)
			throws ParseException {
		Route route = modelMapper.map(routeDTO, Route.class);
		route.setOriginAirport(airportService.getAirportById(routeDTO.getOriginId()));
		route.setDestinationAirport(airportService.getAirportById(routeDTO.getDestinationId()));
		return route;
		}

	}
