package com.utopia.administration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "An airplane with the given ID does not exist")
public class AirplaneNotFoundException extends RuntimeException {
    public AirplaneNotFoundException(Long id) {
        super("Could not find airplane " + id);
    }
}
