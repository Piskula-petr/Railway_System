package cz.railway_system.services;

import java.time.LocalTime;
import java.util.List;

import cz.railway_system.pojo.RouteInfo;

public interface RoutesService {

	public List<Integer> getRouteIDs(int startStationID, int endStationID);
	
	public List<RouteInfo> getRouteInfo(List<Integer> routeIDs, 
			int startStationID, int endStationID, LocalTime departureTime);
	
}
