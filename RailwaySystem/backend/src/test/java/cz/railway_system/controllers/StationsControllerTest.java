package cz.railway_system.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.railway_system.entities.Station;
import cz.railway_system.services.StationService;

@WebMvcTest(StationsController.class)
public class StationsControllerTest {

	private List<Station> stations;
	
	private Random random;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StationService stationService;
	
	
	/**
	 * Inicializace parametrů
	 */
	@BeforeEach
	public void setUp() {
		
		random = new Random();
		stations = new ArrayList<>();
	}
	
	
	/**
	 * Test metody pro získání seznamu železničních stanic
	 * 
	 * @throws Exception
	 */
	@Test
	public void getStations() throws Exception {
		
		// Vytvoření testovacího nádraží
		Station station = new Station();
		station.setId(random.nextInt());
		station.setName("Praha hlavní nádraží");
		station.setPositionX(random.nextInt());
		station.setPositionY(random.nextInt());
		stations.add(station);
		
		when(stationService.getStations()).thenReturn(stations);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/stations"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(stations.size()))
			.andExpect(jsonPath("$[0].id").value(station.getId()))
			.andExpect(jsonPath("$[0].name").value(station.getName()))
			.andExpect(jsonPath("$[0].positionX").value(station.getPositionX()))
			.andExpect(jsonPath("$[0].positionY").value(station.getPositionY()));
		
		verify(stationService, times(1)).getStations();
	}
	
}
