package hr.fer.oprpp2.hw06.dao.jpa;

import java.util.List;

import hr.fer.oprpp2.hw06.dao.DAO;
import hr.fer.oprpp2.hw06.dao.DAOException;
import hr.fer.oprpp2.hw06.model.BlogEntry;
import hr.fer.oprpp2.hw06.model.BlogUser;

/**
 * Implementation of the DAO interface using JPA technology.
 *  
 * @author Ana Bagić
 * 
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public List<BlogUser> getAllUsers() {
		return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.all", BlogUser.class).getResultList();
	}

	@Override
	public BlogUser getUser(String nick) {
		List<BlogUser> result = JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogUser.findByNick", BlogUser.class).setParameter("nick", nick)
				.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public void save(Object object) {
		JPAEMProvider.getEntityManager().persist(object);
	}

	@Override
	public void update(Object object) {
		JPAEMProvider.getEntityManager().merge(object);
	}

}