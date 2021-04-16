package com.utopia.flightservice;

public class AirplaneNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AirplaneNotFoundException(Long id) {
        super("Could not find airplane " + id);
    }
}
