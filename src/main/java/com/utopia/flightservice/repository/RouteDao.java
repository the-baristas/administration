package com.utopia.flightservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface RouteDao extends JpaRepository<Route, Integer> {

    @Query("SELECT r from Route r WHERE r.isActive = ?1")
    Page<Route> findAllActive(Boolean active, Pageable paging);

    List<Route> findByOriginAirportInAndDestinationAirportIn(
            List<Airport> query1, List<Airport> query2);

    Page<Route> findByOriginAirportInOrDestinationAirportIn(
            List<Airport> query1, List<Airport> query2, Pageable paging);

    @Query("SELECT r from Route r WHERE r.isActive = ?3 AND (r.originAirport IN ?1 OR r.destinationAirport IN ?2)")
    Page<Route> findByOriginAirportInOrDestinationAirportInFilterActive(
            List<Airport> query1, List<Airport> query2, Boolean active, Pageable paging);

}
