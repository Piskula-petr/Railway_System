package cz.railway_system.pojo;

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.entities.ConnectionName;
import cz.railway_system.entities.Route;

public class TrainMap {

	private ConnectionName connectionName;
	private Route startRoute;
	private Route endRoute;
	private Checkpoint checkpoint;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public TrainMap() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public ConnectionName getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(ConnectionName connectionName) {
		this.connectionName = connectionName;
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

	public Checkpoint getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(Checkpoint checkpoint) {
		this.checkpoint = checkpoint;
	}
	
}
