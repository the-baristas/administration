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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flight {
    @Id
    @Column(name = "id")
    private Integer id;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "route_id", referencedColumnName = "ID", insertable = false, updatable = false)
    private Route route;

}
