package hr.fer.oprpp2.hw05.dao;

/**
 * Class models {@link RuntimeException} used to notify presentation and service layers about errors in DAO layer.
 * 
 * @author Ana Bagić
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default contructor.
	 */
	public DAOException() {}

	/**
	 * Constructor with message, cause, enableSuppression and writeableStackTrace.
	 * 
	 * @param message of the exception
	 * @param cause of the exception
	 * @param enableSuppression true or false
	 * @param writableStackTrace true or false
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Contructor with message and cause.
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

	/**
	 * Constructor with cause.
	 * 
	 * @param cause of the exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}