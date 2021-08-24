package com.utopia.flightservice.service;

import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.entity.Flight;
import com.utopia.flightservice.entity.Route;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphService {


    AirportService airportService;
    RouteService routeService;
    Graph<Airport, DefaultEdge> graph;

    public GraphService(AirportService airportService, RouteService routeService) {
        this.airportService = airportService;
        this.routeService = routeService;
        createGraph();
    }

    private void createGraph() {
        Graph<Airport, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        List<Airport> airports = airportService.getAllAirports();
        List<Route> routes = routeService.getAllRoutes();

        // adds all airports as vertices
        for (Airport airport : airports) {
            graph.addVertex(airport);
        }

        // adds all routes as edges
        for (Route route : routes) {
            graph.addEdge(route.getOriginAirport(), route.getDestinationAirport());
        }

        this.graph = graph;
    }

    public List<GraphPath<Airport, DefaultEdge>> getPaths(Airport originAirport, Airport destinationAirport) {
        AllDirectedPaths<Airport, DefaultEdge> algo = new AllDirectedPaths<Airport, DefaultEdge>(graph);
        List<GraphPath<Airport, DefaultEdge>> paths = algo.getAllPaths(originAirport, destinationAirport, true, 2);
        return paths;
    }




}
