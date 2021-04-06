package com.utopia.administration.route;

public class RouteNotSavedException extends Exception {
    public RouteNotSavedException(String errorMessage) {
        super(errorMessage);
    }
}
