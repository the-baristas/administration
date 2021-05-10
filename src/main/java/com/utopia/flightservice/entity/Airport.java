package com.utopia.flightservice.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "airport")
@Data @NoArgsConstructor @AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airport {
	
    @Id
    @Column(name = "iata_id")
	private String iataId;
    
    @Column(name = "city")
	private String city;
    
    @Column(name = "is_active")
	private Boolean isActive;

}
