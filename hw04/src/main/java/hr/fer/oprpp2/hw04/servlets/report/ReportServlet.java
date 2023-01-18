package hr.fer.oprpp2.hw04.servlets.report;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to display page with report of OS usage.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/report")
public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/report.jsp").forward(req, resp);
	}
}