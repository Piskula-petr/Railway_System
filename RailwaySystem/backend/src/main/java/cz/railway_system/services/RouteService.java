package cz.railway_system.services;

import java.time.LocalTime;
import java.util.List;

import cz.railway_system.enums.LoadOption;
import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;

public interface RouteService {

	
	/**
	 * Získání seznamu ID spojů
	 * 
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param startTime - čas od
	 * @param endTime - čas do
	 * @param maxResult - počet výsledků
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List id spojů
	 */
	public List<Integer> getConnectionIDs(int startStationID, int endStationID, 
			LocalTime startTime, LocalTime endTime, int maxResult, LoadOption loadOption);
	
	
	/**
	 * Získání seznamu spojů
	 * 
	 * @param connectionIDs - ID spoje
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List spojů
	 */
	public List<RouteInfo> getRoutesInfo(List<Integer> connectionIDs, 
			int startStationID, int endStationID, LoadOption loadOption);
	
	
	/**
	 * Získání detailu spoje
	 * 
	 * @param connectionID - ID spoje
	 * 
	 * @return - vrací detail spoje
	 */
	public RouteDetail getRouteDetail(int connectionID);
	
}
