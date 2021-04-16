package com.utopia.flightservice.airport;

public class AirportNotSavedException extends Exception {
    public AirportNotSavedException(String errorMessage) {
        super(errorMessage);
    }

}
