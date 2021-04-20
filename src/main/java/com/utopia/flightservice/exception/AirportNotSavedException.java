package com.utopia.flightservice.exception;

public class AirportNotSavedException extends Exception {
    private static final long serialVersionUID = 1L;

    public AirportNotSavedException(String errorMessage) {
        super(errorMessage);
    }

}
