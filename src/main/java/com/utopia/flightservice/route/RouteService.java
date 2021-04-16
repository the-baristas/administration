package com.utopia.flightservice.route;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utopia.flightservice.route.Route;
import com.utopia.flightservice.route.RouteDao;
import com.utopia.flightservice.route.RouteNotSavedException;

@Service
public class RouteService {
	
	@Autowired
	private RouteDao routeDao;
	
	// get every route as a list
	public List<Route> getAllRoutes() {
		return routeDao.findAll();
	}
	
	// get one route by the route id
	public Optional<Route> getRouteById(Integer id) {
		return routeDao.findById(id);
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
			routeDao.updateRoute(id, route.getOriginId(), route.getDestinationId(), route.getIsActive());
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
