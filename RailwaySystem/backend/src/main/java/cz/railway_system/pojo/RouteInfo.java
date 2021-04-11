package cz.railway_system.pojo;

import cz.railway_system.entities.Connection;
import cz.railway_system.entities.Route;

public class RouteInfo {

	private Connection connection;
	private Route startRoute;
	private Route endRoute;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public RouteInfo() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

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
