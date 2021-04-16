package com.utopia.flightservice.route;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "route")
public class Route {

    @Id
    @Column(name = "id")
	private Integer id;
    
    @Column(name = "origin_id")
	private String originId;
    
    @Column(name = "destination_id")
	private String destinationId;
    
    @Column(name = "is_active")
    private Integer isActive;
    
    // constructor
    
    public Route() {}
    
	public Route(Integer id, String originId, String destinationId, Integer isActive) {
		super();
		this.id = id;
		this.originId = originId;
		this.destinationId = destinationId;
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", originId=" + originId + ", destinationId=" + destinationId + ", isActive="
				+ isActive + "]";
	}
	
}
