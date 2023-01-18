package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents a function in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementFunction extends Element {

	/** Name of the function. */
	private String name;
	
	/**
	 * Constructor creates a new function with a given name.
	 * 
	 * @param name to be given
	 */
	public ElementFunction(String name) {
		this.name = name;
	}
	
	/**
	 * @return name of the function
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return '@' + name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementFunction))
			return false;
		ElementFunction other = (ElementFunction) obj;
		
		return this.asText().equals(other.asText());
	}

}

