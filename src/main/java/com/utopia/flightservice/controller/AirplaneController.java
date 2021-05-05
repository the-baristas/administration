package com.utopia.flightservice.controller;

import java.net.URI;
import java.util.List;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.service.AirplaneService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {
    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping
    public ResponseEntity<List<Airplane>> findAllAirplanes() {
    List<Airplane> airplanes = airplaneService.findAllAirplanes();
    return ResponseEntity.ok(airplanes);
    }

    @GetMapping("page")
    public ResponseEntity<Page<Airplane>> getAirplanesPage(
            @RequestParam("index") Integer pageIndex,
            @RequestParam("size") Integer pageSize) {
        Page<Airplane> airplanes = airplaneService.getAirplanesPage(pageIndex,
                pageSize);
        return ResponseEntity.ok(airplanes);
    }

    @GetMapping("{id}")
    public Airplane findAirplaneById(@PathVariable Long id) {
        return airplaneService.findAirplaneById(id);
    }

    @GetMapping("search")
    public List<Airplane> findByModelContaining(@RequestParam String model) {
        return airplaneService.findByModelContaining(model);
    }

    @PostMapping
    public ResponseEntity<Airplane> createAirplane(
            @RequestBody Airplane airplane, UriComponentsBuilder builder) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = builder.path("/airplanes/{id}")
                .buildAndExpand(airplane.getId()).toUri();
        responseHeaders.setLocation(location);
        Airplane createdAirplane = airplaneService.createAirplane(airplane);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(responseHeaders).body(createdAirplane);
    }

    @PutMapping
    public ResponseEntity<Airplane> updateAirplane(
            @RequestBody Airplane airplane) throws ResponseStatusException {
        Airplane updatedAirplane = airplaneService.updateAirplane(airplane);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAirplane);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable Long id)
            throws ResponseStatusException {
        airplaneService.deleteAirplaneById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
