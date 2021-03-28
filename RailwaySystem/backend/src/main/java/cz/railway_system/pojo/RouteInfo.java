package cz.railway_system.pojo;

import cz.railway_system.entities.Route;

public class RouteInfo {

	private Route startRoute;
	private Route endRoute;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public RouteInfo() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public Route getStartRoute() {
		return startRoute;
	}

	public void setStartRoute(Route startRoute) {
		this.startRoute = startRoute;
	}

	public Route getEndRoute() {
		return endRoute;
	}

	public void setEndRoute(Route endRoute) {
		this.endRoute = endRoute;
	}
	
}
