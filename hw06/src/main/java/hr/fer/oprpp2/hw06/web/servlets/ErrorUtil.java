package hr.fer.oprpp2.hw06.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class used to manage error messages.
 * 
 * @author Ana Bagić
 *
 */
public class ErrorUtil {

	/**
	 * Method that sends error message to client.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param message message to display
	 */
	public static void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message)
			throws IOException, ServletException {
		req.setAttribute("MSG", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}