package com.utopia.flightservice.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return airportDao.findByIataId(id);
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
			airportDao.updateAirport(id, airport.getCity(), airport.getIsActive());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AirportNotSavedException("ERROR! Airport not updated.");
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
