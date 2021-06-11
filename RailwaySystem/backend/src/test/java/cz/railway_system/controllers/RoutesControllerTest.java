package cz.railway_system.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.railway_system.entities.Route;
import cz.railway_system.enums.LoadOption;
import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RouteService;

@WebMvcTest(StationsController.class)
public class RoutesControllerTest {

	private MockData mockData;
	
	private Random random;
	private LocalTime departureTime;
	private LoadOption loadOption;
	
	private List<Integer> connectionIDs;
	private List<RouteInfo> routesInfo;
	private List<Route> routes;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private RouteService routeService;
	
	
	/**
	 * Inicializace parametrů
	 */
	@BeforeEach
	public void setUp() {
		
		mockData = new MockData();
		
		random = new Random();
		departureTime = LocalTime.now().minusHours(1);
		
		connectionIDs = new ArrayList<>();
		routesInfo = new ArrayList<>();
		routes = new ArrayList<>();
	}
	
	
	/**
	 * Test metody pro získání spojů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoreRoutesInfo() throws Exception {
		
		connectionIDs.add(random.nextInt());
		
		RouteInfo routeInfo = new RouteInfo();
		routeInfo.setConnection(mockData.getConnection());
		routeInfo.setStartRoute(mockData.getStartRoute());
		routeInfo.setEndRoute(mockData.getEndRoute());
		
		routesInfo.add(routeInfo);
		
		when(routeService.getConnectionIDs(anyInt(), anyInt(), any(LocalTime.class), 
				any(LocalTime.class), anyInt(), any(LoadOption.class))).thenReturn(connectionIDs);
		
		when(routeService.getRoutesInfo(anyList(), anyInt(), anyInt(), any(LoadOption.class))).thenReturn(routesInfo);
		
		int fromID = random.nextInt();
		int toID = random.nextInt();
		loadOption = (random.nextInt(2) == 0 ? LoadOption.previous : LoadOption.later);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/routesInfo/fromId=" + fromID + "&toId=" + toID + "&departure=" 
				+ departureTime.format(DateTimeFormatter.ofPattern("HH:mm")) + "&loadOption=" + loadOption))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].connection.id").value(routeInfo.getConnection().getId()))
			.andExpect(jsonPath("$.[0].connection.company.id").value(routeInfo.getConnection().getCompany().getId()))
			.andExpect(jsonPath("$.[0].connection.company.name").value(routeInfo.getConnection().getCompany().getName()))
			.andExpect(jsonPath("$.[0].connection.company.childTariff").value(routeInfo.getConnection().getCompany().getChildTariff()))
			.andExpect(jsonPath("$.[0].connection.company.studentTariff").value(routeInfo.getConnection().getCompany().getStudentTariff()))
			.andExpect(jsonPath("$.[0].connection.company.adultTariff").value(routeInfo.getConnection().getCompany().getAdultTariff()))
			.andExpect(jsonPath("$.[0].connection.company.seniorTariff").value(routeInfo.getConnection().getCompany().getSeniorTariff()))
			.andExpect(jsonPath("$.[0].connection.company.classCharge").value(routeInfo.getConnection().getCompany().getClassCharge()))
			.andExpect(jsonPath("$.[0].connection.trainName.id").value(routeInfo.getConnection().getTrainName().getId()))
			.andExpect(jsonPath("$.[0].connection.trainName.name").value(routeInfo.getConnection().getTrainName().getName()))
			
			.andExpect(jsonPath("$.[0].startRoute.id").value(routeInfo.getStartRoute().getId()))
			.andExpect(jsonPath("$.[0].startRoute.stationName.id").value(routeInfo.getStartRoute().getStationName().getId()))
			.andExpect(jsonPath("$.[0].startRoute.stationName.name").value(routeInfo.getStartRoute().getStationName().getName()))
			.andExpect(jsonPath("$.[0].startRoute.arrival").value(routeInfo.getStartRoute().getArrival()))
			.andExpect(jsonPath("$.[0].startRoute.departure").exists())
			.andExpect(jsonPath("$.[0].startRoute.stationOrder").value(routeInfo.getStartRoute().getStationOrder()))
			.andExpect(jsonPath("$.[0].startRoute.distance").value(routeInfo.getStartRoute().getDistance()))
		
			.andExpect(jsonPath("$.[0].endRoute.id").value(routeInfo.getEndRoute().getId()))
			.andExpect(jsonPath("$.[0].endRoute.stationName.id").value(routeInfo.getEndRoute().getStationName().getId()))
			.andExpect(jsonPath("$.[0].endRoute.stationName.name").value(routeInfo.getEndRoute().getStationName().getName()))
			.andExpect(jsonPath("$.[0].endRoute.arrival").exists())
			.andExpect(jsonPath("$.[0].endRoute.departure").value(routeInfo.getEndRoute().getDeparture()))
			.andExpect(jsonPath("$.[0].endRoute.stationOrder").value(routeInfo.getEndRoute().getStationOrder()))
			.andExpect(jsonPath("$.[0].endRoute.distance").value(routeInfo.getEndRoute().getDistance()));
		
		verify(routeService, times(2)).getConnectionIDs(anyInt(), anyInt(), any(LocalTime.class), 
				any(LocalTime.class), anyInt(), any(LoadOption.class));
		
		verify(routeService, times(2)).getRoutesInfo(anyList(), anyInt(), anyInt(), any(LoadOption.class));
	}
	
	
	/**
	 * Test metody pro získání detailu spoje
	 * 
	 * @throws Exception
	 */
	@Test
	public void getRouteDetail() throws Exception {
		
		// Vytvoření testovacího detailu trasy
		RouteDetail routeDetail = new RouteDetail();
		routeDetail.setTrain(mockData.getTrain());
		
		routes.add(mockData.getStartRoute());
		routeDetail.setRoutes(routes);
		
		when(routeService.getRouteDetail(anyInt())).thenReturn(routeDetail);
		
		int connectionID = random.nextInt();
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/routesInfo/detail/" + connectionID))
			.andExpect(status().isOk())
			
			.andExpect(jsonPath("$.train.id").value(routeDetail.getTrain().getId()))
			.andExpect(jsonPath("$.train.name").value(routeDetail.getTrain().getName()))
			.andExpect(jsonPath("$.train.wifi").value(routeDetail.getTrain().isWifi()))
			.andExpect(jsonPath("$.train.restaurant").value(routeDetail.getTrain().isRestaurant()))
			.andExpect(jsonPath("$.train.handicap").value(routeDetail.getTrain().isHandicap()))
			.andExpect(jsonPath("$.train.bike").value(routeDetail.getTrain().isBike()))
			.andExpect(jsonPath("$.train.wagonsCount").value(routeDetail.getTrain().getWagonsCount()))
			
			.andExpect(jsonPath("$.routes.length()").value(routes.size()))
			.andExpect(jsonPath("$.routes.[0].id").value(routeDetail.getRoutes().get(0).getId()))
			.andExpect(jsonPath("$.routes.[0].arrival").value(routeDetail.getRoutes().get(0).getArrival()))
			.andExpect(jsonPath("$.routes.[0].departure").exists())
			.andExpect(jsonPath("$.routes.[0].distance").value(routeDetail.getRoutes().get(0).getDistance()))
			.andExpect(jsonPath("$.routes.[0].stationOrder").value(routeDetail.getRoutes().get(0).getStationOrder()))
			.andExpect(jsonPath("$.routes.[0].stationName.id").value(routeDetail.getRoutes().get(0).getStationName().getId()))
			.andExpect(jsonPath("$.routes.[0].stationName.name").value(routeDetail.getRoutes().get(0).getStationName().getName()));
		
		verify(routeService, times(1)).getRouteDetail(anyInt());
	}
	
}
