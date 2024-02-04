package hr.fer.oprpp2.hw05;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Listener for the servlet context lifecycle.
 * It takes care of the opening and the closing of the database connection pool.
 * 
 * @author Ana Bagić
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	/** Key for data source stored in the servlet context. */
	public static final String POOL_ATTR = "dbpool";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Error while initializing pool.", e);
		}
		cpds.setJdbcUrl(getConnectionUrl(sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties")));

		sce.getServletContext().setAttribute(POOL_ATTR, cpds);
		
		InitDatabase.createTablesIfNecessary(cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute(POOL_ATTR);
		if(cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Creates connection url to the database from configuration file.
	 * 
	 * @param pathToProperties path to the properties file containing configuration for the database
	 * @return connection url to the database
	 */
	private String getConnectionUrl(String pathToProperties) {
		Properties dbp = new Properties();
		
		try(InputStream is = Files.newInputStream(Paths.get(pathToProperties))) {
			dbp.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String host = dbp.getProperty("host");
		String port = dbp.getProperty("port");
		String name = dbp.getProperty("name");
		String user = dbp.getProperty("user");
		String password = dbp.getProperty("password");
		return "jdbc:derby://" + host + ":" + port + "/" + name + ";user=" + user + ";password=" + password;
	}

}
