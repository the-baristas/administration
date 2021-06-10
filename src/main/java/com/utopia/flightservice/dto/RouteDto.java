package com.utopia.flightservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {

    @Positive
    private Integer id;

    @NotBlank
    private String originId;

    @NotBlank
    private String destinationId;

    @PositiveOrZero
    private Boolean isActive;
}
