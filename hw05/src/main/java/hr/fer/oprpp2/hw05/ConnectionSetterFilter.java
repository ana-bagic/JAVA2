package hr.fer.oprpp2.hw05;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

import hr.fer.oprpp2.hw05.dao.sql.SQLConnectionProvider;

/**
 * Filter used to set connection of the received client request.
 * 
 * @author Ana Bagić
 *
 */
@WebFilter(filterName="f1",urlPatterns={"/servleti/*"})
public class ConnectionSetterFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		DataSource ds = (DataSource)request.getServletContext().getAttribute(Inicijalizacija.POOL_ATTR);
		
		try(Connection con = ds.getConnection()) {
			SQLConnectionProvider.setConnection(con);
			
			try {
				chain.doFilter(request, response);
			} finally {
				SQLConnectionProvider.setConnection(null);
			}
		} catch (SQLException e) {
			throw new IOException("Database is not available.", e);
		}

	}
	
}