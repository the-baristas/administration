package com.utopia.flightservice.exception;

public class RouteNotSavedException extends Exception {
    private static final long serialVersionUID = 1L;

    public RouteNotSavedException(String errorMessage) {
        super(errorMessage);
    }
}
