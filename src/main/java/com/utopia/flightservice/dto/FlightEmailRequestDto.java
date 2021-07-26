package com.utopia.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightEmailRequestDto {

    @Positive
    private Integer flightId;

    @NotBlank
    private String email;
}
