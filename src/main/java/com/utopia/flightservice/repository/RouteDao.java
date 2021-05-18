package com.utopia.flightservice.repository;

import javax.transaction.Transactional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface RouteDao extends JpaRepository<Route, Integer> {

    // @Query("FROM route WHERE origin_id = ?1")
    // Optional<Route> findById(Integer id);

    @Query("FROM route WHERE origin_id = ?1")
    Route findByOriginId(String originId);

    @Query("FROM route WHERE destination_id = ?1")
    Route findByDestinationId(String destinationId);

    @Query("FROM route WHERE origin_id = ?1 AND destination_id = ?2")
    Route findByLocationInfo(String originId, String destinationId);

	List<Route> findByOriginAirportOrDestinationAirport(Airport query1, Airport query2);

    @Modifying
    @Query("UPDATE route SET origin_id = ?2, destination_id = ?3, is_active = ?4 WHERE id = ?1")
    void updateRoute(Integer id, Airport originId, Airport destinationId, Boolean isActive);



}
