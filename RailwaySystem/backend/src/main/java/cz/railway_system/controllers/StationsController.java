package cz.railway_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.entities.Station;
import cz.railway_system.services.StationService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class StationsController {
	
	@Autowired
	private StationService stationService;
	
	
	/**
	 * Získání seznamu železničních stanic
	 * 
	 * @return - vrací List železničních stanic
	 */
	@GetMapping("/stations")
	public List<Station> getStations() {

		List<Station> stations = stationService.getStations();
		
		return stations;
	}
	
}
