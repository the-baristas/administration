package com.utopia.flightservice.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Disabled
@DataJpaTest
public class RouteDaoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RouteDao dao;

    @Test
    public void testCreateAndGetRouteById() {
        Route route = new Route();

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);

        route.setId(28);
        route.setOriginAirport(originAirport);
        route.setDestinationAirport(destinationAirport);
        route.setIsActive(true);
        entityManager.persist(route);
        entityManager.flush();


        Optional<Route> routeFromDB = dao.findById(route.getId());
        assertThat(routeFromDB.get().getId(), is(28));
        assertThat(routeFromDB.get().getOriginAirport(), is("SFO"));
        assertThat(routeFromDB.get().getDestinationAirport(), is("JFK"));
        assertThat(routeFromDB.get().getIsActive(), is(1));
    }

    @Test
    public void testUpdateRoute() {
        Route route = new Route();

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);

        route.setId(28);
        route.setOriginAirport(originAirport);
        route.setDestinationAirport(destinationAirport);
        route.setIsActive(true);
        entityManager.persist(route);
        entityManager.flush();

        Optional<Route> routeFromDB = dao.findById(route.getId());
        routeFromDB.get().setIsActive(false);
        dao.save(routeFromDB.get());
        entityManager.persist(routeFromDB.get());
        entityManager.flush();

        assertThat(routeFromDB.get().getIsActive(), is(2));
    }
}