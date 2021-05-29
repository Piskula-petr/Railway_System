package cz.railway_system.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.entities.Route;
import cz.railway_system.pojo.TrainMap;
import cz.railway_system.services.TrainMapService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class TrainMapController {

	@Autowired
	private TrainMapService trainMapService;
	
	
	/**
	 * Získání aktuálních spojů
	 * 
	 * @return - vrací List aktuálních spojů
	 */
	@GetMapping("/map")
	public List<TrainMap> getRouteIDs() {
		
		List<TrainMap> trainsMap = trainMapService.getCurrentRoutes();
		
		for (TrainMap trainMap : trainsMap) {
			
			Route routeStart = trainMap.getStartRoute();
			Route routeEnd = trainMap.getEndRoute();
			
			// Počet sekund počáteční trasy
			int startSeconds = (routeStart.getArrival() != null 
				? routeStart.getArrival().toSecondOfDay() 
					: routeStart.getDeparture().toSecondOfDay());

			// Počet sekund aktuálního času
			int currentSeconds = LocalTime.now().toSecondOfDay();
			
			// Počet sekund cílové trasy
			int endSeconds = (routeEnd.getArrival() != null
				? routeEnd.getArrival().toSecondOfDay() 
					: routeEnd.getDeparture().toSecondOfDay());
			
			// Odečtení sekund počáteční trasy (vyrovnání obou hodnot)
			currentSeconds = currentSeconds - startSeconds;
			endSeconds = endSeconds - startSeconds;
			
			// Procentuální podíl trasy (trojčlenka)
			double routePercentege = (currentSeconds * 100) / endSeconds; 

			// Zaokrouhlení na desítky
			routePercentege = Math.round(routePercentege / 10) * 10;
			
			// Získání souřadnic kontrolního bodu trasy
			Checkpoint checkpoint = trainMapService.getChecpointPosition(
					routeStart.getStationName().getId(), routeEnd.getStationName().getId(), (int) routePercentege);

			trainMap.setCheckpoint(checkpoint);
		}
		
		return trainsMap;
	}
	
}
