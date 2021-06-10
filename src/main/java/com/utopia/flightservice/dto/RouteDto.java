package com.utopia.flightservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class RouteDto {

    @Positive
    private Integer id;

    @NotBlank
    private String originId;

    @NotBlank
    private String destinationId;

    @PositiveOrZero
    private Integer isActive;
}
