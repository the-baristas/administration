package com.utopia.service;

public class AirportNotSavedException extends Exception {
    public AirportNotSavedException(String errorMessage) {
        super(errorMessage);
    }

}
