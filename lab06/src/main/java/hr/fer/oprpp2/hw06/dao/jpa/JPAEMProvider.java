package hr.fer.oprpp2.hw06.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.oprpp2.hw06.dao.DAOException;

/**
 * Class stores entity manager for each thread.
 * 
 * @author Ana Bagić
 *
 */
public class JPAEMProvider {

	/** Mapping from threads to entity managers. */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Returns the entity manager that is saved for this thread. If one does not exist, then new one is created, stored and started.
	 * 
	 * @return entity manager for this thread
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em == null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Commits the transaction for the entity manager of this thread and closes it.
	 * 
	 * @throws DAOException if an error occurs while commiting transaction and closing entity manager
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em == null) return;
		
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch(Exception ex) {
			if(dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex != null) throw dex;
	}
	
}