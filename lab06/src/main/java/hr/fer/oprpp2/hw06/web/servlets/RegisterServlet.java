package hr.fer.oprpp2.hw06.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw06.dao.DAOProvider;
import hr.fer.oprpp2.hw06.model.BlogUser;
import hr.fer.oprpp2.hw06.model.UserForm;

/**
 * Servlet handles the registration process.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("current.user.id") != null) {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
			return;
		}
		
		UserForm form = new UserForm();
		form.fillFromBlogUser(new BlogUser());
		req.setAttribute("USER", form);

		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("current.user.id") != null) {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
			return;
		}
		
		UserForm form = new UserForm();
		req.setCharacterEncoding("UTF-8");
		form.fillFromHttpRequest(req);
		form.validate();

		if(form.hasErrors()) {
			req.setAttribute("USER", form);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}

		if(DAOProvider.getDAO().getUser(form.getNick()) != null) {
			form.putError("nick", "This nickame is already taken.");
			form.setNick("");
			req.setAttribute("USER", form);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = new BlogUser();
		form.fillBlogUser(user);
		DAOProvider.getDAO().save(user);

		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.firstName", user.getFirstName());
		req.getSession().setAttribute("current.user.lastName", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
	}
}
