package com.utopia.flightservice.service;

import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.RouteNotFoundException;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.repository.RouteDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
	
	@Autowired
	private RouteDao routeDao;

	@Autowired
	private AirportService airportService;
	
	// get every route as a list
	public List<Route> getAllRoutes() {
		return routeDao.findAll();
	}
	
	// get one route by the route id
	public Optional<Route> getRouteById(Integer id) {
		return routeDao.findById(id);
	}

	public Route getRouteByLocationInfo(String originId, String destinationId) {
		return routeDao.findByLocationInfo(originId, destinationId);
	}

	public List<Route> getByOriginAirportOrDestinationAirport(String query1, String query2) throws RouteNotFoundException {

		Airport airport = airportService.getAirportById(query1);
		try {
			return routeDao.findByOriginAirportOrDestinationAirport(airport, airport);
		} catch (Exception e) {
			throw new RouteNotFoundException("ERROR! No routes found.");
		}
	}
	
	// add a new route
	public Integer saveRoute(Route route) throws RouteNotSavedException {
		try { 
			routeDao.save(route);
			return route.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RouteNotSavedException("ERROR! Route not saved.");
		}
	}
	
	// update a route's information
	public Integer updateRoute(Integer id, Route route) throws RouteNotSavedException {
		try {
			routeDao.updateRoute(id, route.getOriginAirport(), route.getDestinationAirport(), route.getIsActive());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RouteNotSavedException("ERROR! Route not updated.");
		}
		return route.getId();
	}
	
	// delete an airport
	public String deleteRoute(Integer id) throws RouteNotSavedException {
		try {
			Optional<Route> theRoute = getRouteById(id);
			if (theRoute.isPresent()) {
				routeDao.delete(theRoute.get());
			} else {
				return "Route not found!";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RouteNotSavedException("ERROR! Route not deleted.");
		}
		return "Route Deleted!";
	}

}
