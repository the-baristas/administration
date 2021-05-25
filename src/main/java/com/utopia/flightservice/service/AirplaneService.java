package com.utopia.flightservice.service;

import java.util.List;

import com.utopia.flightservice.exception.AirplaneNotFoundException;

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
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
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

    public String deleteAirplaneById(Long id) throws ResponseStatusException {
        airplaneRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = " + id));
        airplaneRepository.deleteById(id);
        return "Airplane Deleted!";
    }
}
