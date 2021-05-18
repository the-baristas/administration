package com.utopia.flightservice.controller;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

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
<<<<<<< HEAD
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
=======

>>>>>>> 556ac07824d9ce7db5f9b680d49fbc57742bcf5d
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.Destination;
=======
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
>>>>>>> 556ac07824d9ce7db5f9b680d49fbc57742bcf5d

@RestController
@RequestMapping("/routes")
public class RouteController {
<<<<<<< HEAD
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
=======

    @Autowired
    private RouteService routeService;

    // get all routes
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.getAllRoutes();
        if (routes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No routes found in database");
        } else {
            return new ResponseEntity<List<Route>>(routes, HttpStatus.OK);
        }
    }

    // get single route
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable Integer id) {
        Route route = routeService.getRouteById(id).get();
        return new ResponseEntity<Route>(route, HttpStatus.OK);
    }

    // get single route with location data
    @GetMapping("/{originId}/{destinationId}")
    public ResponseEntity<Route> getRouteByLocationInfo(
            @PathVariable String originId, @PathVariable String destinationId) {
        Route route = routeService.getRouteByLocationInfo(originId,
                destinationId);
        return new ResponseEntity<Route>(route, HttpStatus.OK);
    }

    // create a new route
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route)
            throws RouteNotSavedException {
        routeService.saveRoute(route);
        return new ResponseEntity<Route>(route, HttpStatus.CREATED);
    }

    // update route
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoute(@PathVariable Integer id,
            @RequestBody Route route) throws RouteNotSavedException {
        routeService.updateRoute(id, route);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    // delete a route
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Integer id)
            throws RouteNotSavedException {
        routeService.deleteRoute(id);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
>>>>>>> 556ac07824d9ce7db5f9b680d49fbc57742bcf5d

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
