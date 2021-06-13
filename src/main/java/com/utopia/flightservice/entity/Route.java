package com.utopia.flightservice.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "route")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Route {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "origin_id", referencedColumnName = "iata_id")
    private Airport originAirport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "destination_id", referencedColumnName = "iata_id")
    private Airport destinationAirport;

    @Column(name = "is_active")
    private Boolean isActive;

}
