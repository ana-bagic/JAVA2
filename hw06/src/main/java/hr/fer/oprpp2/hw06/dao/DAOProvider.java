package hr.fer.oprpp2.hw06.dao;

import hr.fer.oprpp2.hw06.dao.jpa.JPADAOImpl;

/**
 * Class provides instance of the DAO object used in this application.
 * 
 * @author Ana Bagić
 *
 */
public class DAOProvider {

	/** Instance of the DAO interface used in this application. */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Returns the DAO instance used in this application.
	 * 
	 * @return DAO instance
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}