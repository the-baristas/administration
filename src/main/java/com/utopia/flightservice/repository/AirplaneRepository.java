package com.utopia.flightservice.repository;

import java.util.List;

import com.utopia.flightservice.entity.Airplane;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    List<Airplane> findByModelContaining(String model);
}
