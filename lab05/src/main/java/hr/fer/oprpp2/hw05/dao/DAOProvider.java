package hr.fer.oprpp2.hw05.dao;

import hr.fer.oprpp2.hw05.dao.sql.SQLDAO;

/**
 * Class provides instance of the DAO object used in this application.
 * 
 * @author Ana Bagić
 *
 */
public class DAOProvider {

	/** Instance of the DAO interface used in this application. */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Returns the DAO instance used in this application.
	 * 
	 * @return DAO instance
	 */
	public static DAO getDao() {
		return dao;
	}
	
}