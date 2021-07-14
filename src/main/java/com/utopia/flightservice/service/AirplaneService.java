package com.utopia.flightservice.service;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.exception.AirplaneNotFoundException;
import com.utopia.flightservice.repository.AirplaneRepository;
import com.utopia.flightservice.repository.FlightDao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final FlightDao flightDao;

    public Page<Airplane> findAll(Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return airplaneRepository.findAll(pageable);
    }

    public Page<Airplane> search(String term, Integer pageIndex,
            Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return airplaneRepository.findByModelContaining(term, pageable);
    }

    public Page<Airplane> findDistinctByModelContaining(String term,
            Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return airplaneRepository.findDistinctByModelContaining(term, pageable);
    }

    public Airplane findById(Long id) {
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    public Page<Airplane> findByModelContaining(String model, Integer pageIndex,
            Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return airplaneRepository.findByModelContaining(model, pageable);
    }

    public Airplane create(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public Airplane update(Airplane airplane) {
        airplaneRepository.findById(airplane.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = "
                                + airplane.getId()));
        return airplaneRepository.save(airplane);
    }

    public void deleteById(Long id) throws ResponseStatusException {
        airplaneRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Could not find airplane with id = " + id));
        if (flightDao.areAnyActiveFlightsWithAirplane(id)) {
            String reason = String.format(
                    "Airplane with id: %s which has at least one active flight cannot be deleted.",
                    id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        } else {
            airplaneRepository.deleteById(id);
        }
    }
}
