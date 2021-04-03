package com.utopia.administration.controller;

import java.net.URI;
import java.util.List;

import com.utopia.administration.entity.Airplane;
import com.utopia.administration.service.AirplaneService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AirplaneController {
    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping("/airplanes")
    public List<Airplane> findAllAirplanes() {
        return airplaneService.findAllAirplanes();
    }

    @GetMapping("/airplanes/{id}")
    public Airplane finalAirplaneById(@PathVariable Long id) {
        return airplaneService.findAirplaneById(id);
    }

    @PostMapping("/airplanes")
    public ResponseEntity<Airplane> createAirplane(
            @RequestBody Airplane airplane, UriComponentsBuilder builder) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = builder.path("/airplanes/{id}")
                .buildAndExpand(airplane.getId()).toUri();
        responseHeaders.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(responseHeaders).body(airplane);
    }
}
