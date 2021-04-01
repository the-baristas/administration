package com.utopia.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.core.ApplicationContext;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.utopia.controller.AirportController;
import com.utopia.dao.AirportDao;
import com.utopia.entity.Airport;
import com.utopia.service.AirportService;

@RunWith(SpringRunner.class)
@WebMvcTest(AirportController.class)
@AutoConfigureMockMvc
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
		Airport airport1 = new Airport("TA3", "Test City 3", 1);
		Airport airport2 = new Airport("TA4", "Test City 4", 1);
		airports.add(airport1);
		airports.add(airport2);
		when(airportService.getAllAirports()).thenReturn(airports);
		
		// create list of airports, pass it to thenReturn to test that getting back list of airports
		
		mockMvc.perform(MockMvcRequestBuilders
					.get("/utopia_airlines/airport")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)));
		
	}

}
