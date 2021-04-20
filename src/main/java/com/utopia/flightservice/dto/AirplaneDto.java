package com.utopia.flightservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneDto {

    @Positive
    private Long id;

    @PositiveOrZero
    private Long firstClassSeatsMax;

    @PositiveOrZero
    private Long businessClassSeatsMax;

    @PositiveOrZero
    private Long economyClassSeatsMax;

    @NotBlank
    private String model;
}
