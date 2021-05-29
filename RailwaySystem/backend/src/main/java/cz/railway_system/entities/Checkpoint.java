package cz.railway_system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "checkpoints")
public class Checkpoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "position_x")
	private int positionX;
	
	@Column(name = "position_y")
	private int positionY;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public Checkpoint() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	
}
