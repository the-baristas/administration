package com.utopia.administration.airplane;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    public Airplane findAirplaneById(@PathVariable Long id) {
        return airplaneService.findAirplaneById(id);
    }

    @PostMapping("/airplanes")
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

    @PutMapping("/airplanes")
    public ResponseEntity<Airplane> updateAirplane(
            @RequestBody Airplane airplane) throws ResponseStatusException {
        Airplane updatedAirplane = airplaneService.updateAirplane(airplane);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAirplane);

    }

    @DeleteMapping("/airplanes/{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable Long id)
            throws ResponseStatusException {
        airplaneService.deleteAirplaneById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
