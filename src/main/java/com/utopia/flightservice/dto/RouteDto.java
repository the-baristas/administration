package com.utopia.flightservice.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class RouteDto {

    @Positive
    private Integer id;

    @NotBlank
    private String originId;

    @NotBlank
    private String destinationId;

    @Positive
    private Integer isActive;
}
