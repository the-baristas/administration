package com.utopia.administration.route;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utopia_airlines")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	// get all routes
	@GetMapping("/route")
	public ResponseEntity<List<Route>> getAllRoutes() {
			List<Route> routes = routeService.getAllRoutes();			
			if (routes.isEmpty()) {
				return new ResponseEntity("No routes found in database", HttpStatus.NO_CONTENT);
			} else {
			    return new ResponseEntity(routes, HttpStatus.OK);
			}
	}
	
	// get single route
	@GetMapping("/route/{id}")
	public ResponseEntity<Route> getRoute(@PathVariable Integer id) {
		Optional<Route> route = routeService.getRouteById(id);
		return new ResponseEntity(route, HttpStatus.OK);
		}
	
	// create a new route
	@PostMapping("/route")
	public ResponseEntity<String> createRoute(@RequestBody Route route) throws RouteNotSavedException {
		Integer theRoute = routeService.saveRoute(route);
			return new ResponseEntity(route, HttpStatus.OK);
		}
	
	// update route
	@PutMapping("/route/{id}")
	public ResponseEntity<String> updateRoute(@PathVariable Integer id, @RequestBody Route route) throws RouteNotSavedException {
		Integer update = routeService.updateRoute(id, route);
			return new ResponseEntity("Route Updated!", HttpStatus.OK);
	}
	
	// delete a route
	@DeleteMapping("/route/{id}")
	public ResponseEntity<String> deleteRoute(@PathVariable Integer id) throws RouteNotSavedException {
		String isRemoved = routeService.deleteRoute(id);
			return new ResponseEntity("Route deleted", HttpStatus.OK);	
	}

}
