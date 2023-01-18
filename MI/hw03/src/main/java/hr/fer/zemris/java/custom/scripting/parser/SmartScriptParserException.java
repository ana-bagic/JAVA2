package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class represents unchecked exception that happens while using {@link SmartScriptParser}.
 * 
 * @author Ana Bagić
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor with message "Error in SmartScriptParser.".
	 */
	public SmartScriptParserException() {
		super("Error in SmartScriptParser.");
	}
	
	/**
	 * Constructor with given message.
	 * 
	 * @param message in the exception
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

}