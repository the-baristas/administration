package com.utopia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "airport")
public class Airport {
	
    @Id
    @Column(name = "iata_id")
	private String iataId;
    
    @Column(name = "city")
	private String city;
    
    @Column(name = "is_active")
	private Integer isActive;
    
    // constructor
    
    public Airport() {}
    
	public Airport(String iataId, String city, Integer isActive) {
		super();
		this.iataId = iataId;
		this.city = city;
		this.isActive = isActive;
	}
    
 
    // getters and setters
	public String getIataId() {
		return iataId;
	}

	public void setIataId(String iataId) {
		this.iataId = iataId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	// to string
	@Override
	public String toString() {
		return "Airport [iataId=" + iataId + ", city=" + city + ", isActive=" + isActive + "]";
	}
	

}
