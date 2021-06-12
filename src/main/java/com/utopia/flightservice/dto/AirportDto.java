package com.utopia.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {

    @NotBlank
    private String iataId;

    @NotBlank
    private String city;

    @Positive
    private Boolean isActive;


}
