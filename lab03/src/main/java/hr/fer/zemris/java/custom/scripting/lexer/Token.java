package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Class represent a token created with lexer.
 * 
 * @author Ana Bagić
 *
 */
public class Token {
	
	/** Type of the token. */
	private TokenType type;
	/** Value of the token. */
	private Object value;

	/**
	 * Constructor creates a new token using given type and value.
	 * 
	 * @param type of the token
	 * @param value of the token
	 * @throws NullPointerException if type is <code>null</code>
	 */
	public Token(TokenType type, Object value) {
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}
	
	/**
	 * @return type of the token
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @return value of the token
	 */
	public Object getValue() {
		return value;
	}
	
}

