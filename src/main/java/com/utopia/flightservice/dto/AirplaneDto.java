package com.utopia.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
