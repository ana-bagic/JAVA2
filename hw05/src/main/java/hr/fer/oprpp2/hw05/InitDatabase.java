package hr.fer.oprpp2.hw05;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import hr.fer.oprpp2.hw05.model.Poll;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Helper class that fills the database with initial data.
 * 
 * @author Ana Bagić
 *
 */
public class InitDatabase {
	
	/** SQL command that creates POLLS table. */
	private static final String CREATE_POLLS = "CREATE TABLE Polls"
			+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
			+ " title VARCHAR(150) NOT NULL,"
			+ " message CLOB(2048) NOT NULL)";
	
	/** SQL command that creates POLLOPTIONS table. */
	private static final String CREATE_POLL_OPTIONS = "CREATE TABLE PollOptions"
			+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
			+ " optionTitle VARCHAR(100) NOT NULL,"
			+ " optionLink VARCHAR(150) NOT NULL,"
			+ " pollID BIGINT,"
			+ " votesCount BIGINT,"
			+ " FOREIGN KEY (pollID) REFERENCES Polls(id) ON DELETE CASCADE)";
	
	/** SQL command that inserts poll in the POLLS table. */
	private static final String INSERT_POLL = "INSERT INTO Polls"
			+ "(title, message) values (?,?)";
	
	/** SQL command that inserts option in the POLLOPTIONS table. */
	private static final String INSERT_OPTION = "INSERT INTO PollOptions"
			+ "(optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)";
	
	/**
	 * Creates POLLS and POLLOPTIONS tables if they don't exist and fills the with data if they are empty.
	 * 
	 * @param cpds data source to get connections from
	 */
	public static void createTablesIfNecessary(ComboPooledDataSource cpds) {
		try(Connection con = cpds.getConnection()) {
			DatabaseMetaData dbmd = con.getMetaData();
			try(ResultSet rs = dbmd.getTables(null, null, null, null)) {
				Set<String> tables = new HashSet<>();
				while(rs.next()) {
					tables.add(rs.getString(3));
				}
				
				if(!tables.contains("POLLS")) {
					try(PreparedStatement pst = con.prepareStatement(CREATE_POLLS)) {
						pst.execute();
					}
				}
				
				if(!tables.contains("POLLOPTIONS")) {
					try(PreparedStatement pst = con.prepareStatement(CREATE_POLL_OPTIONS)) {
						pst.execute();
					}
				}
			}
			
			try(PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLS")) {
				try(ResultSet rs = pst.executeQuery()) {
					if(!rs.next()) {
						fillPolls(con);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fills table POLLS with new data.
	 * 
	 * @param con connection to the database
	 */
	private static void fillPolls(Connection con) {
		for(var poll : createPolls().entrySet()) {
			try(PreparedStatement pst = con.prepareStatement(INSERT_POLL, Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, poll.getKey().getTitle());
				pst.setString(2, poll.getKey().getMessage());
				
				pst.executeUpdate();
				
				try(ResultSet rs = pst.getGeneratedKeys()) {
					if(rs != null && rs.next()) {
						long id = rs.getLong(1);
						poll.getValue().forEach(o -> o.setPollId(id));
						fillOptions(con, poll.getValue());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fills table POLLSOPTIONS with new data.
	 * 
	 * @param con connection to the database
	 * @param options list of options to add to the database
	 */
	private static void fillOptions(Connection con, List<PollOption> options) {
		for(PollOption option : options) {
			try(PreparedStatement pst = con.prepareStatement(INSERT_OPTION, Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, option.getTitle());
				pst.setString(2, option.getLink());
				pst.setLong(3, option.getPollId());
				pst.setLong(4, option.getVotesCount());
				
				pst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates new polls and poll options.
	 * 
	 * @return a map of polls and their options.
	 */
	private static Map<Poll, List<PollOption>> createPolls() {
		Map<Poll, List<PollOption>> polls = new HashMap<>();
		List<PollOption> p1 = new LinkedList<>();
		
		p1.add(new PollOption("Queen", "https://www.youtube.com/watch?v=2ZBtPf7FOoM", 52));
		p1.add(new PollOption("Saint Motel", "https://www.youtube.com/watch?v=IyVPyKrx0Xo", 43));
		p1.add(new PollOption("alt-J", "https://www.youtube.com/watch?v=Qg6BwvDcANg", 50));
		p1.add(new PollOption("KEiiNO", "https://www.youtube.com/watch?v=jQaiarjof2k", 37));
		p1.add(new PollOption("Bastille", "https://www.youtube.com/watch?v=ZCTDKLjdok4", 60));
		p1.add(new PollOption("Imagine Dragons", "https://www.youtube.com/watch?v=mWRsgZuwf_8", 26));
		p1.add(new PollOption("The Killers", "https://www.youtube.com/watch?v=RIZdjT1472Y", 17));
		p1.add(new PollOption("The Lumineers", "https://www.youtube.com/watch?v=pTOC_q0NLTk", 20));
		p1.add(new PollOption("The Neighbourhood", "https://www.youtube.com/watch?v=GCdwKhTtNNw", 25));
		p1.add(new PollOption("Coldplay", "https://www.youtube.com/watch?v=1G4isv_Fylg", 77));
		
		polls.put(new Poll("Vote for your favourite band:", "Which band, from the ones that are given, is your favourite?"), p1);
		
		List<PollOption> p2 = new LinkedList<>();
		
		p2.add(new PollOption("Rihanna", "https://www.youtube.com/watch?v=sEhy-RXkNo0", 82));
		p2.add(new PollOption("Nicki Minaj", "https://www.youtube.com/watch?v=4JipHEz53sU", 72));
		p2.add(new PollOption("Billie Eilish", "https://www.youtube.com/watch?v=EgBJmlPo8Xw", 54));
		p2.add(new PollOption("Lorde", "https://www.youtube.com/watch?v=f2JuxM-snGc", 52));
		p2.add(new PollOption("Taylor Swift", "https://www.youtube.com/watch?v=RsEZmictANA", 43));
		p2.add(new PollOption("Harry Styles", "https://www.youtube.com/watch?v=P3cffdsEXXw", 64));
		p2.add(new PollOption("The Weeknd", "https://www.youtube.com/watch?v=4NRXx6U8ABQ", 27));
		p2.add(new PollOption("Khalid", "https://www.youtube.com/watch?v=by3yRdlQvzs", 41));
		p2.add(new PollOption("Lil Nas X", "https://www.youtube.com/watch?v=6swmTBVI83k", 52));
		p2.add(new PollOption("Troye Sivan", "https://www.youtube.com/watch?v=LniYWYmNXiM", 36));
		
		polls.put(new Poll("Vote for your favourite solo singer:", "Which singer, from the ones that are given, is your favourite?"), p2);
		
		return polls;
	}

}
