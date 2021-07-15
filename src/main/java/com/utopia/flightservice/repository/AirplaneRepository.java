package com.utopia.flightservice.repository;

import com.utopia.flightservice.entity.Airplane;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    Page<Airplane> findAll(Pageable pageable);

    Page<Airplane> findByModelContaining(String model, Pageable pageable);

    Page<Airplane> findDistinctByModelContaining(String model,
            Pageable pageable);
}
