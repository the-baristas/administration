package com.utopia.flightservice.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.repository.FlightDao;
import com.utopia.flightservice.exception.FlightNotSavedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightDao flightDao;

    // get every flight as a list
    public List<Flight> getAllFlights() { return flightDao.findAll(); }

    public Page<Flight> getPagedFlights(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return flightDao.findAll(paging);
    }

    public Page<Flight> getFlightsByRoute(Integer pageNo, Integer pageSize, String sortBy, Integer routeId) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return flightDao.findAllByRouteId(routeId, paging);
    }

//    public List<Flight> getFlightsByLocationQuery(String query) { return flightDao.findByRouteDestinationAirport(query); }

//    public List<Flight> getFlightsByRouteAndDate(Integer routeId, FlightQuery flightQuery) {
//
//        Integer departureMonth = Integer.valueOf(flightQuery.getMonth());
//        Integer departureDate = Integer.valueOf(flightQuery.getDate());
//        Integer departureYear = Integer.valueOf(flightQuery.getYear());
//        Integer departureHour = Integer.valueOf(flightQuery.getHour());
//        Integer departureMinutes = Integer.valueOf(flightQuery.getMinutes());
//
//        Timestamp departureTime = Timestamp.valueOf(LocalDateTime.of(departureYear, departureMonth, departureDate, departureHour, departureMinutes));
//
//        return flightDao.findByRouteAndDate(routeId, departureTime);
//    }

    // get one flight by the flight id
    public Optional<Flight> getFlightById(Integer id) {
        return flightDao.findById(id);
    }

    public Integer saveFlight(Flight flight) throws FlightNotSavedException {
        try {
            flightDao.save(flight);
            return flight.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FlightNotSavedException("ERROR! Flight not saved.");
        }
    }

    // update a route's information
    public Integer updateFlight(Integer id, Flight flight) throws FlightNotSavedException {
        try {
            flightDao.updateFlight(id, flight.getRoute(), flight.getAirplane(), flight.getDepartureTime(), flight.getArrivalTime(), flight.getFirstReserved(), flight.getFirstPrice(), flight.getBusinessReserved(), flight.getBusinessPrice(), flight.getEconomyReserved(), flight.getEconomyPrice(), flight.getIsActive());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FlightNotSavedException("ERROR! Route not updated.");
        }
        return flight.getId();
    }

    // delete a flight
    public String deleteFlight(Integer id) throws FlightNotSavedException {
        try {
            Optional<Flight> theFlight = getFlightById(id);
            System.out.println(theFlight);
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


}
