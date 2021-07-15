package com.utopia.flightservice.entity;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightQuery {
    @Positive
    private Integer month;

    @Positive
    private Integer date;

    @Positive
    private Integer year;
}
