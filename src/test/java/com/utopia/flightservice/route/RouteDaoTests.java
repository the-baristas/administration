package com.utopia.flightservice.route;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class RouteDaoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RouteDao dao;

    @Test
    public void testCreateAndGetRouteById() {
        Route route = new Route();
        route.setId(28);
        route.setOriginId("SFO");
        route.setDestinationId("JFK");
        route.setIsActive(1);
        entityManager.persist(route);
        entityManager.flush();


       Optional<Route> routeFromDB = dao.findById(route.getId());
        assertThat(routeFromDB.get().getId(), is(28));
        assertThat(routeFromDB.get().getOriginId(), is("SFO"));
        assertThat(routeFromDB.get().getDestinationId(), is("JFK"));
        assertThat(routeFromDB.get().getIsActive(), is(1));
    }

    @Test
    public void testUpdateRoute() {
        Route route = new Route();
        route.setId(28);
        route.setOriginId("SFO");
        route.setDestinationId("JFK");
        route.setIsActive(1);
        entityManager.persist(route);
        entityManager.flush();

        Optional<Route> routeFromDB = dao.findById(route.getId());
        routeFromDB.get().setIsActive(2);
        dao.save(routeFromDB.get());
        entityManager.persist(routeFromDB.get());
        entityManager.flush();

        assertThat(routeFromDB.get().getIsActive(), is(2));

    }

}
