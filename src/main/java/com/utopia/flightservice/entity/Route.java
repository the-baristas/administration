package com.utopia.flightservice.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "route")
@Data @NoArgsConstructor @AllArgsConstructor
@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

//    @OneToMany @JsonBackReference
//    @JoinColumn(name = "flight_id")
//    private Integer flightId;
	
}
