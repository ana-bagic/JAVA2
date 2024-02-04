package hr.fer.oprpp2.hw05.model;

/**
 * Class models a poll option in a poll in the voting application.
 * 
 * @author Ana Bagić
 *
 */
public class PollOption {

	/** Poll option id. */
	private long id;
	/** Poll option title. */
	private String title;
	/** Link to the poll option video. */
	private String link;
	/** Id of the poll this poll option is in. */
	private long pollId;
	/** Number of votes given to this option. */
	private long votesCount;
	
	/**
	 * Creates a new poll option with the given title, link and number of votes.
	 * 
	 * @param title of the poll option
	 * @param link to the poll option video
	 * @param votesCount number of votes given to this option
	 */
	public PollOption(String title, String link, long votesCount) {
		this(-1, title, link, -1, votesCount);
	}

	/**
	 * Creates a new poll option with the given id, title, link, pollId and number of votes.
	 * 
	 * @param id of the poll option
	 * @param title of the poll option
	 * @param link to the poll option video
	 * @param pollId of the poll this poll option is in
	 * @param votesCount number of votes given to this option
	 */
	public PollOption(long id, String title, String link, long pollId, long votesCount) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.pollId = pollId;
		this.votesCount = votesCount;
	}

	/**
	 * @return the id of the option
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the title of the option
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the link of the option's video
	 */
	public String getLink() {
		return link;
	}
	
	/**
	 * @return the id of the poll this option is in
	 */
	public long getPollId() {
		return pollId;
	}
	
	/**
	 * @param pollId the if of the poll this option is in to set
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}
	
	/**
	 * @return the number of votes given to this option
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * @param votesCount to set
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}
	
}
