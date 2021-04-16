package com.utopia.flightservice.exception;

public class RouteNotSavedException extends Exception {
    public RouteNotSavedException(String errorMessage) {
        super(errorMessage);
    }
}
