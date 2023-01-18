package hr.fer.zemris.java.webserver;

/**
 * Interface models an object that handles processing of given path.
 * 
 * @author Ana Bagić
 *
 */
public interface IDispatcher {

	/**
	 * Method analyzes the given path and determines how to process it.
	 * 
	 * @param urlPath path to process
	 * @throws Exception if error occurs while processing path
	 */
	void dispatchRequest(String urlPath) throws Exception;

}
