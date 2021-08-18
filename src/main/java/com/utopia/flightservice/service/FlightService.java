package com.utopia.flightservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.email.EmailSender;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.FlightQuery;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.entity.User;
import com.utopia.flightservice.exception.FlightNotSavedException;
import com.utopia.flightservice.repository.FlightDao;

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

    public Page<Flight> getFlightsByRouteAndDate(Integer pageNo, Integer pageSize, String sortBy, List<Route> routes, FlightQuery flightQuery) {


        Integer month = Integer.valueOf(flightQuery.getMonth());
        Integer date = Integer.valueOf(flightQuery.getDate());
        Integer year = Integer.valueOf(flightQuery.getYear());
        Integer hour = 00;
        Integer min = 00;

        try {
            if(flightQuery.getFilter().equals("all")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date, hour, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month, date + 1, hour, min);
                Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                return flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging);
            } else if(flightQuery.getFilter().equals("morning")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date, 04, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month, date, 12, min);
                Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                return flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging);
            } else if(flightQuery.getFilter().equals("afternoon")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date, 12, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month, date, 18, min);
                Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                return flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging);
            } else if(flightQuery.getFilter().equals("evening")) {
                LocalDateTime departure = LocalDateTime.of(year, month, date, 18, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month, date + 1, 04, min);
                Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                return flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging);
            } else {
                LocalDateTime departure = LocalDateTime.of(year, month, date, hour, min);
                LocalDateTime departureHelper = LocalDateTime.of(year, month, date + 1, hour, min);
                Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
                return flightDao.findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(routes, departure, departureHelper, paging);
            }
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find flights for those locations/dates. Try again.");
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
                    flight.getIsActive(), flight.getDepartureGate(),flight.getArrivalGate());
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


    //email flight details to all the emails of the users booked for the flight
    public void emailFlightDetailsToAllBookedUsers(Flight flight){
        for(User user : flight.getBookedUsers()){
            emailSender.sendFlightDetails(user.getEmail(), flight);
        }
    }

}
