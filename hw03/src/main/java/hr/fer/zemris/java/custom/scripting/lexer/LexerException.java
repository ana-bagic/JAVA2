package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class represents unchecked exception that happens while using {@link SmartScriptLexer}.
 * 
 * @author Ana Bagić
 *
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor with message "Error in SmartScriptLexer.".
	 */
	public LexerException() {
		super("Error in SmartScripLexer.");
	}
	
	/**
	 * Constructor with given message.
	 * 
	 * @param message in the exception
	 */
	public LexerException(String message) {
		super(message);
	}

}
