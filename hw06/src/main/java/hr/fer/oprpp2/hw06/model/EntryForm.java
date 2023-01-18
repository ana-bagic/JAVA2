package hr.fer.oprpp2.hw06.model;

import javax.servlet.http.HttpServletRequest;

/**
 * Class models form for creating/editing entries {@link BlogEntry}.
 * 
 * @author Ana Bagić
 *
 */
public class EntryForm extends Form {

	/** Id of the entry. */
	private String id;
	/** Title of the entry. */
	private String title;
	/** Text of the entry. */
	private String text;
	
	/**
	 * Fills the form parameters based on the parameters received from {@link HttpServletRequest}. 
	 * 
	 * @param req request with the parameters
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		title = prepare(req.getParameter("title"));
		text = prepare(req.getParameter("text"));
	}

	/**
	 * Fills the form parameters based on the parameters from {@link BlogEntry}. 
	 * 
	 * @param e entry with the original parameters
	 */
	public void fillFromBlogEntry(BlogEntry e) {
		id = e.getId().toString();
		title = e.getTitle();
		text = e.getText();
	}

	/**
	 * Fills the given entry with the parameters from this form.
	 * Before calling this method, form should be validates and checked for errors.
	 * 
	 * @param e entry to fill with parameters
	 */
	public void fillBlogEntry(BlogEntry e) {
		e.setTitle(title);
		e.setText(text);
	}

	/**
	 * Method validates form. It checks for the semantic correctness of the stored parameters, and fills the errors map if necessary.
	 */
	public void validate() {
		errors.clear();
		
		if(title.isEmpty()) {
			errors.put("title", "Title is required.");
		}
		
		if(text.isEmpty()) {
			errors.put("text", "Text is required.");
		}
	}
	
	/**
	 * @return the id of the entry
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title of the entry
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the text of the entry
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
