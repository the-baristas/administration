package com.utopia.flightservice.flight;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightDao flightDao;

    // get every flight as a list
    public List<Flight> getAllFlights() {
        return flightDao.findAll();
    }

    public List<Flight> getFlightsByRoute(Integer routeId) { return flightDao.findByRouteId(routeId); }

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
            flightDao.updateFlight(id,
                            flight.getRouteId(),
                            flight.getAirplaneId(),
                            flight.getDepartureTime(),
                            flight.getArrivalTime(),
                            flight.getFirstReserved(),
                            flight.getFirstPrice(),
                            flight.getBusinessReserved(),
                            flight.getBusinessPrice(),
                            flight.getEconomyReserved(),
                            flight.getEconomyPrice(),
                            flight.getIsActive());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FlightNotSavedException("ERROR! Route not updated.");
        }
        return flight.getId();
    }

    // delete an airport
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




}
