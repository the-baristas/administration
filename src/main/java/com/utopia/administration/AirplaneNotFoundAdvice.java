package com.utopia.administration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AirplaneNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(AirplaneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String airplaneNotFoundHandler(AirplaneNotFoundException ex) {
        return ex.getMessage();
    }
}
