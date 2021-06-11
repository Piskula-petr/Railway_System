package cz.railway_system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "connections")
public class ConnectionName {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private CompanyName companyName;
	
	@ManyToOne
	@JoinColumn(name = "train_id")
	private TrainName trainName;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public ConnectionName() {
	
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public CompanyName getCompanyName() {
		return companyName;
	}

	public void setCompanyNames(CompanyName companyName) {
		this.companyName = companyName;
	}

	public TrainName getTrainName() {
		return trainName;
	}

	public void setTrainName(TrainName trainName) {
		this.trainName = trainName;
	}
	
}
