package cz.railway_system.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RoutesService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class RoutesController {

	@Autowired
	private RoutesService routesService;
	
	
	@GetMapping("/routeInfo")
	public List<RouteInfo> getRouteInfo() {
		
		List<Integer> routeIDs = routesService.getRouteIDs(1, 2);
		
		LocalTime departureTime = LocalTime.of(10, 0);
		List<RouteInfo> routesInfo = routesService.getRouteInfo(routeIDs, 1, 2, departureTime);
		
		return routesInfo;
	}
	
}
