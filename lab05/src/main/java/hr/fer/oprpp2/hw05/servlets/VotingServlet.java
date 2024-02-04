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
 * Servlet used to display a poll.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/glasanje")
public class VotingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id;
		try {
			id = Long.parseLong(req.getParameter("pollID"));
		} catch (NumberFormatException e) {
			ErrorUtil.sendErrorMessage(req, resp, "Poll id has to be a whole number.");
			return;
		}

		try {
			req.setAttribute("POLL", DAOProvider.getDao().getPoll(id));
			req.setAttribute("OPTIONS", DAOProvider.getDao().getOptions(id));
		} catch (DAOException e) {
			if (e.getCause() == null) {
				ErrorUtil.sendErrorMessage(req, resp, e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}

		req.getRequestDispatcher("/WEB-INF/pages/vote.jsp").forward(req, resp);
	}
}