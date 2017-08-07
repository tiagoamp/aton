package br.com.tiagoamp.aton.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	public static final String PERSISTENCE_UNIT_NAME = "PU_ATON";
	public static final String PERSISTENCE_UNIT_NAME_TESTS = "PU_ATON_TESTS";
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	private static EntityManagerFactory emfTests = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_TESTS);
	
	public EntityManager getMyEntityManager() {
		return emf.createEntityManager();
	}
	
	public EntityManager getMyTestsEntityManager() {
		return emfTests.createEntityManager();
	}

}
