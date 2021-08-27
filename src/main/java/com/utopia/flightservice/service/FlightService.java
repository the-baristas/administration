package com.utopia.flightservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.utopia.flightservice.email.EmailSender;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.FlightQuery;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.entity.User;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.repository.FlightDao;
import com.utopia.flightservice.repository.RouteDao;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FlightService {

    @Autowired
    private FlightDao flightDao;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private GraphService graphService;

    @Autowired
    private RouteDao routeDao;

    // get every flight as a list
    public List<Flight> getAllFlights() {
        return flightDao.findAll();
    }

    public Page<Flight> getPagedFlights(Integer pageNo, Integer pageSize,
            String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return flightDao.findAll(paging);
    }

    public Page<Flight> getFlightsByRoute(Integer pageNo, Integer pageSize,
            String sortBy, List<Route> routes) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return flightDao.findAllByRouteIn(routes, paging);
    }

    public List<List<Flight>> searchFlights(Airport originAirport,
            Airport destinationAirport, LocalDateTime startTime) {
        // get all paths based on starting place and destination
        List<GraphPath<Airport, DefaultEdge>> airportPaths = graphService
                .getPaths(originAirport, destinationAirport);

        Graph<Flight, DefaultEdge> flightGraph = new SimpleDirectedGraph<>(
                DefaultEdge.class);

        Set<Flight> firstRouteFlights = new HashSet<>();
        Set<Flight> lastRouteFlights = new HashSet<>();
        LocalDateTime endTime = startTime.plusDays(1);
        for (GraphPath<Airport, DefaultEdge> airportPath : airportPaths) {
            List<Airport> airports = airportPath.getVertexList();
            List<Route> routes = new ArrayList<Route>();

            // for each path, find the corresponding route and add to the routes
            // array
            for (int i = 0; i < airports.size() - 1; i++) {
                Airport origin = airports.get(i);
                Airport dest = airports.get(i + 1);
                routes.add(routeDao
                        .findByOriginAirportAndDestinationAirport(origin, dest)
                        .get());
            }

            // Create flight path for first route.
            int routeIndex = 0;
            List<Flight> routeZeroFlights = flightDao
                    .findByRouteAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                            routes.get(routeIndex), startTime, endTime);
            firstRouteFlights.addAll(routeZeroFlights);
            if (routeIndex == routes.size() - 1) {
                lastRouteFlights.addAll(routeZeroFlights);
            }
            routeIndex = Math.min(routeIndex + 1, routes.size() - 1);

            for (Flight routeZeroFlight : routeZeroFlights) {
                flightGraph.addVertex(routeZeroFlight);

                List<Flight> routeOneFlights = flightDao
                        .findByRouteAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes.get(routeIndex),
                                routeZeroFlight.getArrivalTime(), endTime);
                if (routeIndex == routes.size() - 1) {
                    lastRouteFlights.addAll(routeOneFlights);
                }
                routeIndex = Math.min(routeIndex + 1, routes.size() - 1);

                for (Flight routeOneFlight : routeOneFlights) {
                    flightGraph.addVertex(routeOneFlight);
                    flightGraph.addEdge(routeZeroFlight, routeOneFlight);

                    List<Flight> routeTwoFlights = flightDao
                            .findByRouteAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                    routes.get(routeIndex),
                                    routeOneFlight.getArrivalTime(), endTime);
                    if (routeIndex == routes.size() - 1) {
                        lastRouteFlights.addAll(routeTwoFlights);
                    }

                    for (Flight routeTwoFlight : routeTwoFlights) {
                        flightGraph.addVertex(routeTwoFlight);
                        flightGraph.addEdge(routeOneFlight, routeTwoFlight);
                    }
                }
            }
        }

        AllDirectedPaths<Flight, DefaultEdge> algo = new AllDirectedPaths<>(
                flightGraph);
        List<GraphPath<Flight, DefaultEdge>> flightPaths = algo
                .getAllPaths(firstRouteFlights, lastRouteFlights, true, 3);
        return flightPaths.stream()
                .map(((GraphPath<Flight, DefaultEdge> graphPath) -> graphPath
                        .getVertexList()))
                .collect(Collectors.toList());
    }

    public Page<Flight> getFlightsByRouteAndDate(Integer pageNo,
            Integer pageSize, String sortBy, List<Route> routes,
            FlightQuery startFlightQuery) {

        Integer month = startFlightQuery.getMonth();
        Integer date = startFlightQuery.getDate();
        Integer year = startFlightQuery.getYear();
        Integer hour = 0;
        Integer min = 0;

        try {
            if (startFlightQuery.getFilter().equals("all")) {
                LocalDateTime startTime = LocalDateTime.of(year, month, date,
                        hour, min);
                LocalDateTime endTime = startTime.plusDays(1);
                Pageable paging = PageRequest.of(pageNo, pageSize,
                        Sort.by(sortBy));
                return flightDao
                        .findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes, startTime, endTime, paging);
            } else if (startFlightQuery.getFilter().equals("morning")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date,
                        04, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month,
                        date, 12, min);
                Pageable paging = PageRequest.of(pageNo, pageSize,
                        Sort.by(sortBy));
                return flightDao
                        .findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes, departure, departureHelper, paging);
            } else if (startFlightQuery.getFilter().equals("afternoon")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date,
                        12, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month,
                        date, 18, min);
                Pageable paging = PageRequest.of(pageNo, pageSize,
                        Sort.by(sortBy));
                return flightDao
                        .findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes, departure, departureHelper, paging);
            } else if (startFlightQuery.getFilter().equals("evening")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date,
                        18, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month,
                        date + 1, 04, min);
                Pageable paging = PageRequest.of(pageNo, pageSize,
                        Sort.by(sortBy));
                return flightDao
                        .findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes, departure, departureHelper, paging);
            } else {
                LocalDateTime departure = LocalDateTime.of(year, month, date,
                        hour, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month,
                        date + 1, hour, min);
                Pageable paging = PageRequest.of(pageNo, pageSize,
                        Sort.by(sortBy));
                return flightDao
                        .findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
                                routes, departure, departureHelper, paging);
            }
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find flights for those locations/dates. Try again.");
        }
    }

    // get one flight by the flight id
    public Optional<Flight> getFlightById(Integer id) {
        return flightDao.findById(id);
    }

    public Integer saveFlight(Flight flight) throws FlightNotSavedException {
        try {
            Flight savedFlight = flightDao.save(flight);
            return savedFlight.getId();
        } catch (Exception e) {
            throw new FlightNotSavedException("ERROR! Flight not saved.");
        }
    }

    // update a route's information
    public Integer updateFlight(Integer id, Flight flight)
            throws FlightNotSavedException {
        try {
            flightDao.updateFlight(id, flight.getRoute(), flight.getAirplane(),
                    flight.getDepartureTime(), flight.getArrivalTime(),
                    flight.getFirstReserved(), flight.getFirstPrice(),
                    flight.getBusinessReserved(), flight.getBusinessPrice(),
                    flight.getEconomyReserved(), flight.getEconomyPrice(),
                    flight.getIsActive());
        } catch (Exception e) {
            throw new FlightNotSavedException("ERROR! Flight not updated.");
        }
        return flight.getId();
    }

    // delete a flight
    public String deleteFlight(Integer id) throws FlightNotSavedException {
        try {
            Optional<Flight> theFlight = getFlightById(id);
            if (theFlight.isPresent()) {
                flightDao.delete(theFlight.get());
            } else {
                return "Flight not found!";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FlightNotSavedException("ERROR! Flight not deleted.");
        }
        return "Flight Deleted!";
    }

    // email flight details to all the emails of the users booked for the flight
    public void emailFlightDetailsToAllBookedUsers(Flight flight) {
        for (User user : flight.getBookedUsers()) {
            emailSender.sendFlightDetails(user.getEmail(), flight);
        }
    }

}
