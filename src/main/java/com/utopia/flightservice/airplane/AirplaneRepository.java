package com.utopia.flightservice.airplane;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    List<Airplane> findByModelContaining(String model);
}
