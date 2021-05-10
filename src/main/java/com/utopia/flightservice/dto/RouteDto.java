package com.utopia.flightservice.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class RouteDto {

    @Positive
    private Integer id;

    @NotBlank
    private String originAirport;

    @NotBlank
    private String destinationAirport;

    @Positive
    private Boolean isActive;
}
