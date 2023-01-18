package hr.fer.oprpp2.hw04.servlets.appinfo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to display application info.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/appInfo")
public class AppInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final long SEC_C = 1000;
		final long MIN_C = SEC_C*60;
		final long HOURS_C = MIN_C*60;
		final long DAYS_C = HOURS_C*24;
		
		long before = (long) getServletContext().getAttribute("start");
		long now = System.currentTimeMillis();
		long time = now - before;
		
		StringBuilder sb = new StringBuilder();
		long days = time/DAYS_C;
		sb.append(days + " days ");
		time -= days*DAYS_C;
		
		long hours = time/HOURS_C;
		sb.append(hours + " hours ");
		time -= hours*HOURS_C;
		
		long mins = time/MIN_C;
		sb.append(mins + " minutes ");
		time -= mins*MIN_C;
		
		long secs = time/SEC_C;
		sb.append(secs + " seconds and ");
		time -= secs*SEC_C;
		
		sb.append(time + " miliseconds");
		
		req.setAttribute("TIME", sb.toString());
		req.getRequestDispatcher("/WEB-INF/pages/appinfo.jsp").forward(req, resp);
	}
}
