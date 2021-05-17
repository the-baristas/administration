package com.utopia.flightservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.repository.RouteDao;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.service.RouteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RouteServiceTests {

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirportService airportService;

    @MockBean
    private RouteDao routeDao;

    @Test
    public void findAllRoutes_FindsRoutes() {
        Route route = new Route();
        route.setId(28);

        Airport airport = airportService.getAirportById("SFO");
        Airport airport2 = airportService.getAirportById("LAX");

        route.setOriginAirport(airport);
        route.setDestinationAirport(airport2);
        route.setIsActive(true);
        List<Route> allRoutes = Arrays.asList(route);
        when(routeDao.findAll()).thenReturn(allRoutes);

        List<Route> foundRoutes = routeService.getAllRoutes();
        assertEquals(allRoutes, foundRoutes);
    }

    @Test
    public void findRouteById_FindsRoute() {
        Optional<Route> route = Optional.ofNullable(new Route());
        route.get().setId(28);
        Airport airport = airportService.getAirportById("SFO");
        Airport airport2 = airportService.getAirportById("LAX");

        route.get().setOriginAirport(airport);
        route.get().setDestinationAirport(airport2);
        route.get().setIsActive(true);


        when(routeDao.findById(28)).thenReturn(route);

        Optional<Route> foundRoute = routeService.getRouteById(28);
        assertThat(route.get().getId(), is(foundRoute.get().getId()));
    }

    @Test
    public void addRoute_AndSaveIt() throws RouteNotSavedException {
        Route route = new Route();
        route.setId(28);
        Airport airport = airportService.getAirportById("SFO");
        Airport airport2 = airportService.getAirportById("LAX");

        route.setOriginAirport(airport);
        route.setDestinationAirport(airport2);
        route.setIsActive(true);
        when(routeDao.save(route)).thenReturn(route);

        Integer savedRouteID = routeService.saveRoute(route);
        assertThat(route.getId(), is(savedRouteID));
    }

}
