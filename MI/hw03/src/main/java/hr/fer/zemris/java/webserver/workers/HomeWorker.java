package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class processes index2.html request.
 * 
 * @author Ana Bagić
 *
 */
public class HomeWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgColor = context.getPersistentParameter("bgcolor");
		context.setTemporaryParameter("background", bgColor != null ? bgColor : "7F7F7F");
		
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
