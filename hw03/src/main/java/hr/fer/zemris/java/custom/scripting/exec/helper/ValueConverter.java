package hr.fer.zemris.java.custom.scripting.exec.helper;

/**
 * Class is used as helper class for {@link ValueWrapper}. It is used to convert operands and execute operation.
 * 
 * @author Ana Bagić
 *
 */
public class ValueConverter {

	/** Operation that needs to be executed on values. */
	private Operation operation;
	
	/**
	 * Constructor creates new converter using given operation.
	 * 
	 * @param operation to be set
	 */
	public ValueConverter(Operation operation) {
		this.operation = operation;
	}

	/**
	 * Method converts operand to Integer of Double and executes operation on them.
	 * If both operands are of type {@link Integer} then so will the result be, otherwise it will be {@link Double}.
	 * 
	 * @param n1 first operand
	 * @param n2 second operand
	 * @return result of the operation on operands, it is of type {@link Double} or {@link Integer}
	 */
	public Number execute(Object n1, Object n2) {
		Number x1 = parseObject(n1);
		Number x2 = parseObject(n2);
		
		boolean isInteger = false;
		if(x1 instanceof Integer && x2 instanceof Integer) {
			isInteger = true;
		}
		
		Number result = operation.doOperation(x1.doubleValue(), x2.doubleValue());
		if(isInteger) {
			result = Integer.valueOf(result.intValue());
		}
		
		return result;
	}
	
	/**
	 * Helper method used to determine object's class. If parameter is null, Integer(0) is returned.
	 * If parameter is {@link String} parsable to {@link Double} or {@link Integer}, then that is returned.
	 * If parameter is {@link Double} or {@link Integer}, then that is returned.
	 * Otherwise {@link RuntimeException} is thrown.
	 * 
	 * @param n object to parse
	 * @return object of type {@link Double} or {@link Integer}
	 */
	private Number parseObject(Object n) {
		if(n == null) {
			return Integer.valueOf(0);
		} else if(n instanceof String) {
			try {
				return Integer.valueOf(Integer.parseInt((String) n));
			} catch (NumberFormatException e) {
				try {
					return Double.valueOf(Double.parseDouble((String) n));
				} catch (NumberFormatException e1) {
					throw new RuntimeException("Object: " + n + " is not parsable to Integer or Double.");
				}
			}
		} else if(n instanceof Integer || n instanceof Double) {
			return (Number)n;
		}
		
		throw new RuntimeException("Object has to be of type Integer, Double, String or null for this operation to be executed.");
	}
}
