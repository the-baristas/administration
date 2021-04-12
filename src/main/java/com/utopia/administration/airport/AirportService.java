package com.utopia.administration.airport;

import java.util.List;

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
	
	// get one airport by the iata id
	public Airport getAirportById(String id) {
		try {
			return airportDao.findByIataId(id);
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Could not find airport with Iata ID:" + id);
		}
	}
	
	// add a new airport
	public String saveAirport(Airport airport) throws AirportNotSavedException {
		try { 
			airportDao.save(airport);
			return airport.getIataId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AirportNotSavedException("ERROR! Airport not saved.");
		}
	}
	
	// update an airport's information (in progress)
	public String updateAirport(String id, Airport airport) throws AirportNotSavedException {
			try {
				if (airportDao.findByIataId(id) != null) {
					Airport foundAirport = airportDao.findByIataId(id);
					airportDao.updateAirport(id, airport.getCity(), airport.getIsActive());
				}
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find airport with ID:" + id);
			}

		return airport.getIataId();
	}
	
	// delete an airport
	public String deleteAirport(String id) throws AirportNotSavedException {
		try {
			Airport theAirport = getAirportById(id);
			airportDao.delete(theAirport);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AirportNotSavedException("ERROR! Airport not deleted.");
		}
		return "Airport Deleted!";
	}

}
