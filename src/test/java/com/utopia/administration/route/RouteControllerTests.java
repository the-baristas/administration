package com.utopia.administration.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.utopia.administration.airport.Airport;
import com.utopia.administration.airport.AirportController;
import com.utopia.administration.airport.AirportService;
import org.apache.catalina.core.ApplicationContext;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
public class RouteControllerTests {

    // import mock mvc
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RouteService routeService;

    // Route Controller Tests
    @Autowired
    private RouteController controller;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // Route Controller Is Not Null
    @Test
    public void controllerLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void test_getAllRoutes_statusOkAndListLength() throws Exception {
        List<Route> routes = new ArrayList<>();
        Route route1 = new Route(25, "SFO", "LAX", 1);
        Route route2 = new Route(26, "MSP", "JFK", 1);
        routes.add(route1);
        routes.add(route2);
        when(routeService.getAllRoutes()).thenReturn(routes);

        // create list of airports, pass it to thenReturn to test that getting back list of airports

        mockMvc.perform(get("/utopia_airlines/route")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void shouldCreateRoute() throws Exception {
        Route mockRoute = new Route(27, "SFO", "DEN", 1);
        when(routeService.saveRoute(mockRoute)).thenReturn(mockRoute.getId());

        mockMvc.perform(post("/utopia_airlines/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockRoute)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateRoute() throws Exception {
        Route route = new Route(28, "SFO", "JFK", 1);
        routeService.saveRoute(route);
        Route updatedRoute = new Route(28, "SFO", "JFK", 2);
        when(routeService.updateRoute(route.getId(), updatedRoute)).thenReturn(updatedRoute.getId());

        mockMvc.perform(put("/utopia_airlines/route/28")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRoute)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteRoute() throws Exception {
        Route mockRoute = new Route(29, "SFO", "LAX", 1);
        routeService.saveRoute(mockRoute);
        when(routeService.deleteRoute(mockRoute.getId())).thenReturn(mockRoute.getId().toString());

        mockMvc.perform(delete("/utopia_airlines/route/29")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockRoute)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
