package cz.railway_system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companies")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "child_tariff")
	private double childTariff;
	
	@Column(name = "student_tariff")
	private double studentTariff;
	
	@Column(name = "adult_tariff")
	private double adultTariff;
	
	@Column(name = "senior_tariff")
	private double seniorTariff;
	
	@Column(name = "class_charge")
	private double classCharge;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////
	
	public Company() {
		
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

	public double getChildTariff() {
		return childTariff;
	}

	public void setChildTariff(double childTariff) {
		this.childTariff = childTariff;
	}

	public double getStudentTariff() {
		return studentTariff;
	}

	public void setStudentTariff(double studentTariff) {
		this.studentTariff = studentTariff;
	}

	public double getAdultTariff() {
		return adultTariff;
	}

	public void setAdultTariff(double adultTariff) {
		this.adultTariff = adultTariff;
	}

	public double getSeniorTariff() {
		return seniorTariff;
	}

	public void setSeniorTariff(double seniorTariff) {
		this.seniorTariff = seniorTariff;
	}

	public double getClassCharge() {
		return classCharge;
	}

	public void setClassCharge(double classCharge) {
		this.classCharge = classCharge;
	}
	
}
