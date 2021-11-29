package com.utopia.flightservice.entity;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightQuery {
    @Positive
    @NonNull
    private Integer month;

    @Positive
    @NonNull
    private Integer date;

    @Positive
    @NonNull
    private Integer year;

    LocalDateTime departureDay;

    @NonNull
    private String filter;

    LocalDateTime lowerBound;

    LocalDateTime upperBound;

    public FlightQuery(Integer month, Integer date, Integer year, String filter) {
        this.month = month;
        this.date = date;
        this.year = year;
        this.filter = filter;
    }

}