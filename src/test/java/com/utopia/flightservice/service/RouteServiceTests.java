package com.utopia.flightservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utopia.flightservice.entity.Airplane;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.RouteNotFoundException;
import com.utopia.flightservice.exception.RouteNotSavedException;
import com.utopia.flightservice.repository.AirportDao;
import com.utopia.flightservice.repository.RouteDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

@SpringBootTest
public class RouteServiceTests {

    @Autowired
    private RouteService routeService;

    @Autowired
    private AirportService airportService;

    @MockBean
    private RouteDao routeDao;

    @MockBean
    private AirportDao airportDao;

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

    @Test
    public void test_getPagedRoutes() {
        Route route = new Route();
        route.setId(28);

        Airport airport = airportService.getAirportById("SFO");
        Airport airport2 = airportService.getAirportById("LAX");

        route.setOriginAirport(airport);
        route.setDestinationAirport(airport2);
        route.setIsActive(true);
        List<Route> allRoutes = Arrays.asList(route);
        Page<Route> routePage = new PageImpl<Route>(
                Arrays.asList(route));

        Integer pageIndex = 0;
        Integer pageSize = 1;
        String sortBy = "id";
        Pageable paging = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));
        when(routeDao.findAll(paging)).thenReturn(routePage);

        Page<Route> foundRoutes = routeService.getPagedRoutes(pageIndex, pageSize, sortBy);
        assertEquals(routePage, foundRoutes);
    }

    @Test
    public void test_getRouteByLocationInfo() {
        String originId = "SFO";
        String destinationId = "LAX";
        Integer pageIndex = 0;
        Integer pageSize = 10;
        String sortBy = "id";
        Pageable paging = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));

        Route route = new Route();
        route.setId(28);

        Airport airport = new Airport("SFO", "Test Airport", true);
        Airport airport2 = new Airport("LAX", "Test Airport 2", true);

        route.setOriginAirport(airport);
        route.setDestinationAirport(airport2);
        route.setIsActive(true);
        List<Route> allRoutes = Arrays.asList(route);

        when(airportService.getAirportById("SFO")).thenReturn(airport);
        when(airportService.getAirportById("LAX")).thenReturn(airport2);

        List<Airport> query1 = airportService.getAirportByIdOrCity(originId);
        List<Airport> query2 = airportService.getAirportByIdOrCity(destinationId);

        when(routeDao.findByOriginAirportInAndDestinationAirportIn(query1, query2)).thenReturn(allRoutes);
        when(airportDao.findByIataIdContainingOrCityContaining(originId,
                originId)).thenReturn(query1);
        when(airportDao.findByIataIdContainingOrCityContaining(destinationId,
                destinationId)).thenReturn(query2);

        List<Route> foundRoutes = routeService.getRouteByLocationInfo("SFO", "LAX");

        assertEquals(allRoutes, foundRoutes);
    }

    @Test
    public void test_getByOriginAirport_OrDestinationAirport() throws RouteNotFoundException {
        String query1 = "SFO";
        Route route = new Route();
        route.setId(28);

        Airport airport = airportService.getAirportById("SFO");
        Airport airport2 = airportService.getAirportById("LAX");
        List<Airport> airports = new ArrayList();
        airports.add(airport);

        route.setOriginAirport(airport);
        route.setDestinationAirport(airport2);
        route.setIsActive(true);
        Page<Route> routePage = new PageImpl<Route>(
                Arrays.asList(route));

        Integer pageIndex = 0;
        Integer pageSize = 10;
        String sortBy = "id";
        Pageable paging = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));

        when(airportDao.findByIataIdContainingOrCityContaining(query1, query1)).thenReturn(airports);
        when(routeDao.findByOriginAirportInOrDestinationAirportIn(airports, airports, paging)).thenReturn(routePage);

        Page<Route> foundRoutes = routeService.getByOriginAirportOrDestinationAirport(pageIndex, pageSize, sortBy, query1, query1);

        assertEquals(routePage, foundRoutes);
    }

    @Test
    public void test_updateRoute() throws RouteNotSavedException {
        Integer id = 28;
        Airport airport1 = new Airport("SFO", "Test Airport 1", true);
        Airport airport2 = new Airport("LAX", "Test Airport 2", true);
        Route route = new Route(28, airport1, airport2, true);

        doNothing().when(routeDao).updateRoute(28, airport1, airport2, true);
        Integer updateId = routeService.updateRoute(28, route);
        assertEquals(id, updateId);
    }

    @Test
    public void test_deleteRoute() throws RouteNotSavedException {
        Integer id = 28;
        Airport airport1 = new Airport("SFO", "Test Airport 1", true);
        Airport airport2 = new Airport("LAX", "Test Airport 2", true);
        Route route = new Route(28, airport1, airport2, true);

        when(routeDao.findById(id)).thenReturn(Optional.of(route));
        doNothing().when(routeDao).delete(route);
        String msg = routeService.deleteRoute(28);
        assertEquals("Route Deleted!", msg);
    }
}
