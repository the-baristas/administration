package com.utopia.flightservice.entity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "flight")
@Data @NoArgsConstructor @AllArgsConstructor
public class Flight {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "airplane_id")
    private Integer airplaneId;

    @Column(name = "departure_time")
    private Timestamp departureTime;

    @Column(name = "arrival_time")
    private Timestamp arrivalTime;

    @Column(name = "first_reserved")
    private Integer firstReserved;

    @Column(name = "first_price")
    private Float firstPrice;

    @Column(name = "business_reserved")
    private Integer businessReserved;

    @Column(name = "business_price")
    private Float businessPrice;

    @Column(name = "economy_reserved")
    private Integer economyReserved;

    @Column(name = "economy_price")
    private Float economyPrice;

    @Column(name = "is_active")
    private Integer isActive;
}
