package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class processes setbgcolor request.
 * 
 * @author Ana Bagić
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		
		context.write("<html><body>");
		context.write("<a href=\"/index2.html\">index2.html</a></br>");
		
		if(color != null && color.length() == 6 && color.matches("-?[0-9a-fA-F]+")) {
			context.setPersistentParameter("bgcolor", color);
			context.write("Color is successfully updated.");
		} else {
			context.write("Color is not updated.");
		}
		
		context.write("</body></html>");
	}

}
