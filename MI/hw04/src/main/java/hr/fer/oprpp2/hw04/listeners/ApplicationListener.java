package hr.fer.oprpp2.hw04.listeners;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener of the application.
 * 
 * @author Ana Bagić
 *
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String fileName = sce.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		try (OutputStream os = new FileOutputStream(fileName)) {
			os.write(("").getBytes());
		} catch (IOException e) {}
		
		sce.getServletContext().setAttribute("start", System.currentTimeMillis());
		sce.getServletContext().log("Aplikacija je inicijalizirana.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("start");
		sce.getServletContext().log("Aplikacija završava s radom.");
	}
	
}