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

import cz.railway_system.entities.Checkpoint;
import cz.railway_system.entities.ConnectionName;
import cz.railway_system.entities.Route;
import cz.railway_system.pojo.TrainMap;
import cz.railway_system.services.TrainMapService;

@Service
public class TrainMapImpl implements TrainMapService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Získání seznamu aktuálně probíhajících spojů
	 * 
	 * @return - vrací List probíhajících spojů
	 */
	@Override
	@Transactional
	public List<TrainMap> getCurrentRoutes() {
		
		List<TrainMap> trainsMap = new ArrayList<>();
		
		LocalTime currentTime = LocalTime.now();
		
// Seznam ID, podle aktuálního času ///////////////////////////////////////////////////////		
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
				
		"SELECT connection_id "
	  + "FROM routes "
	  + "WHERE departure <= :currentTime "
	  + "AND station_order = 1 "
	  + "GROUP BY connection_id");
		
		// Parametr
		query.setParameter("currentTime", currentTime);
		
		List<Integer> connectionIDs = query.getResultList();
		
// Vyfiltrování spojů v aktuálním čase ////////////////////////////////////////////////////		
		
		for (int connectionID : connectionIDs) {

			query = session.createQuery(
			
			"FROM Route "
		  + "WHERE connection_id = :connectionID "
		  + "ORDER BY station_order ");
			
			// Parametr
			query.setParameter("connectionID", connectionID);
			
			List<Route> routes = query.getResultList();
			
			for (int i = 0; i < routes.size(); i++) {
				
				Route route = routes.get(i);
				
				// Čas příjezdu (odjezdu, při null hodnotě)
				LocalTime arrivalTime = (route.getArrival() != null 
					? route.getArrival() : route.getDeparture());

				// Nastavení počáteční / cílové trasy, při překročení aktuálního času
				if (arrivalTime.isAfter(currentTime)) {

					TrainMap trainMap = new TrainMap();
					
					// Získání názvu vlaku
					ConnectionName connectionTrainName = session.get(ConnectionName.class, connectionID);
					trainMap.setConnectionName(connectionTrainName);
					
					// Předešlá trasa + ošetření indexu
					trainMap.setStartRoute((i-- < 0) ? routes.get(0) : routes.get(i--));
					trainMap.setEndRoute(route);
					trainsMap.add(trainMap);
					
					break;
				}
			}
		}
		
		return trainsMap;
	}
	
	
	/**
	 * Získání kontrolních bodů trasy
	 */
	@Override
	@Transactional
	public Checkpoint getChecpointPosition(int startStationID, int endStationID, int percentage) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				
		"FROM Checkpoint "
	  + "WHERE start_station_id = :startStationID "
	  + "AND end_station_id = :endStationID "
	  + "AND percentage = :percentage ");
		
		// Parametry
		query.setParameter("startStationID", startStationID);
		query.setParameter("endStationID", endStationID);
		query.setParameter("percentage", percentage);
		
		// Otočení směru
		if (query.getResultList().isEmpty()) {
			
			query = session.createQuery(
			
			"FROM Checkpoint "
		  + "WHERE start_station_id = :startStationID "
		  + "AND end_station_id = :endStationID "
		  + "AND percentage = :percentage ");
			
			// Parametry (otočené)
			query.setParameter("startStationID", endStationID);
			query.setParameter("endStationID", startStationID);
			query.setParameter("percentage", 100 - percentage);
		}
		
		Checkpoint checkpoint = (Checkpoint) query.uniqueResult();
		
		return checkpoint;
	}
	
}