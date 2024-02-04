package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents a constant {@link Integer} in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementConstantInteger extends Element {

	/** Value of the {@link Integer}. */
	private Integer value;
	
	/**
	 * Constructor creates a new constant {@link Integer} with a given value.
	 * 
	 * @param value to be set
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * @return value of the constant {@link Integer}
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementConstantInteger))
			return false;
		ElementConstantInteger other = (ElementConstantInteger) obj;
		
		return this.asText().equals(other.asText());
	}

}
