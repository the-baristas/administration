package com.utopia.flightservice.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "route")
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
