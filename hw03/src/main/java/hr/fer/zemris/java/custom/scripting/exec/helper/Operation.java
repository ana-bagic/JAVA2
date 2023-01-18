package hr.fer.zemris.java.custom.scripting.exec.helper;

/**
 * Interface models operations for Strategy design pattern used in {@link ValueWrapper} class.
 * 
 * @author Ana Bagić
 *
 */
public interface Operation {
	
	/**
	 * Operation executed on given operands.
	 * 
	 * @param n1 first operand
	 * @param n2 second operand
	 * @return result of the operation
	 */
	public Double doOperation(Double n1, Double n2);
	

}
