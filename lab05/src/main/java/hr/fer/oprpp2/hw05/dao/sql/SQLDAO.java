package hr.fer.oprpp2.hw05.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import hr.fer.oprpp2.hw05.dao.DAO;
import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.model.Poll;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Implementation of the DAO interface using SQL technology.
 *  
 * @author Ana Bagić
 * 
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			try(PreparedStatement pst = con.prepareStatement("SELECT id, title, message from Polls order by id")) {
				try(ResultSet rset = pst.executeQuery()) {
					List<Poll> polls = new LinkedList<>();
					while(rset.next()) {
						polls.add(new Poll(rset.getLong(1), rset.getString(2), rset.getString(3)));
					}
					
					if(polls.isEmpty()) {
						throw new DAOException("There are no polls available right now.");
					}
					
					return polls;
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public Poll getPoll(long pollID) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			try(PreparedStatement pst = con.prepareStatement("SELECT title, message from Polls where id=?")) {
				pst.setLong(1, pollID);
				try(ResultSet rset = pst.executeQuery()) {
					if(!rset.next()) {
						throw new DAOException("Poll with id " + pollID + " does not exist.");
					}
					
					return new Poll(pollID, rset.getString(1), rset.getString(2));
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<PollOption> getOptions(long pollID) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			try(PreparedStatement pst = con.prepareStatement("SELECT id, optionTitle, optionLink, votesCount from PollOptions where pollID=?")) {
				pst.setLong(1, pollID);
				try(ResultSet rset = pst.executeQuery()) {
					List<PollOption> options = new LinkedList<>();
					while(rset.next()) {
						options.add(new PollOption(rset.getLong(1), rset.getString(2), rset.getString(3), pollID, rset.getLong(4)));
					}
					
					if(options.isEmpty()) {
						throw new DAOException("There are no options available right now.");
					}
					
					return options;
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public PollOption getOption(long id) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			try(PreparedStatement pst = con.prepareStatement("SELECT optionTitle, optionLink, pollId, votesCount from PollOptions where id=?")) {
				pst.setLong(1, id);
				try(ResultSet rset = pst.executeQuery()) {
					if(!rset.next()) {
						throw new DAOException("Poll option with id " + id + " does not exist.");
					}
					
					return new PollOption(id, rset.getString(1), rset.getString(2), rset.getLong(3), rset.getLong(4));
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void updateOption(PollOption option) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			try(PreparedStatement pst = con.prepareStatement("UPDATE PollOptions set optionTitle=?, optionLink=?, votesCount=? where id=?")) {
				pst.setString(1, option.getTitle());
				pst.setString(2, option.getLink());
				pst.setLong(3, option.getVotesCount());
				pst.setLong(4, option.getId());
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e.getCause());
		}
	}

}