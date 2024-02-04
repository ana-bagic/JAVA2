package hr.fer.oprpp2.hw06.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

/**
 * Class models form for creating users {@link BlogUser}.
 * 
 * @author Ana Bagić
 *
 */
public class UserForm extends Form {

	/** First name of the user. */
	private String firstName;
	/** Last name of the user. */
	private String lastName;
	/** Nickname of the user (username). */
	private String nick;
	/** Email of the user. */
	private String email;
	/** Password of the user. */
	private String password;
	
	/**
	 * Fills the form parameters based on the parameters received from {@link HttpServletRequest}. 
	 * 
	 * @param req request with the parameters
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		firstName = prepare(req.getParameter("firstName"));
		lastName = prepare(req.getParameter("lastName"));
		email = prepare(req.getParameter("email"));
		nick = prepare(req.getParameter("nick"));
		password = prepare(req.getParameter("password"));
	}

	/**
	 * Fills the form parameters based on the parameters from {@link BlogUser}. 
	 * 
	 * @param u object with the original parameters
	 */
	public void fillFromBlogUser(BlogUser u) {		
		firstName = prepare(u.getFirstName());
		lastName = prepare(u.getLastName());
		email = prepare(u.getEmail());
		nick = prepare(u.getNick());
		password = "";
	}

	/**
	 * Fills the given user with the parameters from this form.
	 * Before calling this method, form should be validates and checked for errors.
	 * 
	 * @param u user to fill with parameters
	 */
	public void fillBlogUser(BlogUser u) {
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setEmail(email);
		u.setNick(nick);
		u.setPasswordHash(getHashed(password));
	}

	/**
	 * Method validates form. It checks for the semantic correctness of the stored parameters, and fills the errors map if necessary.
	 */
	public void validate() {
		errors.clear();
		
		if(firstName.isEmpty()) {
			errors.put("firstName", "First name is required.");
		}
		
		if(lastName.isEmpty()) {
			errors.put("lastName", "Last name is required.");
		}
		
		if(email.isEmpty()) {
			errors.put("email", "Email is required.");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l < 3 || p < 1 || p == l-1) {
				errors.put("email", "Email is not in the right format.");
			}
		}
		
		if(nick.isEmpty()) {
			errors.put("nick", "Nickname is required.");
		}
		
		if(password.isEmpty()) {
			errors.put("password", "Password is required.");
		}
	}
	
	/**
	 * Helper method that returns hashed password from the given password
	 * 
	 * @param password to hash
	 * @return hashed password
	 */
	public static String getHashed(String password) {		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA256");
			md.update(password.getBytes());			
			return new String(md.digest(), StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @return the first name of the user
	 */
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
	 * @return the nickname of the user
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nickname to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the email of the user
	 */
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
	 * @return the password of the user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
