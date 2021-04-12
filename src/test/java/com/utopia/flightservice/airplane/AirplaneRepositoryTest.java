package com.utopia.flightservice.airplane;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AirplaneRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Test
    public void findById_Airplane() {
        Airplane airplane = new Airplane();
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        entityManager.persist(airplane);
        entityManager.flush();

        Airplane foundAirplane = airplaneRepository.findById(airplane.getId())
                .get();
        assertThat(foundAirplane.getId(), is(1L));
        assertThat(foundAirplane.getFirstClassSeatsMax(), is(0L));
        assertThat(foundAirplane.getBusinessClassSeatsMax(), is(0L));
        assertThat(foundAirplane.getEconomyClassSeatsMax(), is(0L));
    }

    @Test
    public void createAirplane_newAirplane() {
        Airplane airplaneToCreate = new Airplane();
        airplaneToCreate.setFirstClassSeatsMax(0L);
        airplaneToCreate.setBusinessClassSeatsMax(0L);
        airplaneToCreate.setEconomyClassSeatsMax(0L);
        entityManager.persistAndFlush(airplaneToCreate);

        Airplane newAirplane = airplaneRepository.save(airplaneToCreate);
        assertThat(airplaneToCreate, is(newAirplane));
    }
}
