package hr.fer.oprpp2.hw06.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw06.dao.DAOProvider;
import hr.fer.oprpp2.hw06.model.BlogUser;
import hr.fer.oprpp2.hw06.model.UserForm;

/**
 * Servlet shows main page to user and handles the login process.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		UserForm form = new UserForm();
		form.fillFromBlogUser(new BlogUser());
		req.setAttribute("USER", form);
		
		findAuthorsAndForward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		UserForm form = new UserForm();
		form.fillFromHttpRequest(req);
		
		BlogUser user = DAOProvider.getDAO().getUser(form.getNick());
		if(user != null && UserForm.getHashed(form.getPassword()).equals(user.getPasswordHash())) {
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.firstName", user.getFirstName());
			req.getSession().setAttribute("current.user.lastName", user.getLastName());
			req.getSession().setAttribute("current.user.nick", user.getNick());
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
			return;
		}
		
		req.setAttribute("ERROR", "Nickname or password incorrect.");
		form.setPassword("");
		req.setAttribute("USER", form);
		findAuthorsAndForward(req, resp);
	}
	
	private void findAuthorsAndForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> authors = DAOProvider.getDAO().getAllUsers();
		req.setAttribute("AUTHORS", authors);
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
