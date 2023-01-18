package hr.fer.oprpp2.hw05.dao.sql;

import java.sql.Connection;

/**
 * Class stores connection for each thread.
 * 
 * @author Ana Bagić
 *
 */
public class SQLConnectionProvider {

	/** Mapping from threads to connections to database. */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Sets the connection to the current thread to given, or removes set connection for this thread if argument is <code>null</code>.
	 * 
	 * @param con connection to the database
	 */
	public static void setConnection(Connection con) {
		if(con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Returns the connection to the database that is saved for this thread.
	 * 
	 * @return connection to the database
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}