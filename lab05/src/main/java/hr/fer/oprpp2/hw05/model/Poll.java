package hr.fer.oprpp2.hw05.model;

/**
 * Class models a poll in the voting application.
 * 
 * @author Ana Bagić
 *
 */
public class Poll {

	/** Poll id. */
	private long id;
	/** Poll title. */
	private String title;
	/** Poll message. */
	private String message;
	
	/**
	 * Creates a new poll with the given title and message.
	 * 
	 * @param title of the poll
	 * @param message of the poll
	 */
	public Poll(String title, String message) {
		this(-1, title, message);
	}

	/**
	 * Creates a new poll with the given id and title.
	 * 
	 * @param id of the poll
	 * @param title of the poll
	 * @param message of the poll
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * @return the poll id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the poll title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the poll message
	 */
	public String getMessage() {
		return message;
	}
	
}
