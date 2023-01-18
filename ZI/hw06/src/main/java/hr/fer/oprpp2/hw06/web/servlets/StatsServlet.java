package hr.fer.oprpp2.hw06.web.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.oprpp2.hw06.dao.DAOProvider;
import hr.fer.oprpp2.hw06.model.BlogUser;
import hr.fer.oprpp2.hw06.model.StatsDTO;

@WebServlet(urlPatterns = "/servleti/stats")
public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> users = DAOProvider.getDAO().getAllUsers();
		List<StatsDTO> stats = new LinkedList<>();
		for(BlogUser u : users) {
			stats.add(new StatsDTO(u.getNick(), u.getEntries().size()));
		}
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(stats.toArray());
		
		resp.getWriter().write(jsonText);
		resp.getWriter().flush();
	}
}