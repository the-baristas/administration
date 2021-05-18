package com.utopia.flightservice.repository;

import javax.transaction.Transactional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {

    @Query("FROM flight WHERE route_id = ?1")
    List<Flight> findByRouteId(Pageable paging, Integer id);

    @Query("FROM flight WHERE route_id = ?1 AND departure_time = ?2")
    List<Flight> findByRouteAndDate(Integer id, Timestamp departureTime);

    List<Flight> findByRouteDestinationAirport(String query);

    @Modifying
    @Query("UPDATE flight SET route_id = ?2, airplane_id = ?3, departure_time = ?4, arrival_time = ?5, first_reserved = ?6, first_price = ?7, business_reserved = ?8, business_price = ?9, economy_reserved = ?10, economy_price = ?11, is_active = ?12 WHERE id = ?1")
    void updateFlight(Integer id, Route route, Airplane airplane, Timestamp departureTime, Timestamp arrivalTime, Integer firstReserved, Float firstPrice, Integer businessReserved, Float businessPrice, Integer economyReserved, Float economyPrice, Boolean isActive);

}
