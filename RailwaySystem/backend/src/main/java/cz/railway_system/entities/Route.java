package cz.railway_system.entities;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "routes")
public class Route {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "station_id")
	private StationName stationName;
	
	@Column(name = "arrival")
	private LocalTime arrival;
	
	@Column(name = "departure")
	private LocalTime departure;
	
	@Column(name = "station_order")
	private int stationOrder;
	
	@Column(name = "distance")
	private int distance;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////
	
	public Route() {
		
	}

// Gettery + Settery /////////////////////////////////////////////////////////////////////	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public StationName getStationName() {
		return stationName;
	}

	public void setStationName(StationName stationName) {
		this.stationName = stationName;
	}

	public LocalTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalTime arrival) {
		this.arrival = arrival;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public int getStationOrder() {
		return stationOrder;
	}

	public void setStationOrder(int stationOrder) {
		this.stationOrder = stationOrder;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
}
