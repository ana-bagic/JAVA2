package hr.fer.oprpp2.hw06.dao;

import java.util.List;

import hr.fer.oprpp2.hw06.model.BlogEntry;
import hr.fer.oprpp2.hw06.model.BlogUser;

/**
 * Interface to the data persistence subsystem.
 * 
 * @author Ana Bagić
 *
 */
public interface DAO {

	/**
	 * Returns an entry with the given id, or <code>null</code> if the entry with the given id does not exist.
	 * 
	 * @param id of the entry
	 * @return an entry with the given id, or <code>null</code> if the entry with the given id does not exist
	 * @throws DAOException if error occurs while fetching data from the database
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Returns a list of all the users stored in the database.
	 * 
	 * @return a list of all the users stored in the database
	 */
	public List<BlogUser> getAllUsers();

	/**
	 * Returns a user with the given nickname, or <code>null</code> if one does not exist.
	 * 
	 * @param nick nickname of the user to get
	 * @return a user with the given nickname, or <code>null</code> if one does not exist
	 */
	public BlogUser getUser(String nick);

	/**
	 * Stored the new given object in the database.
	 * 
	 * @param object to store in the database
	 */
	public void save(Object object);

	/**
	 * Updates the existing object in the database to the given.
	 * 
	 * @param object to update to
	 */
	public void update(Object object);
	
}