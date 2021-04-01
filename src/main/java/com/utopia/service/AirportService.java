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
	
	public List<Airport> getAllAirports() {
		return airportDao.findAll();
	}
	
	public String saveAirport(Airport airport) {
		try { 
			airportDao.save(airport);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return airport.getIataId();
	}

}
