package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents an operator in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementOperator extends Element {

	/** Symbol of the operator. */
	private String symbol;
	
	/**
	 * Konstruktor postavlja simbol operatora.
	 * Constructor creates a new operator with a given symbol.
	 * 
	 * @param symbol to be given to an operator
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * @return symbol of the operator
	 */
	public String getName() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementOperator))
			return false;
		ElementOperator other = (ElementOperator) obj;
		
		return this.asText().equals(other.asText());
	}

}
