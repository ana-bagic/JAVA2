package hr.fer.oprpp2.hw05.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;

/**
 * Servlet used to display a list of polls.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/index.html")
public class PollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			req.setAttribute("POLLS", DAOProvider.getDao().getPolls());
		} catch(DAOException e) {
			if(e.getCause() == null) {
				ErrorUtil.sendErrorMessage(req, resp, e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}

		req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
	}
}
