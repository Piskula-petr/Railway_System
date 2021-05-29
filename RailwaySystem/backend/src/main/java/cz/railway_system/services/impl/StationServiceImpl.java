package cz.railway_system.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.railway_system.entities.Station;
import cz.railway_system.services.StationService;

@Service
public class StationServiceImpl implements StationService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Získání seznamu železničních stanic
	 * 
	 * @return - vrací List železničních stanic
	 */
	@Transactional
	@Override
	public List<Station> getStations() {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Station", Station.class);
		
		List<Station> stations = query.getResultList();
		
		return stations;
	}

}
