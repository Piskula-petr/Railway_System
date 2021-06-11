package cz.railway_system.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.pojo.TrainMap;
import cz.railway_system.services.TrainMapService;

@WebMvcTest(StationsController.class)
public class TrainMapControllerTest {
	
	private MockData mockData;
	
	private List<TrainMap> trainsMap;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TrainMapService trainMapService;
	
	
	/**
	 * Inicializace parametrů
	 */
	@BeforeEach
	public void setUp() {
		
		mockData = new MockData();
		
		trainsMap = new ArrayList<>();
	}
	
	
	/**
	 * Test metody pro získání vlakové mapy
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(authorities = {"employee"})
	public void getTrainMap() throws Exception {
		
		TrainMap trainMap = new TrainMap();
		trainMap.setConnectionName(mockData.getConnectionName());
		trainMap.setStartRoute(mockData.getStartRoute());
		trainMap.setEndRoute(mockData.getEndRoute());
		trainsMap.add(trainMap);
		
		when(trainMapService.getCurrentRoutes()).thenReturn(trainsMap);
		
		// Vytvoření testovacího kontrolního bodu
		Checkpoint checkpoint = mockData.getCheckpoint();
		
		when(trainMapService.getChecpointPosition(anyInt(), anyInt(), anyInt())).thenReturn(checkpoint);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/map"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(trainsMap.size()))
			.andExpect(jsonPath("$.[0].connectionName.id").value(trainMap.getConnectionName().getId()))
			.andExpect(jsonPath("$.[0].connectionName.companyName.id").value(trainMap.getConnectionName().getCompanyName().getId()))
			.andExpect(jsonPath("$.[0].connectionName.companyName.name").value(trainMap.getConnectionName().getCompanyName().getName()))
			.andExpect(jsonPath("$.[0].connectionName.trainName.id").value(trainMap.getConnectionName().getTrainName().getId()))
			.andExpect(jsonPath("$.[0].connectionName.trainName.name").value(trainMap.getConnectionName().getTrainName().getName()))
			
			.andExpect(jsonPath("$.[0].startRoute.id").value(trainMap.getStartRoute().getId()))
			.andExpect(jsonPath("$.[0].startRoute.arrival").value(trainMap.getStartRoute().getArrival()))
			.andExpect(jsonPath("$.[0].startRoute.departure").exists())
			.andExpect(jsonPath("$.[0].startRoute.distance").value(trainMap.getStartRoute().getDistance()))
			.andExpect(jsonPath("$.[0].startRoute.stationOrder").value(trainMap.getStartRoute().getStationOrder()))
			.andExpect(jsonPath("$.[0].startRoute.stationName.id").value(trainMap.getStartRoute().getStationName().getId()))
			.andExpect(jsonPath("$.[0].startRoute.stationName.name").value(trainMap.getStartRoute().getStationName().getName()))
			
			.andExpect(jsonPath("$.[0].endRoute.id").value(trainMap.getEndRoute().getId()))
			.andExpect(jsonPath("$.[0].endRoute.arrival").exists())
			.andExpect(jsonPath("$.[0].endRoute.departure").value(trainMap.getEndRoute().getDeparture()))
			.andExpect(jsonPath("$.[0].endRoute.distance").value(trainMap.getEndRoute().getDistance()))
			.andExpect(jsonPath("$.[0].endRoute.stationOrder").value(trainMap.getEndRoute().getStationOrder()))
			.andExpect(jsonPath("$.[0].endRoute.stationName.id").value(trainMap.getEndRoute().getStationName().getId()))
			.andExpect(jsonPath("$.[0].endRoute.stationName.name").value(trainMap.getEndRoute().getStationName().getName()))
			
			.andExpect(jsonPath("$.[0].checkpoint.id").value(trainMap.getCheckpoint().getId()))
			.andExpect(jsonPath("$.[0].checkpoint.positionX").value(trainMap.getCheckpoint().getPositionX()))
			.andExpect(jsonPath("$.[0].checkpoint.positionY").value(trainMap.getCheckpoint().getPositionY()));
			
		verify(trainMapService, times(1)).getCurrentRoutes();
		verify(trainMapService, times(1)).getChecpointPosition(anyInt(), anyInt(), anyInt());
	}
	
}
