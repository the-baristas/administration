package com.utopia.administration.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.administration.dao.AirplaneDao;
import com.utopia.administration.entity.Airplane;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AirplaneServiceTest {
    @Autowired
    private AirplaneService airplaneService;

    @MockBean
    private AirplaneDao airplaneDao;

    @Test
    public void findAllAirplanes_AirplanesFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        List<Airplane> airplanes = Arrays.asList(airplane);
        when(airplaneDao.findAll()).thenReturn(airplanes);

        List<Airplane> foundAirplanes = airplaneService.findAllAirplanes();
        assertEquals(airplanes, foundAirplanes);
    }

    @Test
    public void findAirplaneById_ValidId_AirplaneFound() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        Optional<Airplane> optionalAirplane = Optional.of(airplane);
        when(airplaneDao.findById(airplane.getId()))
                .thenReturn(optionalAirplane);

        Airplane foundAirplane = airplaneService
                .findAirplaneById(airplane.getId());
        assertThat(airplane, is(foundAirplane));
    }

    @Test
    public void createAirplane_Airplane_AirplaneSaved() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        when(airplaneDao.save(airplane)).thenReturn(airplane);

        Airplane savedAirplane = airplaneService.createAirplane(airplane);
        assertThat(airplane, is(savedAirplane));
    }
}
