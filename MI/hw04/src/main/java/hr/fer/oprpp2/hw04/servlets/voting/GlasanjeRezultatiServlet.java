package hr.fer.oprpp2.hw04.servlets.voting;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.Band;
import hr.fer.oprpp2.hw04.util.BandsUtil;

/**
 * Servlet used to display results of voting.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Band> bands = BandsUtil.getBands(req);
		Map<Integer, Integer> votes = BandsUtil.getVotes(req);
		
		bands.forEach(b -> b.setVotes(votes.get(b.getId())));
		bands.sort((b1, b2) -> b2.getVotes().compareTo(b1.getVotes()));
		
		List<Band> winningBands = new LinkedList<>();
		winningBands.add(bands.get(0));
		int maxVotes = bands.get(0).getVotes();
		int counter = 1;
		while(counter < bands.size()) {
			if(!bands.get(counter).getVotes().equals(maxVotes)) {
				break;
			}
			winningBands.add(bands.get(counter));
			counter++;
		}
		
		req.setAttribute("BANDS", bands);
		req.setAttribute("WINBANDS", winningBands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
