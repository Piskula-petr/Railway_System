package cz.railway_system.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RoutesService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class RoutesController {

	@Autowired
	private RoutesService routesService;
	
	
	/**
	 * Získání informací o trasách
	 * 
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * @param departure - čas odjezdu
	 * 
	 * @return - vrací List informací o spojích
	 */
	@GetMapping("/routesInfo/fromId={fromId}&toId={toId}&departure={departure}")
	public List<RouteInfo> getRoutesInfo(@PathVariable("fromId") int startStationID,
										 @PathVariable("toId") int endStationID,
										 @PathVariable("departure") LocalTime departure) {
		
		List<Integer> connectionIDs = routesService.getConnectionIDs(
				startStationID, endStationID);

		List<RouteInfo> routesInfo = routesService.getRoutesInfo(
			connectionIDs, startStationID, endStationID, departure);
		
		return routesInfo;
	}

	
	/**
	 * Získání detailu o trase
	 * 
	 * @param connectionID - id spoje
	 * 
	 * @return - vrací detail o trase
	 */
	@GetMapping("/routesInfo/detail/{connectionID}")
	public RouteDetail getRouteDetail(@PathVariable("connectionID") int connectionID) {
		
		RouteDetail routeDetail = routesService.getRouteDetail(connectionID);
		
		return routeDetail;
	}
	
}
