package com.utopia.flightservice.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flight")
@JsonSerialize
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("bookedUsers")
public class Flight {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
    private Airplane airplane;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToMany
    @JoinTable(name = "passenger_booking",
            joinColumns = @JoinColumn(name = "flight_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    private Set<User> bookedUsers;

}
