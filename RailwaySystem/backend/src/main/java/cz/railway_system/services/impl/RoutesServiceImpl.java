package cz.railway_system.services.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.railway_system.entities.Connection;
import cz.railway_system.entities.Route;
import cz.railway_system.entities.Train;
import cz.railway_system.pojo.RouteDetail;
import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RoutesService;

@Service
public class RoutesServiceImpl implements RoutesService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Získání seznamu id spojů
	 * 
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * 
	 * @return - vrací List id spojů
	 */
	@Override
	@Transactional
	public List<Integer> getConnectionIDs(int startStationID, int endStationID) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
			
		"SELECT connection_id "
	  + "FROM routes "
	  + "WHERE station_id = :startStationID OR station_id = :endStationID "
	  + "GROUP BY connection_id "
	  + "HAVING COUNT(*) > 1 "
	  + "ORDER BY connection_id");
		
		// Parametry
		query.setParameter("startStationID", startStationID);
		query.setParameter("endStationID", endStationID);
		
		List<Integer> connectionIDs = query.getResultList();
		
		return connectionIDs;
	}
	
	
	/**
	 * Získání seznamu informací o trasách
	 * 
	 * @param connectionIDs - id spoje
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * @param departure - čas odjezdu
	 * 
	 * @return - vrací List informací o trasách
	 */
	@Override
	@Transactional
	public List<RouteInfo> getRoutesInfo(List<Integer> connectionIDs, int startStationID, int endStationID, LocalTime departureTime) {
		
		List<RouteInfo> routesInfo = new ArrayList<>();
		
		Session session = sessionFactory.getCurrentSession();
		
		for (int connectionID : connectionIDs) {
			
			RouteInfo routeInfo = new RouteInfo();
			
			// Nastavení informace o spoji
			Connection connection = new Connection();
			connection = session.get(Connection.class, connectionID);
			routeInfo.setConnection(connection);
			
			Query query = session.createQuery(
					
			"FROM Route "
		  + "WHERE ("
					
		  		+ "FROM RouteOrder "
		  		+ "WHERE connection_id = :routeID "
		  		+ "AND station_id = :startStationID "
		  		+ "AND departure >= :departureTime"
		  		
		  + ") < ("
		  
		  		+ "FROM RouteOrder "
		  		+ "WHERE connection_id = :routeID "
		  		+ "AND station_id = :endStationID "
		  		
		  + ") AND (station_id = :startStationID OR station_id = :endStationID) "
		  + "AND connection_id = :routeID "
		  + "ORDER BY station_order", Route.class);
			
			// Parametry
			query.setParameter("routeID", connectionID);
			query.setParameter("startStationID", startStationID);
			query.setParameter("endStationID", endStationID);
			query.setParameter("departureTime", departureTime);
			
			// Nastavení počátečního a cílového trasy
			if (!query.list().isEmpty()) {
				
				Route startRoute = (Route) query.getResultList().get(0);
				Route endRoute = (Route) query.getResultList().get(1);
				
				routeInfo.setStartRoute(startRoute);
				routeInfo.setEndRoute(endRoute);
				routesInfo.add(routeInfo);
			}
		}
		
		// Setřízení Listu, podle času odjezdu
		Collections.sort(routesInfo, (a, b) -> a.getStartRoute().getDeparture()
				.compareTo(b.getStartRoute().getDeparture()));
		
		return routesInfo;
	}

	
	/**
	 * Získání detailu o trase
	 * 
	 * @param connectionID - id spoje
	 * 
	 * @return - vrací detail o trase
	 */
	@Override
	@Transactional
	public RouteDetail getRouteDetail(int connectionID) {
		
		RouteDetail routeDetail = new RouteDetail();
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNativeQuery(
				
		"SELECT * FROM trains "
	  + "WHERE id = ("
	  
	  		+ "SELECT train_id FROM connections "
	  		+ "WHERE id = :connectionID "
	  		
	  + ")", Train.class);
		
		// Parametr
		query.setParameter("connectionID", connectionID);
		
		// Nastavení informací o vlaku
		Train train = (Train) query.uniqueResult();
		routeDetail.setTrain(train);
		
		query = session.createQuery(
			
		"FROM Route "
	  + "WHERE connection_id = :connectionID "
	  + "ORDER BY station_order", Route.class);
		
		// Parametr
		query.setParameter("connectionID", connectionID);
		
		// Nastavení trasy
		List<Route> routes = query.list();
		routeDetail.setRoutes(routes);
		
		return routeDetail;
	}
	
}
