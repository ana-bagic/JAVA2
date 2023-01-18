package hr.fer.oprpp2.hw06.model;

import javax.servlet.http.HttpServletRequest;

/**
 * Class models form for creating comments {@link BlogComment}.
 * 
 * @author Ana Bagić
 *
 */
public class CommentForm extends Form {

	/** Email of the comment poster. */
	private String usersEMail;
	/** Message of the comment. */
	private String message;
	
	/**
	 * Fills the form parameters based on the parameters received from {@link HttpServletRequest}. 
	 * 
	 * @param req request with the parameters
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		usersEMail = prepare(req.getParameter("usersEMail"));
		message = prepare(req.getParameter("message"));
	}

	/**
	 * Fills the form parameters based on the parameters from {@link BlogComment}. 
	 * 
	 * @param c comment with the original parameters
	 */
	public void fillFromBlogComment(BlogComment c) {
		usersEMail = c.getUsersEMail();
		message = c.getMessage();
	}

	/**
	 * Fills the given comment with the parameters from this form.
	 * Before calling this method, form should be validates and checked for errors.
	 * 
	 * @param c comment to fill with parameters
	 */
	public void fillBlogComment(BlogComment c) {
		c.setUsersEMail(usersEMail);
		c.setMessage(message);
	}

	/**
	 * Method validates form. It checks for the semantic correctness of the stored parameters, and fills the errors map if necessary.
	 */
	public void validate() {
		errors.clear();
		
		if(usersEMail.isEmpty()) {
			errors.put("usersEMail", "Email is required.");
		} else {
			int l = usersEMail.length();
			int p = usersEMail.indexOf('@');
			if(l < 3 || p < 1 || p == l-1) {
				errors.put("usersEMail", "Email is not in the right format.");
			}
		}
		
		if(message.isEmpty()) {
			errors.put("message", "Message is required.");
		}
	}

	/**
	 * @return the email of the comment poster
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * @param usersEMail the user email to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * @return the message pf the comment
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
