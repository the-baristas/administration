package com.utopia.flightservice.service;

import java.util.List;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.exception.AirportNotSavedException;
import com.utopia.flightservice.repository.AirportDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AirportService {

    @Autowired
    private AirportDao airportDao;

    // get every airport as a list
    public List<Airport> getAllAirports() {
        return airportDao.findAll();
    }

    // find airports containing letter
    public List<Airport> findByCityContainingLetter(String contains) {
        return airportDao.findByCityContaining(contains);
    }

    // get one airport by the iata id or city
    public List<Airport> getAirportByIdOrCity(String query) {
        try {
            return airportDao.findByIataIdContainingOrCityContaining(query,
                    query);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find airport with City/Iata ID:" + query);
        }
    }

    // get one airport by the iata id
    public Airport getAirportById(String id) {
        try {
            return airportDao.findByIataId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find airport with Iata ID:" + id);
        }
    }

    // add a new airport
    public String saveAirport(Airport airport) throws AirportNotSavedException {
            airportDao.save(airport);
            return airport.getIataId();
    }

    // update an airport's information (in progress)
    public String updateAirport(String id, Airport airport)
            throws AirportNotSavedException {
            if (airportDao.findByIataId(id) != null) {
                airportDao.updateAirport(id, airport.getCity(),
                        airport.getIsActive());
            } else {
                return "Airport not found!";
            }
        return airport.getIataId();
    }

    // delete an airport
    public String deleteAirport(String id) throws AirportNotSavedException {
            Airport theAirport = getAirportById(id);
            airportDao.delete(theAirport);
            return "Airport Deleted!";
    }

}
