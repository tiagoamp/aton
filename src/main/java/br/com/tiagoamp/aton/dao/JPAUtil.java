package br.com.tiagoamp.aton.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	public static final String PERSISTENCE_UNIT_NAME = "PU_ATON";
	public static final String PERSISTENCE_UNIT_NAME_TESTS = "PU_ATON_TESTS";
	
	private EntityManagerFactory emf;
		
	public EntityManager getMyEntityManager() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		return emf.createEntityManager();
	}
	
	public EntityManager getMyTestsEntityManager() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_TESTS);
		return emf.createEntityManager();
	}

}
