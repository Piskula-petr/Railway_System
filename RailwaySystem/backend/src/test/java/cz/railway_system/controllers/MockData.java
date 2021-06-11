package cz.railway_system.controllers;

import java.time.LocalTime;
import java.util.Random;

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.entities.Company;
import cz.railway_system.entities.CompanyName;
import cz.railway_system.entities.Connection;
import cz.railway_system.entities.ConnectionName;
import cz.railway_system.entities.Route;
import cz.railway_system.entities.Station;
import cz.railway_system.entities.StationName;
import cz.railway_system.entities.Train;
import cz.railway_system.entities.TrainName;

public class MockData {

	private Random random;
	private LocalTime startRouteTime;
	private LocalTime endRouteTime;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public MockData() {
		
		random = new Random();
		
		startRouteTime = LocalTime.now().minusHours(1);
		endRouteTime = LocalTime.now().plusHours(1);
	}
	

	/**
	 * Mock objet vlaku
	 * 
	 * @return - vrací Mock objet vlaku
	 */
	public Train getTrain() {
		
		Train train = new Train();
		train.setId(random.nextInt());
		train.setName("Train");
		train.setWifi(true);
		train.setRestaurant(false);
		train.setHandicap(true);
		train.setBike(false);
		train.setWagonsCount(random.nextInt());
		
		
		return train;
	}
	
	
	/**
	 * Mock objekt počáteční trasy
	 * 
	 * @return - vrací Mock objekt počáteční trasy
	 */
	public Route getStartRoute() {
		
		Route startRoute = new Route();
		startRoute.setId(random.nextInt());
		startRoute.setArrival(null);
		startRoute.setDeparture(startRouteTime);
		startRoute.setDistance(random.nextInt());
		startRoute.setStationOrder(random.nextInt());
		
		StationName startRouteStation = new StationName();
		startRouteStation.setId(random.nextInt());
		startRouteStation.setName("Prague Main Railway Station");
		startRoute.setStationName(startRouteStation);
		
		return startRoute;
	}
	
	
	/**
	 * Mock objekt cílové trasy
	 * 
	 * @return - vrací Mock objekt cílové trasy
	 */
	public Route getEndRoute() {
		
		Route endRoute = new Route();
		endRoute.setId(random.nextInt());
		endRoute.setArrival(endRouteTime);
		endRoute.setDeparture(null);
		endRoute.setDistance(random.nextInt());
		endRoute.setStationOrder(random.nextInt());
		
		StationName endRouteStation = new StationName();
		endRouteStation.setId(random.nextInt());
		endRouteStation.setName("Brno Main Railway Station");
		endRoute.setStationName(endRouteStation);
		
		return endRoute;
	}
	
	
	/**
	 * Mock objekt spoje
	 * 
	 * @return - vrací Mock objekt spoje
	 */
	public Connection getConnection() {
		
		Connection connection = new Connection();
		connection.setId(random.nextInt());
		
		Company company = new Company();
		company.setId(random.nextInt());
		company.setName("Company");
		company.setChildTariff(random.nextDouble());
		company.setStudentTariff(random.nextDouble());
		company.setAdultTariff(random.nextDouble());
		company.setSeniorTariff(random.nextDouble());
		company.setClassCharge(random.nextDouble());
		connection.setCompany(company);
		
		TrainName trainName = new TrainName();
		trainName.setId(random.nextInt());
		trainName.setName("Train");
		connection.setTrainName(trainName);
		
		return connection;
	}
	
	
	/**
	 * Mock objekt názvu spoje
	 * 
	 * @return - vrací Mock objekt názvu spoje
	 */
	public ConnectionName getConnectionName() {
		
		ConnectionName connectionName = new ConnectionName();
		connectionName.setId(random.nextInt());
		
		CompanyName companyName = new CompanyName();
		companyName.setId(random.nextInt());
		companyName.setName("Company");
		connectionName.setCompanyNames(companyName);
		
		TrainName trainName = new TrainName();
		trainName.setId(random.nextInt());
		trainName.setName("Train");
		connectionName.setTrainName(trainName);
		
		return connectionName;
	}
	
	
	/**
	 * Mock objekt kontrolního bodu
	 * 
	 * @return - vrací Mock objekt kontrolního bodu
	 */
	public Checkpoint getCheckpoint() {
		
		Checkpoint checkpoint = new Checkpoint();
		checkpoint.setId(random.nextInt());
		checkpoint.setPositionX(random.nextInt());
		checkpoint.setPositionY(random.nextInt());
		
		return checkpoint;
	}
	
	
	/**
	 * Mock objekt stanice
	 * 
	 * @return - vrací Mock objekt stanice
	 */
	public Station getStation() {
		
		Station station = new Station();
		station.setId(random.nextInt());
		station.setName("Praha hlavní nádraží");
		station.setPositionX(random.nextInt());
		station.setPositionY(random.nextInt());
		
		return station;
	}
	
}
