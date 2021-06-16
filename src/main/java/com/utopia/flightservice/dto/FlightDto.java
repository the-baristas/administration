package com.utopia.flightservice.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {

    @Positive
    private Integer id;

    @PositiveOrZero
    private Integer airplaneId;

    @PositiveOrZero
    private Integer routeId;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalTime;

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
    private Boolean isActive;

}
