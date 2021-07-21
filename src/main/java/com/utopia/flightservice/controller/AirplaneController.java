package com.utopia.flightservice.controller;

import com.utopia.flightservice.dto.AirplaneDto;
import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.service.AirplaneService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
@RequestMapping("airplanes")
public class AirplaneController {
    private final AirplaneService airplaneService;
    private final ModelMapper modelMapper;

    public AirplaneController(AirplaneService airplaneService,
            ModelMapper modelMapper) {
        this.airplaneService = airplaneService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Page<AirplaneDto>> findAll(
            @RequestParam("index") Integer pageIndex,
            @RequestParam("size") Integer pageSize) {
        final Page<Airplane> airplanesPage = airplaneService.findAll(pageIndex,
                pageSize);
        final Page<AirplaneDto> airplaneDtosPage = airplanesPage
                .map(this::convertToDto);
        return ResponseEntity.ok(airplaneDtosPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<AirplaneDto> findById(@PathVariable Long id) {
        AirplaneDto airplaneDto = convertToDto(airplaneService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDto);
    }

    @GetMapping("search")
    public ResponseEntity<Page<AirplaneDto>> findByModelContaining(
            @RequestParam String term, @RequestParam("index") Integer pageIndex,
            @RequestParam("size") Integer pageSize) {
        Page<Airplane> airplanes = airplaneService.search(term, pageIndex,
                pageSize);
        Page<AirplaneDto> airplaneDtos = airplanes.map(this::convertToDto);
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDtos);
    }

    @GetMapping("distinct_search")
    public ResponseEntity<Page<AirplaneDto>> findDistinctByModelContaining(
            @RequestParam("term") String term,
            @RequestParam("index") Integer pageIndex,
            @RequestParam("size") Integer pageSize) {
        Page<Airplane> airplanes = airplaneService
                .findDistinctByModelContaining(term, pageIndex, pageSize);
        Page<AirplaneDto> airplaneDtos = airplanes.map(this::convertToDto);
        return ResponseEntity.status(HttpStatus.OK).body(airplaneDtos);
    }

    @PostMapping
    public ResponseEntity<AirplaneDto> create(
            @RequestBody AirplaneDto airplaneDto,
            UriComponentsBuilder builder) {
        Airplane airplane = modelMapper.map(airplaneDto, Airplane.class);
        Airplane createdAirplane = airplaneService.create(airplane);
        AirplaneDto createdAirplaneDto = modelMapper.map(createdAirplane,
                AirplaneDto.class);
        Long id = airplaneDto.getId();
        return ResponseEntity.created(builder.path("/airplanes/{id}").build(id))
                .body(createdAirplaneDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<AirplaneDto> update(
            @RequestBody AirplaneDto airplaneDto)
            throws ResponseStatusException {
        Airplane airplane = modelMapper.map(airplaneDto, Airplane.class);
        Airplane updatedAirplane = airplaneService.update(airplane);
        return ResponseEntity.status(HttpStatus.OK)
                .body(convertToDto(updatedAirplane));

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
            throws ResponseStatusException {
        airplaneService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private AirplaneDto convertToDto(Airplane airplane) {
        return modelMapper.map(airplane, AirplaneDto.class);
    }
}
