package com.utopia.administration.dao;

import com.utopia.administration.entity.Airplane;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneDao extends JpaRepository<Airplane, Long> {}
