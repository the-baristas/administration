package com.utopia.flightservice.airplane;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.utopia.flightservice.airplane.Airplane;
import com.utopia.flightservice.airplane.AirplaneRepository;
import com.utopia.flightservice.airplane.AirplaneService;

@ExtendWith(MockitoExtension.class)
public class AirplaneServiceTest {
    @Mock
    private AirplaneRepository airplaneRepository;

    @InjectMocks
    private AirplaneService airplaneService;

    @Test
    public void findAllAirplanes_AirplanesFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        List<Airplane> airplanes = Arrays.asList(airplane);
        when(airplaneRepository.findAll()).thenReturn(airplanes);

        List<Airplane> foundAirplanes = airplaneService.findAllAirplanes();
        assertEquals(airplanes, foundAirplanes);
    }

    @Test
    public void findAirplaneById_ValidId_AirplaneFound() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Optional<Airplane> airplaneOptional = Optional.of(airplane);
        when(airplaneRepository.findById(id)).thenReturn(airplaneOptional);

        Airplane foundAirplane = airplaneService
                .findAirplaneById(airplane.getId());
        assertThat(airplane, is(foundAirplane));
    }

    @Test
    public void createAirplane_Airplane_AirplaneSaved() {
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

        Airplane newAirplane = airplaneService.createAirplane(airplaneToSave);
        assertThat(newAirplane, is(airplaneToReturn));
    }

    @Test
    public void updateAirplane_ValidAirplaneId_AirplaneUpdated() {
        Airplane airplane = new Airplane();
        Long id = 1L;
        airplane.setId(id);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Optional<Airplane> airplaneOptional = Optional.ofNullable(airplane);
        when(airplaneRepository.findById(id)).thenReturn(airplaneOptional);
        when(airplaneRepository.save(airplane)).thenReturn(airplane);

        Airplane updatedAirplane = airplaneService.updateAirplane(airplane);
        assertThat(updatedAirplane, is(airplane));
    }

    @Test
    public void deleteAirplane_ValidAirplaneId_AirplaneDeleted() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Optional<Airplane> airplaneOptional = Optional.ofNullable(airplane);
        when(airplaneRepository.findById(1L)).thenReturn(airplaneOptional);

        airplaneService.deleteAirplaneById(airplane.getId());
        verify(airplaneRepository, times(1)).deleteById(airplane.getId());
    }
}
