package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enum models all token types.
 * 
 * @author Ana Bagić
 *
 */
public enum TokenType {
	
	/** End-of-file. */
	EOF,
	
	/** Opening of the tag. */
	OPENTAG,
	
	/** Closing of the tag. */
	CLOSETAG,
	
	/** String. */
	STRING,
	
	/** Integer. */
	INTNUMBER,
	
	/** Double. */
	DOUBLENUMBER,
	
	/** Symbol. */
	SYMBOL,
	
	/** Variable. */
	VARIABLE,
	
	/** Function. */
	FUNCTION
	
}
