package com.utopia.flightservice.entity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.utopia.flightservice.service.RouteService;
import lombok.*;

import com.utopia.flightservice.repository.RouteDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "flight")
@JsonSerialize
@Data @NoArgsConstructor @AllArgsConstructor
public class Flight {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
    private Airplane airplane;

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
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

}
