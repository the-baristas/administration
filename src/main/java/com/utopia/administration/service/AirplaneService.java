package com.utopia.administration.service;

import java.util.List;

import com.utopia.administration.AirplaneNotFoundException;
import com.utopia.administration.dao.AirplaneDao;
import com.utopia.administration.entity.Airplane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService {
    @Autowired
    private AirplaneDao airplaneDao;

    public List<Airplane> getAllAirplanes() {
        return airplaneDao.findAll();
    }

    public Airplane getAirplaneById(Long id) {
        return airplaneDao.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    public Airplane addAirplane(Airplane airplane) {
        return airplaneDao.save(airplane);
    }
}
