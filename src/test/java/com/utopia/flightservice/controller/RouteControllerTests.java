package com.utopia.flightservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import com.utopia.flightservice.controller.RouteController;
import com.utopia.flightservice.entity.Airport;
=======
import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> 556ac07824d9ce7db5f9b680d49fbc57742bcf5d
import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.AirportService;
import com.utopia.flightservice.service.RouteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
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
        when(routeService.getAllRoutes()).thenReturn(routes);

        // create list of airports, pass it to thenReturn to test that getting back list of airports

        mockMvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void shouldCreateRoute() throws Exception {
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport destinationAirport1 = airportService.getAirportById("DEN");

        Route mockRoute = new Route(27, originAirport1, destinationAirport1, true);
        when(routeService.saveRoute(mockRoute)).thenReturn(mockRoute.getId());

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockRoute)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateRoute() throws Exception {
        Airport originAirport1 = airportService.getAirportById("SFO");
        Airport destinationAirport2 = airportService.getAirportById("JFK");

        Route route = new Route(28, originAirport1, destinationAirport2, true);
        routeService.saveRoute(route);
        Route updatedRoute = new Route(28, originAirport1, destinationAirport2, false);
        when(routeService.updateRoute(route.getId(), updatedRoute)).thenReturn(updatedRoute.getId());

        mockMvc.perform(put("/routes/28")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRoute)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteRoute() throws Exception {
        Airport originAirport1 = airportService.getAirportById("SFO");

        Airport destinationAirport1 = airportService.getAirportById("LAX");

        Route mockRoute = new Route(29, originAirport1, destinationAirport1, true);
        routeService.saveRoute(mockRoute);
        when(routeService.deleteRoute(mockRoute.getId())).thenReturn(mockRoute.getId().toString());

        mockMvc.perform(delete("/routes/29")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockRoute)))
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
