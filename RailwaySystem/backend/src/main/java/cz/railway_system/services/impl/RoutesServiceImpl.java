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
import cz.railway_system.enums.LoadOption;
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
	 * @param startTime - čas od
	 * @param endTime - čas do
	 * @param maxResult - počet výsledků
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List id spojů
	 */
	@Override
	@Transactional
	public List<Integer> getConnectionIDs(int startStationID, int endStationID, 
			LocalTime startTime, LocalTime endTime, int maxResult, LoadOption loadOption) {
		
		List<Integer> connectionIDs = new ArrayList<>();
		
		String direction = (loadOption == LoadOption.later ? "" : "DESC");
		
// Seznamu ID, obsahující požadované vstupní / výstupní nádraží //////////////////////////
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
			
		"SELECT connection_id "
	  + "FROM routes "
	  + "WHERE station_id = :startStationID OR station_id = :endStationID "
	  + "GROUP BY connection_id "
	  + "HAVING COUNT(*) > 1 "
	  + "ORDER BY MIN(departure) " + direction);
		
		// Parametry
		query.setParameter("startStationID", startStationID);
		query.setParameter("endStationID", endStationID);
		
		List<Integer> tempIDs = query.getResultList();
		
// Kontrola směru trasy //////////////////////////////////////////////////////////////////
		
		for (int connectionID : tempIDs) {
			
			query = session.createSQLQuery(
					
			"SELECT connection_id "
		  +	"FROM routes "
		  + "WHERE ("
					
		  		+ "SELECT station_order "
		  		+ "FROM routes "
		  		+ "WHERE connection_id = :connectionID "
		  		+ "AND station_id = :startStationID "
		  		+ "AND departure BETWEEN :startTime AND :endTime"
		  
		  + ") < ("
		  
		  		+ "SELECT station_order "
		  		+ "FROM routes "
		  		+ "WHERE connection_id = :connectionID "
		  		+ "AND station_id = :endStationID "
	  
	  	  + ") AND connection_id = :connectionID "
	  	  + "GROUP BY connection_id");
			
			// Parametry
			query.setParameter("connectionID", connectionID);
			query.setParameter("startStationID", startStationID);
			query.setParameter("endStationID", endStationID);
			query.setParameter("startTime", startTime);
			query.setParameter("endTime", endTime);
			
			if (query.uniqueResult() != null) {
				
				connectionIDs.add((Integer) query.uniqueResult());
				
				// Přerušení cyklu, po dosažení počtu výsledků
				if (connectionIDs.size() == maxResult) break;
			}
		}
		
		return connectionIDs;
	}
	
	
	/**
	 * Získání seznamu spojů
	 * 
	 * @param connectionIDs - id spoje
	 * @param startStationID - id startovního nádraží
	 * @param endStationID - id cílového nádraží
	 * @param loadOption - Enum volba načítání [previous, next] 
	 * 
	 * @return - vrací List spojů
	 */
	@Override
	@Transactional
	public List<RouteInfo> getRoutesInfo(List<Integer> connectionIDs, int startStationID, 
			int endStationID, LoadOption loadOption) {
		
		List<RouteInfo> routesInfo = new ArrayList<>();
		
		Session session = sessionFactory.getCurrentSession();
		
		for (int connectionID : connectionIDs) {
			
			RouteInfo routeInfo = new RouteInfo();
			
			// Nastavení informací o spoji
			Connection connection = new Connection();
			connection = session.get(Connection.class, connectionID);
			routeInfo.setConnection(connection);
			
			Query query = session.createQuery(
					
			"FROM Route "
		  + "WHERE (station_id = :startStationID OR station_id = :endStationID) "
		  + "AND connection_id = :connectionID "
		  + "ORDER BY station_order", Route.class);
			
			// Parametry
			query.setParameter("connectionID", connectionID);
			query.setParameter("startStationID", startStationID);
			query.setParameter("endStationID", endStationID);
			
			// Nastavení počátečního a cílové trasy
			if (!query.list().isEmpty()) {
				
				Route startRoute = (Route) query.getResultList().get(0);
				Route endRoute = (Route) query.getResultList().get(1);
				
				routeInfo.setStartRoute(startRoute);
				routeInfo.setEndRoute(endRoute);
				routesInfo.add(routeInfo);
			}
		}
		
		// Setřízení starších spojů, podle času odjezdu
		if (loadOption == LoadOption.previous) {
			
			Collections.sort(routesInfo, (a, b) -> a.getStartRoute().getDeparture()
					.compareTo(b.getStartRoute().getDeparture()));
		}
		
		return routesInfo;
	}

	
	/**
	 * Získání detailu spoje
	 * 
	 * @param connectionID - id spoje
	 * 
	 * @return - vrací detail spoje
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
