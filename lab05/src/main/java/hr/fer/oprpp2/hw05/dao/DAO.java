package hr.fer.oprpp2.hw05.dao;

import java.util.List;

import hr.fer.oprpp2.hw05.model.Poll;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Interface to the data persistence subsystem.
 * 
 * @author Ana Bagić
 *
 */
public interface DAO {
	
	/**
	 * Returns list of all polls stored in the database.
	 * 
	 * @return list of all polls stored in the database
	 * @throws DAOException if error occurs while fetching data from the database
	 */
	public List<Poll> getPolls() throws DAOException;
	
	/**
	 * Returns a poll with the given pollID.
	 * 
	 * @param pollID od the poll to get
	 * @return a poll with the given pollID
	 * @throws DAOException if error occurs while fetching data from the database
	 */
	public Poll getPoll(long pollID) throws DAOException;
	
	/**
	 * Returns a list of poll options of the poll with the given pollID.
	 * 
	 * @param pollID of the poll to get the options from
	 * @return a list of poll options of the poll with the given pollID
	 * @throws DAOException if error occurs while fetching data from the database
	 */
	public List<PollOption> getOptions(long pollID) throws DAOException;
	
	/**
	 * Returns a poll option with the given id.
	 * 
	 * @param id of the poll option to get
	 * @return a poll option with the given id
	 * @throws DAOException if error occurs while fetching data from the database
	 */
	public PollOption getOption(long id) throws DAOException;
	
	/**
	 * Updates the poll option to the one given.
	 * 
	 * @param option poll option to update
	 * @throws DAOException if error occurs while updating data in the database
	 */
	public void updateOption(PollOption option) throws DAOException;
}