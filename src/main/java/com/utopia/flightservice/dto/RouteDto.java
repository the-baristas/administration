package com.utopia.flightservice.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
=======
>>>>>>> 556ac07824d9ce7db5f9b680d49fbc57742bcf5d
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
