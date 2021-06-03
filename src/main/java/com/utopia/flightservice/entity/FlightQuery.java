package com.utopia.flightservice.entity;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class FlightQuery {
    private String month;
    private String date;
    private String year;
}