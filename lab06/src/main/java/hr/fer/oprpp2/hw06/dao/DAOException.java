package hr.fer.oprpp2.hw06.dao;

/**
 * Class models {@link RuntimeException} used to notify presentation and service layers about errors in DAO layer.
 * 
 * @author Ana Bagić
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DAOException() {}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param message of the exception
	 * @param cause of the exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with message.
	 * 
	 * @param message of the exception
	 */
	public DAOException(String message) {
		super(message);
	}
}