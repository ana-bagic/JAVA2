package hr.fer.oprpp2.hw06.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that models form for creating/editing entities in the database. It provides methods for error management.
 * 
 * @author Ana Bagić
 *
 */
public abstract class Form {

	/** Error map that maps property name to the error message. */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Returns the message of the error for the given property.
	 * 
	 * @param name of the propery for wanted error
	 * @return message of the error, or <code>null</code> if the error with the given propery name does not exist
	 */
	public String getError(String name) {
		return errors.get(name);
	}
	
	/**
	 * Checks if there are any errors.
	 * 
	 * @return <code>true</code> if errors exist, otherwise <code>false</code>
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Checks if the error with the given property name exists.
	 * 
	 * @param name of the property to check for errors
	 * @return <code>true</code> if the error exists, otherwise <code>false</code>
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}
	
	/**
	 * Puts the given error for the given property name.
	 * 
	 * @param name of the property to put error for
	 * @param value message of the error
	 */
	public void putError(String name, String value) {
		errors.put(name, value);
	}
	
	/**
	 * Helper method that converts <code>null</code> strings into empty strings and trims strings.
	 * 
	 * @param value string to check and convert
	 * @return trimmed given string, or empty string if the given string is <code>null</code>
	 */
	protected String prepare(String value) {
		if(value == null) return "";
		return value.trim();
	}
}
