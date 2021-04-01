package com.utopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utopia.dao.AirportDao;
import com.utopia.entity.Airport;

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
	public String saveAirport(Airport airport) {
		try { 
			airportDao.save(airport);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return airport.getIataId();
	}
	
	// update an airport's information (in progress)
	public String updateAirport(String id, Airport airport) {
		try {
			airportDao.updateAirport(id, airport.getCity(), airport.getIsActive());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return airport.getIataId();
	}
	
	// delete an airport
	public String deleteAirport(String id) {
		try {
			Airport theAirport = getAirportById(id);
			airportDao.delete(theAirport);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Airport Deleted!";
	}

}
