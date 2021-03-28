package cz.railway_system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routes")
public class RouteOrder {

	@Id
	@Column(name = "station_order")
	private int stationOrder;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////
	
	public RouteOrder() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public int getStationOrder() {
		return stationOrder;
	}

	public void setStationOrder(int stationOrder) {
		this.stationOrder = stationOrder;
	}
	
}
