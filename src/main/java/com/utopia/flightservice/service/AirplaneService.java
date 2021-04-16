package com.utopia.flightservice.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.exception.AirplaneNotFoundException;
import com.utopia.flightservice.repository.AirplaneRepository;

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

    public List<Airplane> findByModelContaining(String model) {
        return airplaneRepository.findByModelContaining(model);
    }

    public Airplane createAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public Airplane updateAirplane(Airplane airplane) {
        airplaneRepository.findById(airplane.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = "
                                + airplane.getId()));
        return airplaneRepository.save(airplane);
    }

    public void deleteAirplaneById(Long id) throws ResponseStatusException {
        airplaneRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = " + id));
        airplaneRepository.deleteById(id);
    }
}
