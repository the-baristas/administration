package com.utopia.flightservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "airport")
@Data @NoArgsConstructor @AllArgsConstructor
public class Airport {
	
    @Id
    @Column(name = "iata_id")
	private String iataId;
    
    @Column(name = "city")
	private String city;
    
    @Column(name = "is_active")
	private Integer isActive;

}
