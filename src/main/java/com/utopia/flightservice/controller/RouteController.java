package com.utopia.flightservice.controller;

import java.util.List;

import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.service.RouteService;

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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/routes")
public class RouteController {

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

}
