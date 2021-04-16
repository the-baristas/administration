package com.utopia.flightservice.route;

public class RouteNotSavedException extends Exception {
    public RouteNotSavedException(String errorMessage) {
        super(errorMessage);
    }
}
