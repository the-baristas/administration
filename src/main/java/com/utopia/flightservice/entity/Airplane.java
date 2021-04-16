package com.utopia.flightservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airplane")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFirstClassSeatsMax() {
        return firstClassSeatsMax;
    }

    public void setFirstClassSeatsMax(Long firstClassSeatsMax) {
        this.firstClassSeatsMax = firstClassSeatsMax;
    }

    public Long getBusinessClassSeatsMax() {
        return businessClassSeatsMax;
    }

    public void setBusinessClassSeatsMax(Long businessClassSeatsMax) {
        this.businessClassSeatsMax = businessClassSeatsMax;
    }

    public Long getEconomyClassSeatsMax() {
        return economyClassSeatsMax;
    }

    public void setEconomyClassSeatsMax(Long economyClassSeatsMax) {
        this.economyClassSeatsMax = economyClassSeatsMax;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Airplane other = (Airplane) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Airplane [id=" + id + "]";
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
