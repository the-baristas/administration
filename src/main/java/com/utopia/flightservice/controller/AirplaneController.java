package com.utopia.flightservice.controller;

import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.utopia.flightservice.dto.AirplaneDto;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.exception.ModelMapperFailedException;
import com.utopia.flightservice.service.AirplaneService;

import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public AirplaneController(AirplaneService airplaneService,
            ModelMapper modelMapper) {
        this.airplaneService = airplaneService;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<AirplaneDto>> findAllAirplanes() {
        List<Airplane> airplanes = airplaneService.findAllAirplanes();
        List<AirplaneDto> airplaneDtos = airplanes.stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDtos);
    }

    @GetMapping("page")
    public ResponseEntity<Page<Airplane>> getAirplanesPage(
            @RequestParam("index") Integer pageIndex,
            @RequestParam("size") Integer pageSize) {
        Page<Airplane> airplanes = airplaneService.getAirplanesPage(pageIndex,
                pageSize);
        return ResponseEntity.ok(airplanes);
    }

    @GetMapping("/airplanes/{id}")
    public ResponseEntity<AirplaneDto> findAirplaneById(@PathVariable Long id) {
        AirplaneDto airplaneDto = convertToDto(
                airplaneService.findAirplaneById(id));
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDto);
    }

    @GetMapping("/airplanes/")
    public ResponseEntity<List<AirplaneDto>> findByModelContaining(
            @RequestParam String model) {
        List<Airplane> airplanes = airplaneService.findByModelContaining(model);
        List<AirplaneDto> airplaneDtos = airplanes.stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDtos);
    }

    @PostMapping("/airplanes")
    public ResponseEntity<AirplaneDto> createAirplane(
            @RequestBody AirplaneDto airplaneDto,
            UriComponentsBuilder builder) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = builder.path("/airplanes/{id}")
                .buildAndExpand(airplaneDto.getId()).toUri();
        responseHeaders.setLocation(location);
        Airplane airplane;
        try {
            airplane = convertToEntity(airplaneDto);
        } catch (ParseException e) {
            throw new ModelMapperFailedException(e);
        }
        Airplane createdAirplane = airplaneService.createAirplane(airplane);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(responseHeaders).body(convertToDto(createdAirplane));
    }

    @PutMapping("/airplanes")
    public ResponseEntity<AirplaneDto> updateAirplane(
            @RequestBody AirplaneDto airplaneDto)
            throws ResponseStatusException {
        Airplane airplane;
        try {
            airplane = convertToEntity(airplaneDto);
        } catch (ParseException e) {
            throw new ModelMapperFailedException(e);
        }
        Airplane updatedAirplane = airplaneService.updateAirplane(airplane);
        return ResponseEntity.status(HttpStatus.OK)
                .body(convertToDto(updatedAirplane));

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable Long id)
            throws ResponseStatusException {
        airplaneService.deleteAirplaneById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private AirplaneDto convertToDto(Airplane airplane) {
        return modelMapper.map(airplane, AirplaneDto.class);
    }

    private Airplane convertToEntity(AirplaneDto airplaneDto)
            throws ParseException {
        return modelMapper.map(airplaneDto, Airplane.class);
    }
}
