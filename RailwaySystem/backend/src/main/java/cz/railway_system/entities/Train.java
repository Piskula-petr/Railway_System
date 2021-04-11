package cz.railway_system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trains")
public class Train {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "wifi")
	private boolean wifi;
	
	@Column(name = "restaurant")
	private boolean restaurant;
	
	@Column(name = "handicap")
	private boolean handicap;
	
	@Column(name = "bike")
	private boolean bike;
	
	@Column(name = "wagons_count")
	private int wagonsCount;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////	
	
	public Train() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public boolean isRestaurant() {
		return restaurant;
	}

	public void setRestaurant(boolean restaurant) {
		this.restaurant = restaurant;
	}

	public boolean isHandicap() {
		return handicap;
	}

	public void setHandicap(boolean handicap) {
		this.handicap = handicap;
	}

	public boolean isBike() {
		return bike;
	}

	public void setBike(boolean bike) {
		this.bike = bike;
	}

	public int getWagonsCount() {
		return wagonsCount;
	}

	public void setWagonsCount(int wagonsCount) {
		this.wagonsCount = wagonsCount;
	}
	
}
