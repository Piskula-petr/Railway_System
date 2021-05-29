package cz.railway_system.controllers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.enums.LoadOption;
import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RouteService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class RoutesController {

	@Autowired
	private RouteService routeService;
	
	
	/**
	 * Získání spojů
	 * 
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param departure - čas odjezdu
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List spojů
	 */
	@GetMapping("/routesInfo/fromId={fromId}&toId={toId}&departure={departure}&loadOption={loadOption}")
	public List<RouteInfo> getMoreRoutesInfo(@PathVariable("fromId") int startStationID,
			 								 @PathVariable("toId") int endStationID,
			 								 @PathVariable("departure") LocalTime departure,
			 								 @PathVariable("loadOption") LoadOption loadOption) {
		
		List<RouteInfo> routesInfo = new ArrayList<>();
		
		if (loadOption == LoadOption.previous) {
			
			// Získání předešlých spojů
			routesInfo = getPreviousRoutesInfo(startStationID, endStationID, departure, loadOption);

		} else if (loadOption == LoadOption.later) {
			
			// Získání pozdějších spojů
			routesInfo = getLaterRoutesInfo(startStationID, endStationID, departure, loadOption);
		}
		
		return routesInfo;
	}
	
	
	/**
	 * Získání detailu spoje
	 * 
	 * @param connectionID - ID spoje
	 * 
	 * @return - vrací detail spoje
	 */
	@GetMapping("/routesInfo/detail/{connectionID}")
	public RouteDetail getRouteDetail(@PathVariable("connectionID") int connectionID) {
		
		RouteDetail routeDetail = routeService.getRouteDetail(connectionID);
		
		return routeDetail;
	}
	
	
	/**
	 * Získání předešlých spojů
	 * 
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param departure - čas odjezdu
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List předešlých spojů
	 */
	private List<RouteInfo> getPreviousRoutesInfo(int startStationID, int endStationID,
			LocalTime departure, LoadOption loadOption) {
		
		// List ID [00:00:00 -> departure - 1 min]
		List<Integer> connectionIDs = routeService.getConnectionIDs(startStationID, 
			endStationID, LocalTime.of(0, 0, 0), departure.minusMinutes(1), 3, loadOption);
	
		List<RouteInfo> routesInfo = routeService.getRoutesInfo(connectionIDs, 
			startStationID, endStationID, loadOption);
		
		// Načtení dalších spojů
		if (routesInfo.size() < 3) {
			
			// List ID [departure -> 23:59:59]
			connectionIDs = routeService.getConnectionIDs(startStationID, endStationID, 
				departure, LocalTime.of(23, 59, 59), 3 - routesInfo.size(), loadOption);
		
			routesInfo.addAll(0, routeService.getRoutesInfo(connectionIDs, 
				startStationID, endStationID, loadOption));
		}
		
		return routesInfo;
	}
	
	
	/**
	 * Získání pozdějších spojů
	 * 
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param departure - čas odjezdu
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List pozdějších spojů
	 */
	private List<RouteInfo> getLaterRoutesInfo(int startStationID, int endStationID,
			LocalTime departure, LoadOption loadOption) {
		
		// List ID [departure + 1 min -> 23:59:59]
		List<Integer> connectionIDs = routeService.getConnectionIDs(startStationID, 
			endStationID, departure.plusMinutes(1), LocalTime.of(23, 59, 59), 3, loadOption);
		
		List<RouteInfo> routesInfo = routeService.getRoutesInfo(connectionIDs, 
			startStationID, endStationID, loadOption);
		
		// Načtení dalších spojů
		if (routesInfo.size() < 3) {
			
			// List ID [00:00:00 -> departure]
			connectionIDs = routeService.getConnectionIDs(startStationID, endStationID, 
				LocalTime.of(0, 0, 0), departure, 3 - routesInfo.size(), loadOption);
		
			routesInfo.addAll(routeService.getRoutesInfo(connectionIDs, startStationID, 
				endStationID, loadOption));
		}
		
		return routesInfo;
	}
	
}
