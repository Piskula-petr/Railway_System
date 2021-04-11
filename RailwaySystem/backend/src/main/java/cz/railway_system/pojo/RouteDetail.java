package cz.railway_system.pojo;

import java.util.List;

import cz.railway_system.entities.Route;
import cz.railway_system.entities.Train;

public class RouteDetail {

	private Train train;
	private List<Route> routes;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////	
	
	public RouteDetail() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
}
