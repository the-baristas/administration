package com.utopia.flightservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "route")
@Data @NoArgsConstructor @AllArgsConstructor
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

	
}
