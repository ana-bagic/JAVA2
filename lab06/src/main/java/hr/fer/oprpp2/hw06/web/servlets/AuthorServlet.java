package hr.fer.oprpp2.hw06.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw06.dao.DAOProvider;
import hr.fer.oprpp2.hw06.model.BlogComment;
import hr.fer.oprpp2.hw06.model.BlogEntry;
import hr.fer.oprpp2.hw06.model.BlogUser;
import hr.fer.oprpp2.hw06.model.CommentForm;
import hr.fer.oprpp2.hw06.model.EntryForm;

/**
 * Servlet is used for showing the author page, creating and editing entries, showing entry page and posting comments.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String parameters[] = getParameters(req, resp);
		if(parameters == null) return;

		if(parameters.length == 1) {
			showAuthorsPage(req, resp, parameters[0]);
			return;
		}

		if(parameters.length == 2 && parameters[1].equals("new")) {
			showNewEditEntryPage(req, resp, true, parameters[0], null);
			return;
		}

		if(parameters.length == 3 && parameters[1].equals("edit")) {
			showNewEditEntryPage(req, resp, false, parameters[0], parameters[2]);
			return;
		}

		if(parameters.length == 2) {
			req.setAttribute("COMMENT", new CommentForm());
			showEntryPage(req, resp, parameters[0], parameters[1]);
			return;
		}

		ErrorUtil.sendErrorMessage(req, resp, "URL mapping " + req.getRequestURI() + " does not exist for GET method.");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String parameters[] = getParameters(req, resp);
		if(parameters == null) return;

		if(parameters.length == 2 && parameters[1].equals("new")) {
			saveEntry(req, resp, true, parameters[0], null);
			return;
		}
		
		if(parameters.length == 3 && parameters[1].equals("edit")) {
			saveEntry(req, resp, false, parameters[0], parameters[2]);
			return;
		}

		if(parameters.length == 2) {
			saveComment(req, resp, parameters[0], parameters[1]);
			return;
		}

		ErrorUtil.sendErrorMessage(req, resp, "URL mapping " + req.getRequestURI() + " does not exist for POST method.");
	}

	/**
	 * Method handles the request for author page.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param nick nickname extracted from URL
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private void showAuthorsPage(HttpServletRequest req, HttpServletResponse resp, String nick) throws ServletException, IOException {
		BlogUser user = DAOProvider.getDAO().getUser(nick);
		if(user == null) {
			ErrorUtil.sendErrorMessage(req, resp, "Author with the given nickname " + nick + " is not found.");
			return;
		}

		req.setAttribute("NICK", nick);
		req.setAttribute("ENTRIES", user.getEntries());
		req.getRequestDispatcher("/WEB-INF/pages/authorPage.jsp").forward(req, resp);
	}

	/**
	 * Method handles the request for showing create new entry/edit entry page.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param isNew whether this is request for creating a new entry or editing the old one
	 * @param nick nickname extracted from URL
	 * @param eId if request is for editing existing entry, this is entry id
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private void showNewEditEntryPage(HttpServletRequest req, HttpServletResponse resp, boolean isNew, String nick, String eId) throws IOException, ServletException {
		if(isNew) {
			req.setAttribute("ENTRY", new EntryForm());
		} else {
			BlogEntry entry = getBlogEntry(req, resp, nick, eId);
			if(entry == null) return;

			EntryForm form = new EntryForm();
			form.fillFromBlogEntry(entry);
			req.setAttribute("ENTRY", form);
		}

		req.setAttribute("NICK", nick);
		req.setAttribute("NEW", isNew);
		req.getRequestDispatcher("/WEB-INF/pages/newEditEntry.jsp").forward(req, resp);
	}

	/**
	 * Method handles the request for entry page.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param nick nickname extracted from URL
	 * @param eId id of the entry
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private void showEntryPage(HttpServletRequest req, HttpServletResponse resp, String nick, String eId) throws IOException, ServletException {
		BlogEntry entry = getBlogEntry(req, resp, nick, eId);
		if(entry == null) return;

		req.setAttribute("ENTRY", entry);
		req.setAttribute("NICK", nick);
		req.getRequestDispatcher("/WEB-INF/pages/entryPage.jsp").forward(req, resp);
	}

	/**
	 * Method handles the request for saving new entry/editing exisiting entry.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param isNew whether this is request for creating a new entry or editing the existing one
	 * @param nick nickname extracted from URL
	 * @param eId if request is for editing existing entry, this is entry id
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private void saveEntry(HttpServletRequest req, HttpServletResponse resp, boolean isNew, String nick, String eId) throws ServletException, IOException {
		EntryForm form = new EntryForm();
		form.fillFromHttpRequest(req);
		form.validate();

		if(form.hasErrors()) {
			req.setAttribute("ENTRY", form);
			req.setAttribute("NICK", nick);
			req.setAttribute("NEW", isNew);
			req.getRequestDispatcher("/WEB-INF/pages/newEditEntry.jsp").forward(req, resp);
			return;
		}

		BlogEntry entry;
		if(isNew) {
			entry = new BlogEntry();
			form.fillBlogEntry(entry);
			entry.setCreator(DAOProvider.getDAO().getUser(nick));
			entry.setCreatedAt(new Date());
			entry.setLastModifiedAt(new Date());
			DAOProvider.getDAO().save(entry);
		} else {
			entry = getBlogEntry(req, resp, nick, eId);
			if(entry == null) return;

			form.fillBlogEntry(entry);
			entry.setLastModifiedAt(new Date());
			DAOProvider.getDAO().update(entry);
		}

		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/author/" + nick + "/" + entry.getId()));
	}

	/**
	 * Method handles the request for saving new comment.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param nick nickname extracted from URL
	 * @param eId id of the entry the comment is for
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private void saveComment(HttpServletRequest req, HttpServletResponse resp, String nick, String eId) throws IOException, ServletException {
		CommentForm form = new CommentForm();
		form.fillFromHttpRequest(req);
		form.validate();

		if(form.hasErrors()) {
			req.setAttribute("COMMENT", form);
			showEntryPage(req, resp, nick, eId);
			return;
		}
		
		BlogEntry entry = getBlogEntry(req, resp, nick, eId);
		if(entry == null) return;

		BlogComment comment = new BlogComment();
		form.fillBlogComment(comment);
		comment.setPostedOn(new Date());
		comment.setBlogEntry(entry);
		DAOProvider.getDAO().save(comment);
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/author/" + nick + "/" + eId));
	}

	/**
	 * Returns the entry with the given id, or shows error to user if one does not exist.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @param nick nickname extracted from URL
	 * @param eId id of the entry to get
	 * @return retreived entry with the given id, or <code>null</code> if there was an error in request
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private BlogEntry getBlogEntry(HttpServletRequest req, HttpServletResponse resp, String nick, String eId) throws IOException, ServletException {
		try {
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(eId));

			if(entry == null) {
				ErrorUtil.sendErrorMessage(req, resp, "Entry with the id " + eId + " does not exist.");
				return null;
			}

			if(!entry.getCreator().getNick().equals(nick)) {
				ErrorUtil.sendErrorMessage(req, resp, "Entry with given id is not by author " + nick + ".");
				return null;
			}

			return entry;
		} catch (NumberFormatException e) {
			ErrorUtil.sendErrorMessage(req, resp, "Entry id " + eId + " is not parsable to a number.");
			return null;
		}
	}

	/**
	 * Returns an array of parameters extracted from the URL, or shows an error to user if URL was not in correct format.
	 * 
	 * @param req client request
	 * @param resp client response
	 * @return an array of parameters extracted from the URL, or <code>null</code> if URL was not in correct format
	 * @throws ServletException if an error occurs
	 * @throws IOException if an error occurs
	 */
	private String[] getParameters(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String pathInfo = req.getPathInfo();
		if(pathInfo != null && pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}

		if(pathInfo == null || pathInfo.split("/").length == 0) {
			ErrorUtil.sendErrorMessage(req, resp, "Author is not given.");
			return null;
		}

		return pathInfo.split("/");
	}

}
