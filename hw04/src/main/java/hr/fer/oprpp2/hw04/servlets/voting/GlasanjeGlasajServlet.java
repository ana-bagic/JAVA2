package hr.fer.oprpp2.hw04.servlets.voting;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.Band;
import hr.fer.oprpp2.hw04.util.BandsUtil;
import hr.fer.oprpp2.hw04.util.ErrorUtil;

/**
 * Servlet used to store given vote.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, Integer> votes = BandsUtil.getVotes(req);
		
		List<Band> bands = BandsUtil.getBands(req);
		Set<Integer> bandIds = new HashSet<>();
		bands.forEach(b -> bandIds.add(b.getId()));
		
		String idS = req.getParameter("id");
		int id;
		try {
			id = Integer.parseInt(idS);
		} catch (NumberFormatException e) {
			ErrorUtil.sendErrorMessage(req, resp, "Band id has to be an Integer.");
			return;
		}
		
		if(!bandIds.contains(id)) {
			ErrorUtil.sendErrorMessage(req, resp, "Band with id " + id + " does not exist");
			return;
		}
		
		Integer currVotes = votes.get(id);
		if(currVotes == null) {
			votes.put(id, 1);
		} else {
			votes.put(id, currVotes.intValue() + 1);
		}
		
		BandsUtil.writeVotes(req, votes);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

}
