package com.utopia.flightservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "airplane")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "max_first")
    private Long firstClassSeatsMax;

    @Column(name = "max_business")
    private Long businessClassSeatsMax;

    @Column(name = "max_economy")
    private Long economyClassSeatsMax;

    @Column(name = "model")
    private String model;
}
