package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        route.setOriginAirport(originAirport);
        route.setDestinationAirport(destinationAirport);
        route.setIsActive(true);
        entityManager.merge(route);
        entityManager.flush();
        dao.save(route);

        Integer firstId = 1;
        Route routeFromDB = dao.findById(firstId).get();
        assertThat(routeFromDB.getId(), is(firstId));
        assertThat(routeFromDB.getOriginAirport().getCity(), is("Test City 1"));
        assertThat(routeFromDB.getDestinationAirport().getCity(),
                is("Test City 2"));
        assertThat(routeFromDB.getIsActive(), is(Boolean.TRUE));
    }

    @Test
    public void testUpdateRoute() {
        Route route = new Route();

        Airport originAirport = new Airport("TC1", "Test City 1", true);
        Airport destinationAirport = new Airport("TC2", "Test City 2", true);

        route.setOriginAirport(originAirport);
        route.setDestinationAirport(destinationAirport);
        route.setIsActive(true);
        entityManager.merge(route);
        entityManager.flush();
        dao.save(route);

        Integer firstId = 1;
        Optional<Route> routeFromDB = dao.findById(firstId);
        routeFromDB.get().setIsActive(Boolean.FALSE);
        dao.save(routeFromDB.get());

        assertThat(routeFromDB.get().getIsActive(), is(Boolean.FALSE));
    }
}
