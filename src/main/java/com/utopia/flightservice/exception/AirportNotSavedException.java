package com.utopia.flightservice.exception;

public class AirportNotSavedException extends Exception {
    public AirportNotSavedException(String errorMessage) {
        super(errorMessage);
    }

}
