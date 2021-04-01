package com.utopia.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utopia.entity.Airport;

@Transactional
@Repository
public interface AirportDao extends JpaRepository<Airport, Integer> {
	
	@Query("FROM airport WHERE iata_id = ?1")
	Airport findByIataId(String id);
	
	@Modifying
	@Query("UPDATE airport SET city = ?2, is_active = ?3 WHERE iata_id = ?1")
	void updateAirport(String id, String city, Integer isActive);

}
