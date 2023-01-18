package hr.fer.oprpp2.hw04.servlets.voting;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.Band;
import hr.fer.oprpp2.hw04.util.BandsUtil;

@WebServlet(urlPatterns = "/glasanje-rezultati2")
public class GlasanjeRezultati2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Comparator<Band> byName = (b1, b2) -> b1.getName().compareTo(b2.getName());
		Comparator<Band> byVotes = (b1, b2) -> b1.getVotes().compareTo(b2.getVotes());
		
		List<Band> bands = BandsUtil.getBands(req);
		Map<Integer, Integer> votes = BandsUtil.getVotes(req);
		
		bands.forEach(b -> b.setVotes(votes.get(b.getId())));
		
		String sorting = req.getParameter("sort");
		
		if(sorting == null) {
			sorting = "voteDesc";
		}
		
		switch (sorting) {
		case "abcAsc" -> bands.sort(byName);
		case "abcDesc" -> bands.sort(byName.reversed());
		case "voteAsc" -> bands.sort(byVotes);
		case "voteDesc" -> bands.sort(byVotes.reversed());
		default -> bands.sort(byVotes.reversed());
		}
		
		req.setAttribute("BANDS", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez2.jsp").forward(req, resp);
	}
}
