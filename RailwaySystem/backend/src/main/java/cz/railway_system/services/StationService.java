package cz.railway_system.services;

import java.util.List;

import cz.railway_system.entities.Station;

public interface StationService {

	
	/**
	 * Získání seznamu železničních stanic
	 * 
	 * @return - vrací List železničních stanic
	 */
	public List<Station> getStations();
	
}
