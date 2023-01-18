package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class processes EchoParams request.
 * 
 * @author Ana Bagić
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		try {
			context.write("<html><body>");
			context.write("<table><thead><tr><th>Name</th><th>Value</th></tr></thead>");
			context.write("<tbody>");
			for(String name : context.getParameterNames()) {
				context.write("<tr><td>" + name + "</td><td>" + context.getParameter(name) + "</td></tr>");
			}
			
			context.write("</tbody></table></body></html>");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

}
