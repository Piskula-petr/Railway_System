package cz.railway_system.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ControllersTest {

	@Autowired
	private LoginController loginController;
	
	@Autowired
	private RoutesController routesController;
	
	@Autowired
	private StationsController stationsController;
	
	@Autowired
	private TrainMapController trainMapController;
	
	
	@Test
	public void contextLoads() throws Exception {
		
		assertThat(loginController).isNotNull();
		assertThat(routesController).isNotNull();
		assertThat(stationsController).isNotNull();
		assertThat(trainMapController).isNotNull();
	}
	
}
