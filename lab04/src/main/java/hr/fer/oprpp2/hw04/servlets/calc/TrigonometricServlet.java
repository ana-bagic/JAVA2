package hr.fer.oprpp2.hw04.servlets.calc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to display page with values of sin and cos functions.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/trigonometric")
public class TrigonometricServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aS = req.getParameter("a");
		String bS = req.getParameter("b");
		
		int a, b;
		try {
			a = Integer.parseInt(aS);
		} catch (NumberFormatException e) {
			a = 0;
		}
		
		try {
			b = Integer.parseInt(bS);
		} catch (NumberFormatException e) {
			b = 360;
		}
		
		if(a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		if(b > a + 720) {
			b = a + 720;
		}
		
		req.setAttribute("a", a);
		req.setAttribute("b", b);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
}
