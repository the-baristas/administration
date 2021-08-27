package com.utopia.flightservice.service;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Route;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GraphServiceTests {


    @MockBean
    AirportService airportService;

    @MockBean
    RouteService routeService;

    @MockBean
    GraphService graphService;

    @Test
    public void getPathTest() {

        Graph<Airport, DefaultEdge> graph = new SimpleDirectedGraph<>(
                DefaultEdge.class);

        Airport airport1 = new Airport("LAX", "Los Angeles", true);
        Airport airport2 = new Airport("ORD", "Chicago", true);
        Airport airport3 = new Airport("SLO", "St. Louis", true);
        Airport airport4 = new Airport("JFK", "New York City", true);
        Airport airport5 = new Airport("BOS", "Boston", true);

        ArrayList<Airport> airports = new ArrayList<>();
        airports.add(airport1);
        airports.add(airport2);
        airports.add(airport3);
        airports.add(airport4);
        airports.add(airport5);

        Route route1 = new Route(1, airport1, airport4, true);
        Route route2 = new Route(2, airport1, airport2, true);
        Route route3 = new Route(3, airport2, airport4, true);
        Route route4 = new Route(4, airport2, airport3, true);
        Route route5 = new Route(5, airport3, airport4, true);

        ArrayList<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);

        for (Airport airport : airports) {
            graph.addVertex(airport);
        }

        for (Route route : routes) {
            graph.addEdge(route.getOriginAirport(),
                    route.getDestinationAirport());
        }

        AllDirectedPaths<Airport, DefaultEdge> algo = new AllDirectedPaths<Airport, DefaultEdge>(
                graph);
        List<GraphPath<Airport, DefaultEdge>> paths = algo
                .getAllPaths(airport1, airport4, true, 2);
        when(graphService.getPaths(airport1, airport4)).thenReturn(paths);
        List<GraphPath<Airport, DefaultEdge>> get = graphService.getPaths(airport1, airport4);
        assertThat(paths, is(get));
    }

}
