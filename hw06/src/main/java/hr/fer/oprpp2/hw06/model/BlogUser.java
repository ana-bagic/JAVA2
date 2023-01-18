package hr.fer.oprpp2.hw06.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Class models a user in the blog application. This class is an entity in the database.
 * 
 * @author Ana Bagić
 *
 */
@NamedQueries({
	@NamedQuery(name = "BlogUser.all", query = "select u from BlogUser as u"),
	@NamedQuery(name = "BlogUser.findByNick", query = "select u from BlogUser as u where u.nick=:nick")
})
@Entity
@Table(name = "blog_users")
public class BlogUser {

	/** Id of the user. */
	private Long id;
	/** First name of the user. */
	private String firstName;
	/** Last name of the user. */
	private String lastName;
	/** Nickname of the user (username). */
	private String nick;
	/** Email of the user. */
	private String email;
	/** Hashed password of the user. */
	private String passwordHash;
	/** List of the entries this user has posted. */
	private List<BlogEntry> entries = new LinkedList<>();
	
	/**
	 * @return the id of the user
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the first name of the user
	 */
	@Column(length = 100, nullable = false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the last name of the user
	 */
	@Column(length = 100, nullable = false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the nickname of the user (username)
	 */
	@Column(length = 100, nullable = false, unique = true)
	public String getNick() {
		return nick;
	}
	
	/**
	 * @param nick the nickname ot set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * @return the email of the user
	 */
	@Column(length = 100, nullable = false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the password hash of the user
	 */@Column(nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}
	 
	/**
	 * @param passwordHash the password hash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * @return the entries this user has posted
	 */
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("createdAt")
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
