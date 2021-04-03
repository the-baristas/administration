package com.utopia.administration.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.utopia.administration.entity.Airplane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AirplaneDaoTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirplaneDao airplaneDao;

    @AfterEach
    public void tearDown() {
        entityManager.getEntityManager()
                .createNativeQuery(
                        "alter table airplane alter id restart with 1")
                .executeUpdate();
    }

    @Test
    public void findById_Airplane() {
        Airplane airplane = new Airplane();
        airplane.setFirstClassSeatsMax(0L);
        airplane.setBusinessClassSeatsMax(0L);
        airplane.setEconomyClassSeatsMax(0L);
        entityManager.persist(airplane);
        entityManager.flush();

        Airplane foundAirplane = airplaneDao.findById(airplane.getId()).get();
        assertThat(foundAirplane.getId(), is(1L));
        assertThat(foundAirplane.getFirstClassSeatsMax(), is(0L));
        assertThat(foundAirplane.getBusinessClassSeatsMax(), is(0L));
        assertThat(foundAirplane.getEconomyClassSeatsMax(), is(0L));
    }
}
