package cz.railway_system.services.impl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cz.railway_system.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Získání uživatele podle ID
	 */
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		
		Session session = sessionFactory.getCurrentSession();
		UserDetails userDetails = session.get(User.class, Integer.parseInt(id));
		
		return userDetails;
	}

}
