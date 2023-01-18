package hr.fer.oprpp2.hw05.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Servlet used to store the given vote.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/glasanje-glasaj")
public class VoteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id;
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			ErrorUtil.sendErrorMessage(req, resp, "Option id has to be a whole number.");
			return;
		}
		
		PollOption option;
		try {
			option = DAOProvider.getDao().getOption(id);
			option.setVotesCount(option.getVotesCount() + 1);
			DAOProvider.getDao().updateOption(option);
		} catch(DAOException e) {
			if(e.getCause() == null) {
				ErrorUtil.sendErrorMessage(req, resp, e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + option.getPollId()));
	}

}
