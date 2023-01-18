package hr.fer.oprpp2.hw04.servlets.color;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet used to set background color to chosen.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/setColor/*")
public class ColorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		String color = req.getPathInfo();
		if(color != null && color.startsWith("/")) {
			color = color.substring(1);
			session.setAttribute("pickedBgCol", color);
		}
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath()));
	}
}
