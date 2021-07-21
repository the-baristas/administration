package com.utopia.flightservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.repository.AirplaneRepository;
import com.utopia.flightservice.repository.FlightDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AirplaneServiceTest {
    @Mock
    private AirplaneRepository airplaneRepository;

    @Mock
    private FlightDao flightDao;

    @InjectMocks
    private AirplaneService airplaneService;

    @Test
    void findAllAirplanes_AirplanesFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Page<Airplane> airplanesPage = new PageImpl<Airplane>(
                Arrays.asList(airplane));
        Integer pageIndex = 0;
        Integer pageSize = 1;
        when(airplaneRepository.findAll(PageRequest.of(pageIndex, pageSize)))
                .thenReturn(airplanesPage);

        Page<Airplane> foundAirplanesPage = airplaneService.findAll(pageIndex,
                pageSize);
        assertEquals(airplanesPage, foundAirplanesPage);
    }

    @Test
    void findAirplaneById_ValidId_AirplaneFound() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneRepository.findById(id)).thenReturn(Optional.of(airplane));

        Airplane foundAirplane = airplaneService.findById(airplane.getId());
        assertThat(airplane, is(foundAirplane));
    }

    @Test
    void createAirplane_Airplane_AirplaneSaved() {
        Airplane airplaneToSave = new Airplane();
        airplaneToSave.setFirstClassSeatsMax(0L);
        airplaneToSave.setBusinessClassSeatsMax(0L);
        airplaneToSave.setEconomyClassSeatsMax(0L);
        Airplane airplaneToReturn = new Airplane();
        airplaneToReturn.setId(1L);
        airplaneToReturn.setFirstClassSeatsMax(0L);
        airplaneToReturn.setBusinessClassSeatsMax(0L);
        airplaneToReturn.setEconomyClassSeatsMax(0L);
        when(airplaneRepository.save(airplaneToSave))
                .thenReturn(airplaneToReturn);

        Airplane newAirplane = airplaneService.create(airplaneToSave);
        assertThat(newAirplane, is(airplaneToReturn));
    }

    @Test
    void updateAirplane_ValidAirplaneId_AirplaneUpdated() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Optional<Airplane> airplaneOptional = Optional.ofNullable(airplane);
        when(airplaneRepository.findById(id)).thenReturn(airplaneOptional);
        when(airplaneRepository.save(airplane)).thenReturn(airplane);

        Airplane updatedAirplane = airplaneService.update(airplane);
        assertThat(updatedAirplane, is(airplane));
    }

    @Test
    void deleteById_AirplaneWithNoActiveFlight_AirplaneDeleted() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneRepository.findById(id)).thenReturn(Optional.of(airplane));
        when(flightDao.areAnyActiveFlightsWithAirplane(id)).thenReturn(false);

        airplaneService.deleteById(airplane.getId());
        verify(airplaneRepository, times(1)).deleteById(airplane.getId());
    }

    @Test
    void deleteById_AirplaneWithActiveFlight_DeletionFailed() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneRepository.findById(id)).thenReturn(Optional.of(airplane));
        when(flightDao.areAnyActiveFlightsWithAirplane(id)).thenReturn(true);

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> airplaneService.deleteById(id));
        assertThat(exception.getMessage(), containsString(
                "Airplane with id: 1 which has at least one active flight cannot be deleted."));
    }
}
