package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Class represents SmartScript lexer.
 * 
 * @author Ana Bagić
 *
 */
public class SmartScriptLexer {

	/** Input text. */
	private char[] data;
	/** Current token. */
	private Token token;
	/** Index of the char to process next. */
	private int currIndex;
	/** Lexer state. */
	private LexerState state;
	
	/**
	 * Constructor receives input text to tokenize.
	 * 
	 * @param text input text to tokenize
	 * @throws NullPointerException if text is <code>null</code>
	 */
	public SmartScriptLexer(String text) {
		data = text.toCharArray();
		currIndex = 0;
		state = LexerState.TEXT;
	}
	
	/**
	 * Generates and returns next token.
	 * 
	 * @return generated token
	 * @throws LexerException if an exception in lexer occures
	 */
	public Token nextToken() {
		if(token != null && token.getType() == TokenType.EOF)
			throw new LexerException("No more tokens!");
		
		token = extractNextToken();
		return token;
	}
	
	/**
	 * @return last generated token, doesn't generate a new one
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Sets the lexer state to given.
	 * 
	 * @param state to set the lexer to
	 * @throws LexerException if given state is <code>null</code>
	 */
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state);
	}

	/**
	 * @return new generated token
	 * @throws LexerException if an exception in lexer occures
	 */
	private Token extractNextToken() {
		if(currIndex == data.length)
			return new Token(TokenType.EOF, null);
		
		switch (state) {
		case TEXT: {
			if(currIndex < data.length-1 && data[currIndex] == '{' && data[currIndex+1] == '$') {
				currIndex += 2;
				return new Token(TokenType.OPENTAG, "{$");
			}
				
			return new Token(TokenType.STRING, extractStringText());
		}
		case TAG: {
			skipWhitespace();
			
			char nextChar = data[currIndex];
			
			if(currIndex < data.length-1 && nextChar == '$' && data[currIndex+1] == '}') {
				currIndex += 2;
				return new Token(TokenType.CLOSETAG, "$}");
			}
			
			if(nextChar == '"') {
				currIndex++;
				return new Token(TokenType.STRING, extractStringTag());
			}
			
			if(Character.isDigit(nextChar) ||
					currIndex < data.length-1 && nextChar == '-' && Character.isDigit(data[currIndex+1])) {
				String number = extractNumber();
				
				try {
					int value = Integer.parseInt(number);
					return new Token(TokenType.INTNUMBER, value);
				} catch (Exception e) {
					try {
						double value = Double.parseDouble(number);
						return new Token(TokenType.DOUBLENUMBER, value);
					} catch (Exception e2) {
						throw new LexerException("Error while processing number.");
					}
				}
			}
			
			if(Character.isLetter(nextChar))
				return new Token(TokenType.VARIABLE, extractIdentifier());
			
			if(currIndex < data.length-1 && nextChar == '@' && Character.isLetter(data[currIndex+1])) {
				currIndex++;
				return new Token(TokenType.FUNCTION, extractIdentifier());
			}
			
			if(nextChar == '+' || nextChar == '-' || nextChar == '*' ||
					nextChar == '/' || nextChar == '^' || nextChar == '=') {
				currIndex++;
				return new Token(TokenType.SYMBOL, nextChar);
			}
			
			throw new LexerException("Character " + nextChar + " is not legal on this position: " + currIndex);
		}
		default:
			throw new LexerException("State cannot be null.");
		}
	}
	
	/**
	 * Skips all the whitespace.
	 */
	private void skipWhitespace() {
		while(data.length != currIndex &&
			(data[currIndex] == '\r' || data[currIndex] == '\n'
			|| data[currIndex] == '\t' || data[currIndex] == ' ')) {
			currIndex++;
		}
	}
	
	/**
	 * @return new String from data when lexer is in TEXT state
	 * @throws LexerException if an exception in lexer occures
	 */
	private String extractStringText() {
		StringBuilder sb = new StringBuilder();
		
		while(data.length != currIndex) {			
			if(currIndex < data.length-1 && data[currIndex] == '{' && data[currIndex+1] == '$')
				break;
			
			if(data[currIndex] == '\\') {
				if(currIndex > data.length-2 || data[currIndex+1] != '{' && data[currIndex+1] != '\\')
					throw new LexerException("Error in text at position " + currIndex + ": illegal use of \\");
				
				sb.append(data[currIndex+1]);
				currIndex += 2;
				continue;
			}
			
			sb.append(data[currIndex++]);
		}
		
		return sb.toString();
	}
	
	/**
	 * @return new String from data when lexer is in TAG state
	 * @throws LexerException if an exception in lexer occures
	 */
	private String extractStringTag() {
		StringBuilder sb = new StringBuilder();
		
		while(data.length != currIndex) {
			if(data[currIndex] == '"') {
				currIndex++;
				break;
			}
			
			if(data[currIndex] == '\\') {
				if(currIndex > data.length-2)
					throw new LexerException("Error in text at position " + currIndex + ": illegal use of \\");
				sb.append(switch (data[currIndex+1]) {
					case '\\' -> '\\';
					case '"' -> '"';
					case 'n' -> '\n';
					case 'r' -> '\r';
					case 't' -> '\t';
					default ->
						throw new LexerException("Error in text at position " + currIndex + ": illegal use of \\");
				});
				currIndex += 2;
				continue;
			}
			
			sb.append(data[currIndex++]);
		}
		
		return sb.toString();
	}
	
	/**
	 * @return new String that is parsable in a number
	 */
	private String extractNumber() {
		StringBuilder sb = new StringBuilder();
		boolean decimal = false;
		
		if(data[currIndex] == '-') {
			sb.append('-');
			currIndex++;
		}
		
		while(data.length != currIndex) {
			if(!decimal && data[currIndex] == '.' &&
				currIndex < data.length-1 && Character.isDigit(data[currIndex+1])) {
				sb.append('.');
				currIndex++;
				decimal = true;
				continue;
			}
				
			if(Character.isDigit(data[currIndex])) {
				sb.append(data[currIndex++]);
				continue;
			}
			
			break;
		}
		
		return sb.toString();
	}

	/**
	 * @return new String representing identifier
	 */
	private String extractIdentifier() {
		StringBuilder sb = new StringBuilder();
		
		while(data.length != currIndex &&
				(Character.isLetter(data[currIndex]) || Character.isDigit(data[currIndex])
				|| data[currIndex] == '_')) {
			sb.append(data[currIndex++]);
		}
		
		return sb.toString();
	}

}
