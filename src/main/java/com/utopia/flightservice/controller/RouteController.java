package com.utopia.flightservice.controller;

import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.dto.RouteDto;
import com.utopia.flightservice.dto.RouteQueryDto;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.entity.RouteQuery;
import com.utopia.flightservice.exception.ModelMapperFailedException;
import com.utopia.flightservice.exception.RouteNotFoundException;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.service.AirportService;
import com.utopia.flightservice.service.RouteService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.Destination;

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
	@GetMapping("/routes")
	public ResponseEntity<List<Route>> getAllRoutes() {
			List<Route> routes = routeService.getAllRoutes();			
			if (routes.isEmpty()) {
				return new ResponseEntity("No routes found in database", HttpStatus.NO_CONTENT);
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
	public ResponseEntity<List<Route>> getRoutesWithQuery(@RequestParam String query) throws RouteNotFoundException {
		List<Route> routes = routeService.getByOriginAirportOrDestinationAirport(query, query);
		if (routes.isEmpty()) {
			return new ResponseEntity("No routes found in database matching this query!", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(routes, HttpStatus.OK);
		}
	}
	
	// create a new route
	@PostMapping("/routes")
	public ResponseEntity<RouteDto> createRoute(@RequestBody RouteDto routeDTO, UriComponentsBuilder builder) throws RouteNotSavedException {
		System.out.println(routeDTO);
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
