package com.utopia.flightservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.dto.RouteDto;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.exception.RouteNotFoundException;
import com.utopia.flightservice.service.AirportService;
import com.utopia.flightservice.service.RouteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = { "ROLE_ADMIN" })
public class RouteControllerTests {

    // import mock mvc
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RouteService routeService;

    @MockBean
    AirportService airportService;

    // Route Controller Tests
    @Autowired
    private RouteController controller;

    // Route Controller Is Not Null
    @Test
    public void controllerLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void test_getRouteById() throws Exception {

        List<Route> routes = new ArrayList<>();
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport originAirport2 = airportService.getAirportById("MSP");

        Airport destinationAirport1 = airportService.getAirportById("LAX");
        Airport destinationAirport2 = airportService.getAirportById("JFK");

        Route route1 = new Route(25, originAirport1, destinationAirport1, true);
        Route route2 = new Route(26, originAirport2, destinationAirport2, true);
        routes.add(route1);
        routes.add(route2);

        when(routeService.getRouteById(25)).thenReturn(Optional.of(route1));

        mockMvc.perform(get("/routes/25").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(route1.getId()));

    }

    @Test
    public void test_getAllRoutes() throws Exception {

        List<Route> routes = new ArrayList<>();
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport originAirport2 = airportService.getAirportById("MSP");

        Airport destinationAirport1 = airportService.getAirportById("LAX");
        Airport destinationAirport2 = airportService.getAirportById("JFK");

        Route route1 = new Route(25, originAirport1, destinationAirport1, true);
        Route route2 = new Route(26, originAirport2, destinationAirport2, true);
        routes.add(route1);
        routes.add(route2);

        when(routeService.getAllRoutes()).thenReturn(routes);

        mockMvc.perform(get("/routes/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void test_getRouteByLocation() throws Exception {

        List<Route> routes = new ArrayList<>();

        Airport originAirport = new Airport("SFO", "Test City 1", true);
        Airport destinationAirport = new Airport("LAX", "Test City 2", true);

        Route route1 = new Route(25, originAirport, destinationAirport, true);
        routes.add(route1);

        when(routeService.getRouteByLocationInfo("SFO", "LAX")).thenReturn(routes);

        String originId = "SFO";
        String destinationId = "LAX";

        mockMvc.perform(get("/routes/{originId}/{destinationId}", originId, destinationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void test_getRouteWithQuery() throws Exception, RouteNotFoundException {

        List<Route> routes = new ArrayList<>();

        Airport originAirport = new Airport("SFO", "Test City 1", true);
        Airport destinationAirport = new Airport("LAX", "Test City 2", true);

        Route route1 = new Route(25, originAirport, destinationAirport, true);
        routes.add(route1);

        String query = "LAX";
        Page<Route> routePage = new PageImpl<Route>(routes);

        when(routeService.getByOriginAirportOrDestinationAirport(0, 10, "id", query, query)).thenReturn(routePage);


        mockMvc.perform(get("/routes/routes-query?query={query}&pageNo=0&pageSize=10&sortBy=id", query)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

    }

    @Test
    public void test_getAllRoutes_statusOkAndListLength() throws Exception {
        List<Route> routes = new ArrayList<>();
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport originAirport2 = airportService.getAirportById("MSP");

        Airport destinationAirport1 = airportService.getAirportById("LAX");
        Airport destinationAirport2 = airportService.getAirportById("JFK");

        Route route1 = new Route(25, originAirport1, destinationAirport1, true);
        Route route2 = new Route(26, originAirport2, destinationAirport2, true);
        routes.add(route1);
        routes.add(route2);

        Page<Route> routePage = new PageImpl<Route>(routes);

        when(routeService.getPagedRoutes(0, 10, "id")).thenReturn(routePage);

        // create list of airports, pass it to thenReturn to test that getting
        // back list of airports

        mockMvc.perform(get("/routes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    public void shouldCreateRoute() throws Exception {
        RouteDto routeDTO = makeRouteDTO();
        Route mockRoute = makeRoute();
        Airport origin = new Airport("TA1", "Test City 1", true);
        Airport destination = new Airport("TA2", "Test City 2", true);

        when(airportService.getAirportById(routeDTO.getOriginId()))
                .thenReturn(mockRoute.getOriginAirport());
        when(airportService.getAirportById(routeDTO.getDestinationId()))
                .thenReturn(mockRoute.getDestinationAirport());
        when(routeService.getRouteById(mockRoute.getId()))
                .thenReturn(Optional.of(mockRoute));
        when(routeService.saveRoute(mockRoute)).thenReturn(mockRoute.getId());

        mockMvc.perform(post("/routes").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(routeDTO)))
                .andExpect(status().isCreated());

        verify(routeService).saveRoute(mockRoute);

    }

    @Test
    public void shouldUpdateRoute() throws Exception {
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport destinationAirport2 = airportService.getAirportById("JFK");

        Route route = new Route(28, originAirport1, destinationAirport2, true);
        routeService.saveRoute(route);
        Route updatedRoute = new Route(28, originAirport1, destinationAirport2,
                false);

        when(routeService.updateRoute(route.getId(), updatedRoute))
                .thenReturn(updatedRoute.getId());
        Optional<Route> opt = Optional.of(updatedRoute);
        when(routeService.getRouteById(28)).thenReturn(opt);

        mockMvc.perform(
                put("/routes/28").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRoute)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteRoute() throws Exception {
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport destinationAirport1 = airportService.getAirportById("LAX");

        Route mockRoute = new Route(29, originAirport1, destinationAirport1,
                true);
        routeService.saveRoute(mockRoute);
        when(routeService.deleteRoute(mockRoute.getId()))
                .thenReturn(mockRoute.getId().toString());

        mockMvc.perform(
                delete("/routes/29").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockRoute)))
                .andExpect(status().isNoContent());
    }

    private RouteDto makeRouteDTO() {
        RouteDto routeDTO = new RouteDto();
        routeDTO.setId(100);
        routeDTO.setOriginId("TA1");
        routeDTO.setDestinationId("TA2");
        routeDTO.setIsActive(true);
        return routeDTO;
    }

    private Route makeRoute() {
        Route route = new Route();
        route.setId(100);

        Airport origin = new Airport("TA1", "Test City 1", true);
        Airport destination = new Airport("TA2", "Test City 2", true);

        route.setOriginAirport(origin);
        route.setDestinationAirport(destination);

        route.setIsActive(true);
        return route;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
