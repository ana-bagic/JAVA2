package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents a {@link String} in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementString extends Element {

	/** Value of the String. */
	private String value;
	
	/**
	 * Constructor creates a new String with a given value.
	 * 
	 * @param value to be set
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * @return value of the String
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return '"' + value + '"';
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementString))
			return false;
		ElementString other = (ElementString) obj;
		
		return this.asText().equals(other.asText());
	}

}
