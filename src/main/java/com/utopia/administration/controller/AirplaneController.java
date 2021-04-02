package com.utopia.administration.controller;

import java.util.List;

import com.utopia.administration.AirplaneNotFoundException;
import com.utopia.administration.entity.Airplane;
import com.utopia.administration.service.AirplaneService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirplaneController {
    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping("/airplanes")
    public List<Airplane> getAllAirplanes() {
        return airplaneService.getAllAirplanes();
    }

    @GetMapping("/airplanes/{id}")
    public Airplane getAirplaneById(@PathVariable Long id) {
        return airplaneService.getAirplaneById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    @PostMapping("/airplanes")
    @ResponseStatus(HttpStatus.CREATED)
    public Airplane createAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addAirplane(airplane);
    }
}
