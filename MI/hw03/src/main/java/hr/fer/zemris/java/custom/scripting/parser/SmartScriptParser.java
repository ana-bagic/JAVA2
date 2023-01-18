package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.IfEvenNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Class represents a SmartScript parser.
 * 
 * @author Ana Bagić
 *
 */
public class SmartScriptParser {

	/** Lexer used to parse. */
	private SmartScriptLexer lexer;
	/** Stack used to parse. */
	private ObjectStack stack;
	/** Root document node. */
	private DocumentNode doc;
	
	/**
	 * Constructor parses given text.
	 * 
	 * @param body text to parse
	 * @throws SmartScriptParserException if an exception occures while parsing
	 */
	public SmartScriptParser(String body) {
		lexer = new SmartScriptLexer(body);
		stack = new ObjectStack();
		doc = new DocumentNode();
		
		try {
			parse();
		} catch(Exception e) {
			e.printStackTrace();
			throw new SmartScriptParserException(e.getMessage());
		}
	}

	/**
	 * @return root document node
	 */
	public DocumentNode getDocumentNode() {
		return doc;
	}
	
	/**
	 * Parses using lexer.
	 * 
	 * @throws SmartScriptParserException if an exception occures while parsing
	 */
	private void parse() {
		stack.push(doc);
		Token token = lexer.nextToken();
		
		while(token.getType() != TokenType.EOF) {
			switch (token.getType()) {
				case OPENTAG -> {
					lexer.setState(LexerState.TAG);
					solveInsideTag();
				}
				case STRING -> {
					TextNode textNode = new TextNode(token.getValue().toString());
					((Node) stack.peek()).addChildNode(textNode);
				}
				default ->
					throw new SmartScriptParserException(token.getType() + " is not valid at this place.");
			}
			
			token = lexer.nextToken();
		}
		
		if(stack.size() != 1 || !(stack.pop() instanceof DocumentNode))
			throw new SmartScriptParserException("Given text is not in valid format.");
	}

	/**
	 * Parses inside tag.
	 * 
	 * @throws SmartScriptParserException if an exception occures while parsing
	 */
	private void solveInsideTag() {
		Token nextToken = lexer.nextToken();
		
		if(nextToken.getType() == TokenType.SYMBOL && (Character) nextToken.getValue() == '=') {
			solveInsideEqualsTag();
			return;
		}
		
		if(nextToken.getType() == TokenType.VARIABLE) {
			switch (nextToken.getValue().toString().toUpperCase()) {
				case "FOR" -> {
					ForLoopNode forNode = solveInsideForTag();
					((Node) stack.peek()).addChildNode(forNode);
					stack.push(forNode);
					return;
				}
				case "END" -> {
					if(lexer.nextToken().getType() == TokenType.CLOSETAG) {
						lexer.setState(LexerState.TEXT);
						stack.pop();
						return;
					}
				}
				case "IFEVEN" -> {
					IfEvenNode node = solveInsideIfEvenTag();
					((Node) stack.peek()).addChildNode(node);
					stack.push(node);
					return;
				}
			}
		}
		
		throw new SmartScriptParserException("Wrong tag name: " + nextToken.getValue());
	}

	private IfEvenNode solveInsideIfEvenTag() {
		ElementVariable variable;
		Token token = lexer.nextToken();
		
		if(token.getType() == TokenType.VARIABLE)
			variable = new ElementVariable(token.getValue().toString());
		else
			throw new SmartScriptParserException("First element of the IFEVEN tag has to be a variable.");
		
		token = lexer.nextToken();
		
		if(token.getType() == TokenType.CLOSETAG) {
			lexer.setState(LexerState.TEXT);
		} else {
			throw new SmartScriptParserException("Too many arguments in IFEVEN tag, should be only one.");
		}
		
		return new IfEvenNode(variable);
	}

	/**
	 * Parses inside FOR tag.
	 * 
	 * @return new parsed {@link ForLoopNode}
	 * @throws SmartScriptParserException if an exception occures while parsing
	 */
	private ForLoopNode solveInsideForTag() {
		List<Element> elems = new ArrayList<>();
		Token token = lexer.nextToken();
		ElementVariable variable;
		
		if(token.getType() == TokenType.VARIABLE)
			variable = new ElementVariable(token.getValue().toString());
		else
			throw new SmartScriptParserException("First element of the FOR tag has to be a variable.");
		
		for(int counter = 0; counter < 4; counter++) {
			token = lexer.nextToken();
			
			if(token.getType() == TokenType.CLOSETAG) {
				lexer.setState(LexerState.TEXT);
				break;
			}
			
			switch (token.getType()) {
				case STRING ->
					elems.add(new ElementString(token.getValue().toString()));
				case DOUBLENUMBER ->
					elems.add(new ElementConstantDouble((Double) token.getValue()));
				case INTNUMBER ->
					elems.add(new ElementConstantInteger((Integer) token.getValue()));
				case VARIABLE ->
					elems.add(new ElementVariable(token.getValue().toString()));
				default ->
					throw new SmartScriptParserException("Illegal token type in FOR tag: " + token.getType());
			}
		}
		
		if(elems.size() == 2 || elems.size() == 3) {
			return new ForLoopNode(variable, (Element)elems.get(0), (Element)elems.get(1), elems.size() == 2 ? null : (Element)elems.get(2));
		} else {
			throw new SmartScriptParserException("Number of elements in FOR tag is not valid: "
					+ elems.size() + ". Has to be 3 or 4.");
		}
	}

	/**
	 * Parses inside = tag.
	 * 
	 * @throws SmartScriptParserException if an exception occures while parsing
	 */
	private void solveInsideEqualsTag() {
		List<Element> elements = new ArrayList<>();
		Token token;
		
		while(true) {
			token = lexer.nextToken();
			
			if(token.getType() == TokenType.CLOSETAG) {
				lexer.setState(LexerState.TEXT);
				break;
			}
			
			switch (token.getType()) {
				case STRING ->
					elements.add(new ElementString(token.getValue().toString()));
				case DOUBLENUMBER ->
					elements.add(new ElementConstantDouble((Double) token.getValue()));
				case INTNUMBER ->
					elements.add(new ElementConstantInteger((Integer) token.getValue()));
				case FUNCTION ->
					elements.add(new ElementFunction(token.getValue().toString()));
				case SYMBOL -> {
					if((Character) token.getValue() != '=')
						elements.add(new ElementOperator(token.getValue().toString()));
					else
						throw new SmartScriptParserException("Operator cannot be =.");
				}
				case VARIABLE ->
					elements.add(new ElementVariable(token.getValue().toString()));
				default ->
					throw new SmartScriptParserException("Illegal token type in FOR tag: " + token.getType());
			}
			
		}
		
		Element[] elems = elements.toArray(new Element[elements.size()]);
		((Node) stack.peek()).addChildNode(new EchoNode(elems));
	}

}