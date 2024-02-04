package hr.fer.zemris.java.custom.scripting.exec.helper;

/**
 * Class encapsulates a value of any {@link Object}.
 * If object is or can be parsable to {@link Integer} or {@link Double}, arithmetic operations can be executed.
 * 
 * @author Ana Bagić
 *
 */
public class ValueWrapper {

	/** Value of the object. */
	private Object value;
	
	/**
	 * Constructor creates new wrapper containing given value.
	 * 
	 * @param value to encapsulate in wrapper
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * @return value in the wrapper
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value in the wrapper to given.
	 * 
	 * @param value to set in the wrapper
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Adds value to current object.
	 * 
	 * @param incValue value to add
	 * @throws RuntimeException if given or current value is not parsable to {@link Integer} or {@link Double}
	 */
	public void add(Object incValue) {
		ValueConverter converter = new ValueConverter((n1, n2) -> n1 + n2);
		value = converter.execute(value, incValue);
	}
	
	/**
	 * Subtracts value from current object.
	 * 
	 * @param decValue value to subtract
	 * @throws RuntimeException if given or current value is not parsable to {@link Integer} or {@link Double}
	 */
	public void subtract(Object decValue) {
		ValueConverter converter = new ValueConverter((n1, n2) -> n1 - n2);
		value = converter.execute(value, decValue);
	}
	
	/**
	 * Multiplies value with current object.
	 * 
	 * @param mulValue value to multiply
	 * @throws RuntimeException if given or current value is not parsable to {@link Integer} or {@link Double}
	 */
	public void multiply(Object mulValue) {
		ValueConverter converter = new ValueConverter((n1, n2) -> n1 * n2);
		value = converter.execute(value, mulValue);
	}
	
	/**
	 * Divides current object with value.
	 * 
	 * @param divValue value to divide current value with
	 * @throws RuntimeException if given of current value is not parsable to {@link Integer} or {@link Double}
	 * @throws ArithmeticException if divValue is zero
	 */
	public void divide(Object divValue) {
		Operation op = (n1, n2) -> {
			if(n2 == 0.0) {
				throw new ArithmeticException("Dividing by zero.");
			}
			return n1 / n2;
		};
		ValueConverter converter = new ValueConverter(op);
		value = converter.execute(value, divValue);
	}
	
	/**
	 * Compares current value with given. If they are equal, 0 is returned.
	 * If current value is greater then positive value is returned, otherwise negative value is returned.
	 * 
	 * @param withValue value to compare with current value
	 * @return 0 if they are equal, positive value if current value is greater, otherwise negative value
	 */
	public int numCompare(Object withValue) {
		ValueConverter converter = new ValueConverter((n1, n2) -> (double)n1.compareTo(n2));
		return converter.execute(value, withValue).intValue();
	}

}
