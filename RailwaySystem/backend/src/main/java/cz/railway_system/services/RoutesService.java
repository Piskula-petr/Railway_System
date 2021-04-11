package cz.railway_system.services;

import java.time.LocalTime;
import java.util.List;

import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;

public interface RoutesService {

	
	/**
	 * Získání seznamu id spojů
	 * 
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * 
	 * @return - vrací List id spojů
	 */
	public List<Integer> getConnectionIDs(int startStationID, int endStationID);
	
	
	/**
	 * Získání seznamu informací o trasách
	 * 
	 * @param connectionIDs - id spoje
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * @param departure - čas odjezdu
	 * 
	 * @return - vrací List informací o trasách
	 */
	public List<RouteInfo> getRoutesInfo(List<Integer> connectionIDs, 
			int startStationID, int endStationID, LocalTime departure);
	
	
	/**
	 * Získání detailu o trase
	 * 
	 * @param connectionID - id spoje
	 * 
	 * @return - vrací detail o trase
	 */
	public RouteDetail getRouteDetail(int connectionID);
	
}
