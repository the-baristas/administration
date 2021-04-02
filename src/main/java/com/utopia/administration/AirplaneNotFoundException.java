package com.utopia.administration;

public class AirplaneNotFoundException extends RuntimeException {
    public AirplaneNotFoundException(Long id) {
        super("Could not find airplane " + id);
    }
}
