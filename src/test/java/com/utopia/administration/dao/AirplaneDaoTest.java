package com.utopia.administration.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.utopia.administration.entity.Airplane;

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

    @Test
    public void findById_Airplane() {
        Airplane airplane = new Airplane();
        airplane.setId(1L);
        entityManager.persist(airplane);
        entityManager.flush();

        Airplane foundAirplane = airplaneDao.findById(airplane.getId()).get();
        assertThat(foundAirplane.getId(), is(airplane.getId()));
    }
}
