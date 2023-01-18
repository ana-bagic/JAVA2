package hr.fer.zemris.java.webserver;

/**
 * Interface models an object that can process a request.
 * 
 * @author Ana Bagić
 *
 */
public interface IWebWorker {
	
	/**
	 * Method processes a request.
	 * 
	 * @param context {@link RequestContext} used to process request
	 * @throws Exception if error occurs while processing request
	 */
	public void processRequest(RequestContext context) throws Exception;
}
