package com.utopia.administration.airplane;

import java.util.List;

import com.utopia.administration.AirplaneNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class AirplaneService {
    private final AirplaneDao airplaneDao;

    public AirplaneService(AirplaneDao airplaneDao) {
        this.airplaneDao = airplaneDao;
    }

    public List<Airplane> findAllAirplanes() {
        return airplaneDao.findAll();
    }

    public Airplane findAirplaneById(Long id) {
        return airplaneDao.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    public Airplane createAirplane(Airplane airplane) {
        return airplaneDao.save(airplane);
    }
}
