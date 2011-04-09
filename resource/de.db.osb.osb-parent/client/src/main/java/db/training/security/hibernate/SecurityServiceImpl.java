package db.training.security.hibernate;

import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import db.training.hibernate.HibernateCommand;
import db.training.hibernate.TxTemplate;
import db.training.logwrapper.Logger;
import db.training.security.Role;
import db.training.security.SecurityService;
import db.training.security.User;

public class SecurityServiceImpl implements SecurityService {
	private static Logger log = Logger.getLogger(SecurityServiceImpl.class);

	private TxTemplate txTemplate;

	public SecurityServiceImpl() {
		super();
	}

	public void setTxTemplate(TxTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}

	@Transactional
	public UserDetails loadUserByUsername(final String name)
			throws UsernameNotFoundException, DataAccessException {
		if (log.isDebugEnabled())
			log.debug("Loading user: " + name);
		TqmUser tqmUser = null;

		if (name == null || name.trim().equals(""))
			throw new UsernameNotFoundException(
					"User not found for empty name.");

		final String trimmedName = name.trim();

		tqmUser = (TqmUser) txTemplate.executeUnique(new HibernateCommand() {
			@Transactional
			public Object execute(Session session, Object... param) {
				// TqmUser tqmUser = (TqmUser) session.createQuery(
				// "select u from TqmUser u left join fetch u.roles r where u.username = :name")
				// .setCacheable(true).setString("name",
				// trimmedName).uniqueResult();

				Criteria c = session.createCriteria(TqmUser.class);
				c.add(Restrictions.sqlRestriction("upper(username)=upper('"
						+ trimmedName + "')"));
				c.createAlias("roles", "roles", CriteriaSpecification.LEFT_JOIN);
				c.setCacheable(true);
				TqmUser tqmUser = (TqmUser) c.uniqueResult();

				if (tqmUser != null)
					initRoles(tqmUser.getRoles());
				return tqmUser;
			}

			private void initRoles(Set<Role> roles) {
				Hibernate.initialize(roles);
				for (Role role : roles) {
					Hibernate.initialize(role.getAuthorizations());
					initRoles(role.getSubRoles());
				}
			}
		});

		if (tqmUser == null)
			throw new UsernameNotFoundException("User not found.");
		return tqmUser;
	}

	public User getCurrentUser() {
		User user = null;

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null) { // can be null, if Spring security is
										// deactivated during
			// development
			Object object = authentication.getPrincipal();
			if (object instanceof TqmUser)
				user = (TqmUser) object;
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.SecurityService#loginFailed(String)
	 */
	@Transactional(readOnly = false)
	public void loginFailed(String userName) {
		UserDetails userDetails = loadUserByUsername(userName);
		if (userDetails != null) {
			TqmUser extendedUserDetails = (TqmUser) userDetails;
			extendedUserDetails.increaseFailedLogin();
			updateUser(extendedUserDetails);
		}
	}

	@Transactional(readOnly = false)
	protected void updateUser(final TqmUser userDetails) {

		txTemplate.execute(new HibernateCommand() {
			@Transactional
			public Object execute(Session session, Object... param) {
				session.update(userDetails);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.SecurityService#loginSuccess(String)
	 */
	@Transactional(readOnly = false)
	public void loginSuccess(String userName) {
		UserDetails userDetails = loadUserByUsername(userName);
		if (userDetails != null) {
			TqmUser extendedUserDetails = (TqmUser) userDetails;
			if (extendedUserDetails.getLoginAttempts() != 0) {
				extendedUserDetails.resetFailedLogin();
				updateUser(extendedUserDetails);
			}
		}
	}
}
