package com.utopia.flightservice.service;

import java.util.List;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.repository.AirplaneRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return airplaneRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id: " + id));
    }

    public List<Airplane> findByModelContaining(String model) {
        return airplaneRepository.findByModelContaining(model);
    }

    public Airplane createAirplane(Airplane airplane) {
        try {
            return airplaneRepository.save(airplane);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Could not create airplane with id: " + airplane.getId(),
                    exception);
        }
    }

    public Airplane updateAirplane(Airplane airplane) {
        airplaneRepository.findById(airplane.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id: "
                                + airplane.getId()));
        try {
            return airplaneRepository.save(airplane);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Could not update airplane with id: " + airplane.getId(),
                    exception);
        }
    }

    public void deleteAirplaneById(Long id) throws ResponseStatusException {
        airplaneRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = " + id));
        try {
            airplaneRepository.deleteById(id);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Could not delete airplane with id: " + id, exception);
        }
    }
}
