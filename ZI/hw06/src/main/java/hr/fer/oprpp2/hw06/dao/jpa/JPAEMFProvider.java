package hr.fer.oprpp2.hw06.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Class is used to get the entity manager factory used in this application.
 * 
 * @author Ana Bagić
 *
 */
public class JPAEMFProvider {

	/** The entity manager factory used in this application. */
	public static EntityManagerFactory emf;
	
	/**
	 * @return the entity manager factory used in this applicaiton
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * @param emf the entity manager factory to set
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}