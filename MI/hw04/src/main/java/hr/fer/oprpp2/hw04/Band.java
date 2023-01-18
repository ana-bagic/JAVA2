package hr.fer.oprpp2.hw04;

/**
 * Class models band used for voting app.
 * 
 * @author Ana Bagić
 *
 */
public class Band {

	/** Band id. */
	private Integer id;
	/** Band name. */
	private String name;
	/** Link to one of band's best songs. */
	private String songLink;
	/** Number of votes that the band has received. */
	private Integer votes;
	
	/**
	 * Constructor creates new band using given parameters.
	 * 
	 * @param id band id
	 * @param name band name
	 * @param songLink link to one of band's best songs
	 */
	public Band(Integer id, String name, String songLink) {
		this.id = id;
		this.name = name;
		this.songLink = songLink;
	}
	
	/**
	 * Constructor creates new band using given parameters.
	 * 
	 * @param id band id
	 * @param votes number of votes that the band has received
	 */
	public Band(Integer id, Integer votes) {
		this.id = id;
		this.votes = votes;
	}
	
	/**
	 * @return band id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @return band name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return link to one of band's best songs
	 */
	public String getSongLink() {
		return songLink;
	}
	
	/**
	 * @return number of votes that the band has received
	 */
	public Integer getVotes() {
		return votes;
	}

	/**
	 * Sets the number of votes to given, or to 0 if argument is <code>null</code>.
	 * 
	 * @param votes number of votes
	 */
	public void setVotes(Integer votes) {
		this.votes = votes != null ? votes : 0;
	}
	
}
