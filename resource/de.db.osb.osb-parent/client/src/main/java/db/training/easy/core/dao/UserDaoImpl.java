package db.training.easy.core.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDaoImp;
import db.training.easy.core.model.User;
import db.training.logwrapper.Logger;

public class UserDaoImpl extends BasicDaoImp<User, Integer> implements UserDao {

	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	public UserDaoImpl(SessionFactory sessionFactory) {
		super(User.class, sessionFactory);
	}

	public User findUserByEmail(String email) {
		if (log.isDebugEnabled())
			log.debug("finding User by email=" + email);
		// return findUniqueByCriteria(Restrictions.eq("email", email));
		return findUniqueByCriteria(Restrictions
				.sqlRestriction("upper(email)=upper('" + email + "')"));
	}

	public User findUserByLoginName(String login) {
		if (log.isDebugEnabled())
			log.debug("findUserByLoginName:" + login);
		return findUniqueByCriteria(Restrictions.eq("loginName", login));
	}

	@SuppressWarnings("unchecked")
	public List<User> findByRegionalbereich(Regionalbereich regionalbereich) {
		return getCurrentSession()
				.createQuery("select u from User u where u.regionalbereich = ?")
				.setEntity(0, regionalbereich).list();
	}
}