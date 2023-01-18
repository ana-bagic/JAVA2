package hr.fer.oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Servlet used to display the results of the voting.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/glasanje-rezultati")
public class ResultsServlet extends HttpServlet {

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
		req.setAttribute("POLLID", id);
		
		try {
			List<PollOption> options = DAOProvider.getDao().getOptions(id);
			options.sort((b1, b2) -> Long.compare(b2.getVotesCount(), b1.getVotesCount()));
			req.setAttribute("OPTIONS", options);
			
			long maxVotes = options.get(0).getVotesCount();
			List<PollOption> winningOptions = new LinkedList<>(options);
			winningOptions.removeIf(o -> o.getVotesCount() != maxVotes);
			
			req.setAttribute("WINOPTIONS", winningOptions);
		} catch (DAOException e) {
			if (e.getCause() == null) {
				ErrorUtil.sendErrorMessage(req, resp, e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/results.jsp").forward(req, resp);
	}
}
