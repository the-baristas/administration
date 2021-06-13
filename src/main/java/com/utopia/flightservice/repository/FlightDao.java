package com.utopia.flightservice.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {

    Page<Flight> findAllByRouteIn(List<Route> routes, Pageable paging);

    @Query("FROM flight WHERE route_id in ?1 AND departure_time >= DATE(?2) AND departure_time <  DATE(?3)")
    List<Flight> findByRouteInAndDate(Pageable paging, List<Route> routes,
            Timestamp departure, Timestamp departureHelper);

    List<Flight> findByRouteDestinationAirport(String query);

    @Modifying
    @Query("UPDATE flight SET route_id = ?2, airplane_id = ?3, departure_time = ?4, arrival_time = ?5, first_reserved = ?6, first_price = ?7, business_reserved = ?8, business_price = ?9, economy_reserved = ?10, economy_price = ?11, is_active = ?12 WHERE id = ?1")
    void updateFlight(Integer id, Route route, Airplane airplane,
            LocalDateTime departureTime, LocalDateTime arrivalTime,
            Integer firstReserved, Float firstPrice, Integer businessReserved,
            Float businessPrice, Integer economyReserved, Float economyPrice,
            Boolean isActive);

}