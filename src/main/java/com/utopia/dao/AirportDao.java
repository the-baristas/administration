package com.utopia.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utopia.entity.Airport;

@Repository
public interface AirportDao extends JpaRepository<Airport, Integer> {

}
