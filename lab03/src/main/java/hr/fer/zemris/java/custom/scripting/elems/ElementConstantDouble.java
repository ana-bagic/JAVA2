package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class represents a constant {@link Double} in an expression.
 * 
 * @author Ana Bagić
 *
 */
public class ElementConstantDouble extends Element {

	/** Value of the {@link Double}. */
	private Double value;

	/**
	 * Constructor creates a new constant {@link Double} with a given value.
	 * 
	 * @param value to be set
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * @return value of the constant {@link Double}
	 */
	public double getValue() {
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
		if (!(obj instanceof ElementConstantDouble))
			return false;
		ElementConstantDouble other = (ElementConstantDouble) obj;
		
		return this.asText().equals(other.asText());
	}

}
