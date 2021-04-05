package com.utopia.administration.airplane;

import java.util.List;

import com.utopia.administration.AirplaneNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<Airplane> findAllAirplanes() {
        return airplaneRepository.findAll();
    }

    public Airplane findAirplaneById(Long id) {
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    public Airplane createAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }
}
