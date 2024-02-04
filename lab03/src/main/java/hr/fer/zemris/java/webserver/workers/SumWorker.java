package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class processes calc request.
 * 
 * @author Ana Bagić
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Integer a, b;
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch (NullPointerException | NumberFormatException e) {
			a = 1;
		}
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch (NullPointerException | NumberFormatException e) {
			b = 2;
		}
		
		Integer sum = a + b;
		context.setTemporaryParameter("zbroj", sum.toString());
		context.setTemporaryParameter("varA", a.toString());
		context.setTemporaryParameter("varB", b.toString());
		context.setTemporaryParameter("imgName", sum%2 == 0 ? "/images/pic1.jpg" : "/images/pic2.jpg");
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}

}
