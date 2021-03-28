package cz.railway_system.services.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.railway_system.entities.Route;
import cz.railway_system.pojo.RouteInfo;
import cz.railway_system.services.RoutesService;

@Service
public class RoutesServiceImpl implements RoutesService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<Integer> getRouteIDs(int startStationID, int endStationID) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
			
		"SELECT route_id "
	  + "FROM routes "
	  + "WHERE station_id = :startStationID OR station_id = :endStationID "
	  + "GROUP BY route_id "
	  + "HAVING COUNT(*) > 1 "
	  + "ORDER BY route_id");
		
		query.setParameter("startStationID", startStationID);
		query.setParameter("endStationID", endStationID);
		
		List<Integer> routeIDs = query.getResultList();
		
		return routeIDs;
	}
	
	
	@Override
	@Transactional
	public List<RouteInfo> getRouteInfo(List<Integer> routeIDs, int startStationID, int endStationID, LocalTime departureTime) {
		
		Session session = sessionFactory.getCurrentSession();
		
		List<RouteInfo> routesInfo = new ArrayList<>();
		
		for (int routeID : routeIDs) {
			
			Query query = session.createQuery(
					
			"FROM Route "
		  + "WHERE ("
					
		  		+ "FROM RouteOrder "
		  		+ "WHERE route_id = :routeID "
		  		+ "AND station_id = :startStationID "
		  		+ "AND departure >= :departureTime"
		  		
		  + ") < ("
		  
		  		+ "FROM RouteOrder "
		  		+ "WHERE route_id = :routeID "
		  		+ "AND station_id = :endStationID "
		  		
		  + ") AND (station_id = :startStationID OR station_id = :endStationID) "
		  + "AND route_id = :routeID "
		  + "ORDER BY station_order", Route.class);
			
			query.setParameter("routeID", routeID);
			query.setParameter("startStationID", startStationID);
			query.setParameter("endStationID", endStationID);
			query.setParameter("departureTime", departureTime);
			
			if (!query.list().isEmpty()) {
				
				Route startRoute = (Route) query.getResultList().get(0);
				Route endRoute = (Route) query.getResultList().get(1);
				
				RouteInfo routeInfo = new RouteInfo();
				routeInfo.setStartRoute(startRoute);
				routeInfo.setEndRoute(endRoute);
				routesInfo.add(routeInfo);
			}
		}
		
		System.out.println(routesInfo.size());
		
		return routesInfo;
	}

}
