package com.utopia.flightservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flightservice.dto.AirplaneDto;
import com.utopia.flightservice.dto.AirportDto;
import com.utopia.flightservice.entity.Airport;
import com.utopia.flightservice.service.AirportService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AirportController.class)
public class AirportControllerTests {
	
	// import mock mvc
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AirportService airportService;
    
	// Airport Controller Tests
	@Autowired
	private AirportController controller;
	
	// Airport Controller Is Not Null
	@Test
	public void controllerLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void test_getAllAirports_statusOkAndListLength() throws Exception {
		List<Airport> airports = new ArrayList<>();
		Airport airport1 = new Airport("TA3", "Test City 3", true);
		Airport airport2 = new Airport("TA4", "Test City 4", true);
		airports.add(airport1);
		airports.add(airport2);
		when(airportService.getAllAirports()).thenReturn(airports);

		// create list of airports, pass it to thenReturn to test that getting back list of airports
		
		mockMvc.perform(get("/airports")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)));
		}
	
	@Test
	public void shouldCreateAirport() throws Exception {
		Airport mockAirport = new Airport("TA4", "Test City 4", true);
		when(airportService.saveAirport(mockAirport)).thenReturn(mockAirport.getIataId());
	
		mockMvc.perform(post("/airports/{id}", mockAirport.getIataId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(mockAirport)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void shouldUpdateAirport() throws Exception {
		Airport mockAirport = new Airport("TA5", "Test City 5", true);
		airportService.saveAirport(mockAirport);
		Airport updatedAirport = new Airport("TA5", "Updated Test City", false);
		when(airportService.updateAirport(mockAirport.getIataId(), updatedAirport)).thenReturn(updatedAirport.getIataId());
		
		mockMvc.perform(put("/airports/TA5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(updatedAirport)))
				.andExpect(status().isOk());	
	}
	
	@Test
	public void shouldDeleteAirport() throws Exception {
		Airport mockAirport = new Airport("TA6", "Test City 6", true);
		airportService.saveAirport(mockAirport);
		when(airportService.deleteAirport(mockAirport.getIataId())).thenReturn(mockAirport.getIataId());
		
		mockMvc.perform(delete("/airports/TA5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(mockAirport)))
				.andExpect(status().isNoContent());
	}

	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
