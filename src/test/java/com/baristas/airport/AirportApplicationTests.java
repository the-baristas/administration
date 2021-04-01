package com.baristas.airport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.utopia.controller.AirportController;

@SpringBootTest
class AirportApplicationTests {

	@Autowired
	private AirportController controller;
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void airportCreated() throws Exception {
		
	}

}
