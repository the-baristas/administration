package com.utopia.flightservice.dto;

import com.utopia.flightservice.entity.Route;
import com.utopia.flightservice.service.RouteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {

    @Positive
    private Integer id;

    @PositiveOrZero
    private Integer airplaneId;

    @NotBlank
    private Timestamp departureTime;

    @NotBlank
    private Timestamp arrivalTime;

    @PositiveOrZero
    private Integer firstReserved;

    @Positive
    private Float firstPrice;

    @PositiveOrZero
    private Integer businessReserved;

    @Positive
    private Float businessPrice;

    @PositiveOrZero
    private Integer economyReserved;

    @Positive
    private Float economyPrice;

    @Positive
    private Integer isActive;

}
