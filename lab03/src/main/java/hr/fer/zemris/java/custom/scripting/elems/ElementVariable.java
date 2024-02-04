package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents a variable in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementVariable extends Element {

	/** Name of the variable. */
	private String name;
	
	/**
	 * Constructor creates a new variable with a given name.
	 * 
	 * @param name of the variable
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * @return name of the variable
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementVariable))
			return false;
		ElementVariable other = (ElementVariable) obj;
		
		return this.asText().equals(other.asText());
	}

}
