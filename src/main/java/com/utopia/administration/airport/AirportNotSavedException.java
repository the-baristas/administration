package com.utopia.administration.airport;

public class AirportNotSavedException extends Exception {
    public AirportNotSavedException(String errorMessage) {
        super(errorMessage);
    }

}
