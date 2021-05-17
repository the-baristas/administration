package com.utopia.flightservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.repository.RouteDao;
import com.utopia.flightservice.entity.Route;
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

    @Autowired
    private AirportDao airportDao;

    @Test
    public void testCreateAndGetRouteById() {
        Route route = new Route();
        route.setId(28);

        Airport originAirport1 = airportDao.findByIataId("SFO");
        Airport destinationAirport1 = airportDao.findByIataId("JFK");

        route.setOriginAirport(originAirport1);
        route.setDestinationAirport(destinationAirport1);
        route.setIsActive(true);
        entityManager.persist(route);
        entityManager.flush();


       Optional<Route> routeFromDB = dao.findById(route.getId());
        assertThat(routeFromDB.get().getId(), is(28));
        assertThat(routeFromDB.get().getOriginAirport(), is(originAirport1));
        assertThat(routeFromDB.get().getDestinationAirport(), is(destinationAirport1));
        assertThat(routeFromDB.get().getIsActive(), is(true));
    }

    @Test
    public void testUpdateRoute() {
        Route route = new Route();
        route.setId(28);
        Airport originAirport1 = airportDao.findByIataId("SFO");
        Airport destinationAirport1 = airportDao.findByIataId("JFK");

        route.setOriginAirport(originAirport1);
        route.setDestinationAirport(destinationAirport1);
        route.setIsActive(true);
        entityManager.persist(route);
        entityManager.flush();

        Optional<Route> routeFromDB = dao.findById(route.getId());
        routeFromDB.get().setIsActive(false);
        dao.save(routeFromDB.get());
        entityManager.persist(routeFromDB.get());
        entityManager.flush();

        assertThat(routeFromDB.get().getIsActive(), is(false));

    }

}
