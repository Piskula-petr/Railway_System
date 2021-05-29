package cz.railway_system.services;

import java.util.List;

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.pojo.TrainMap;

public interface TrainMapService {

	
	/**
	 * Získání seznamu aktuálně probíhajících spojů
	 * 
	 * @return - vrací List probíhajících spojů
	 */
	public List<TrainMap> getCurrentRoutes();
	
	
	/**
	 * Získání kojntrolního bodu trasy
	 * 
	 * @param startStationID - ID startovního nádraží
	 * @param endStationID - ID cílového nádraží
	 * @param percentage - procentuální podíl trasy
	 * 
	 * @return - vrací kontrolní bod trasy
	 */
	public Checkpoint getChecpointPosition(int startStationID, int endStationID, int percentage);
	
}
