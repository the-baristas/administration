package com.utopia.flightservice.repository;

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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {

    @Query("SELECT f from Flight f WHERE f.isActive = ?1")
    Page<Flight> findAllActive(Boolean active, Pageable paging);

    Page<Flight> findAllByRouteIn(List<Route> routes, Pageable paging);

    Page<Flight> findAllByRouteInAndIsActiveEquals(List<Route> routes,
            Boolean active, Pageable paging);

    Page<Flight> findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
            List<Route> routes, LocalDateTime departure,
            LocalDateTime departureHelper, Pageable paging);

    List<Flight> findByRouteAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThan(
            Route route, LocalDateTime startTime, LocalDateTime endTime);

    Page<Flight> findByRouteInAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThanAndIsActiveEquals(
            List<Route> routes, LocalDateTime departure, Boolean active,
            LocalDateTime departureHelper, Pageable paging);

    @Modifying
    @Query("UPDATE Flight SET route_id = ?2, airplane_id = ?3, departure_time = ?4, departure_gate = ?13, arrival_time = ?5, arrival_gate = ?14, first_reserved = ?6, first_price = ?7, business_reserved = ?8, business_price = ?9, economy_reserved = ?10, economy_price = ?11, is_active = ?12 WHERE id = ?1")
    void updateFlight(Integer id, Route route, Airplane airplane,
            LocalDateTime departureTime, LocalDateTime arrivalTime,
            Integer firstReserved, Float firstPrice, Integer businessReserved,
            Float businessPrice, Integer economyReserved, Float economyPrice,
            Boolean isActive, String departureGate, String arrivalGate);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Flight f WHERE f.airplane.id = :airplaneId AND f.isActive = TRUE")
    boolean areAnyActiveFlightsWithAirplane(
            @Param("airplaneId") Long airplaneId);
}
