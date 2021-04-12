package com.utopia.flightservice.flight;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "flight")
public class Flight {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "airplane_id")
    private Integer airplaneId;

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
    private Integer isActive;

    // constructor

    public Flight() {}

    public Flight(Integer id,
                  Integer routeId,
                  Integer airplaneId,
                  LocalDateTime departureTime,
                  LocalDateTime arrivalTime,
                  Integer firstReserved,
                  Float firstPrice,
                  Integer businessReserved,
                  Float businessPrice,
                  Integer economyReserved,
                  Float economyPrice,
                  Integer isActive) {
        this.id = id;
        this.routeId = routeId;
        this.airplaneId = airplaneId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.firstReserved = firstReserved;
        this.firstPrice = firstPrice;
        this.businessReserved = businessReserved;
        this.businessPrice = businessPrice;
        this.economyReserved = economyReserved;
        this.economyPrice = economyPrice;
        this.isActive = isActive;
    }

    // getters/setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(Integer airplaneId) {
        this.airplaneId = airplaneId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getFirstReserved() {
        return firstReserved;
    }

    public void setFirstReserved(Integer firstReserved) {
        this.firstReserved = firstReserved;
    }

    public Float getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Float firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Integer getBusinessReserved() {
        return businessReserved;
    }

    public void setBusinessReserved(Integer businessReserved) {
        this.businessReserved = businessReserved;
    }

    public Float getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(Float businessPrice) {
        this.businessPrice = businessPrice;
    }

    public Integer getEconomyReserved() {
        return economyReserved;
    }

    public void setEconomyReserved(Integer economyReserved) {
        this.economyReserved = economyReserved;
    }

    public Float getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(Float economyPrice) {
        this.economyPrice = economyPrice;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
