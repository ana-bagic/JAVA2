package hr.fer.oprpp2.hw06.web.init;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.oprpp2.hw06.dao.jpa.JPAEMFProvider;

/**
 * Listener for the servlet context lifecycle.
 * It takes care of the opening and the closing of the entity manager factory.
 * 
 * @author Ana Bagić
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		JPAEMFProvider.setEmf(Persistence.createEntityManagerFactory("baza.podataka.za.blog"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerFactory emf = JPAEMFProvider.getEmf();
		JPAEMFProvider.setEmf(null);
		if(emf != null) {
			emf.close();
		}
	}
}