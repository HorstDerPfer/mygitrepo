package db.training.security.hibernate;

import java.util.List;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.encoding.ShaPasswordEncoder;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.hibernate.HibernateCommand;
import db.training.hibernate.TxTemplate;
import db.training.security.Role;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityException;
import db.training.security.User;
import db.training.security.password.PasswordException;
import db.training.security.password.PasswordValidator;
import db.training.security.password.PasswordValidatorImpl;

public class SecurityAdministrationServiceImpl implements SecurityAdministrationService {
	private SaltSource saltSource;

	private PasswordEncoder passwordEncoder;

	private PasswordValidator passwordValidator;

	private TxTemplate txTemplate;

	public SecurityAdministrationServiceImpl() {
		super();
		/* Konfiguration anstelle von Spring basierter Konfiguration */
		ReflectionSaltSource reflectionSaltSource = new ReflectionSaltSource();
		reflectionSaltSource.setUserPropertyToUse("getUsername");
		saltSource = reflectionSaltSource;
		passwordEncoder = new ShaPasswordEncoder();
		passwordValidator = new PasswordValidatorImpl();
	}

	public void setPasswordValidator(PasswordValidator passwordValidator) {
		this.passwordValidator = passwordValidator;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setTxTemplate(TxTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}

	@Transactional(readOnly = false)
	public void initPassword(User user, String newPassword) throws PasswordException {
		/* in TQM we expect the user to be an TQM User */
		TqmUser tqmUser = (TqmUser) user;
		String password = createPasswordHash(newPassword, tqmUser);
		passwordValidator.validate(user, newPassword, password);
		tqmUser.initPassword(createPasswordHash(password, user));
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void changePassword(final User user, String oldPassword, String newPassword)
	    throws SecurityException, PasswordException {
		passwordValidator.validate(user, newPassword, createPasswordHash(newPassword, user));
		user.changePassword(createPasswordHash(oldPassword, user), createPasswordHash(newPassword,
		    user));
		user.setCredentialsNonExpired(true);
		updateUser(user);
	}

	public String createPasswordHash(String newPassword, User user) {
		/* we can expect a Tqmuser */
		TqmUser tqmUser = (TqmUser) user;
		return passwordEncoder.encodePassword(newPassword, saltSource.getSalt(tqmUser));
	}

	@Transactional(readOnly = false)
	public void changePasswordRequest(final User user, String oldPassword, String newPassword)
	    throws SecurityException, PasswordException {

		passwordValidator.validate(user, newPassword, createPasswordHash(newPassword, user));
		user.changePasswordRequest(createPasswordHash(oldPassword, user), createPasswordHash(
		    newPassword, user));
		user.setCredentialsNonExpired(true);
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void confirmPasswordRequest(final User user) {
		user.confirmPasswordRequest();
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void createUser(final User user, String password) {
		user.initPassword(createPasswordHash(password, user));
		txTemplate.execute(new HibernateCommand() {
			@Transactional(readOnly = false)
			public Object execute(Session session, Object... param) {
				session.save(user);
				return null;
			}
		});
	}

	@Transactional(readOnly = false)
	public void updateUser(final User user) {
		txTemplate.execute(new HibernateCommand() {
			@Transactional(readOnly = false)
			public Object execute(Session session, Object... param) {
				session.update(user);
				return null;
			}
		});
	}

	@Transactional(readOnly = false)
	public void deleteUser(final User user) {
		txTemplate.execute(new HibernateCommand() {
			@Transactional(readOnly = false)
			public Object execute(Session session, Object... param) {
				session.delete(user);
				return null;
			}
		});

	}

	@Transactional(readOnly = false)
	public void resetPassword(User user) {
		user.resetPassword();
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void addRole(User user, Role role) {
		user.addRole(role);
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void removeRole(User user, Role role) {
		user.removeRole(role);
		updateUser(user);
	}

	@Transactional(readOnly = false)
	public void createRole(final Role role) {
		txTemplate.execute(new HibernateCommand() {
			@Transactional(readOnly = false)
			public Object execute(Session session, Object... param) {
				session.save(role);
				return null;
			}
		});
	}

	@Transactional(readOnly = false)
	public void deleteRole(final Role role) {
		txTemplate.execute(new HibernateCommand() {
			@SuppressWarnings("unchecked")
			@Transactional(readOnly = false)
			public Object execute(Session session, Object... param) {
				session.lock(role, LockMode.NONE);
				List<TqmUser> users = session.createQuery(
				    " from TqmUser u where ? in elements(u.roles)").setEntity(0, role).list();
				for (TqmUser impl : users) {
					impl.removeRole(role);
				}
				session.delete(role);
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional()
	public List<User> findAllActiveuser() {
		return (List<User>) txTemplate.executeList(new HibernateCommand() {
			@Transactional()
			public Object execute(Session session, Object... param) {
				return session.createQuery(
				    "select u from TqmUser u left join u.roles where u.enabled = ?").setBoolean(0,
				    true).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional()
	public List<TqmRole> findAllRoles() {
		return (List<TqmRole>) txTemplate.executeList(new HibernateCommand() {
			@Transactional()
			public Object execute(Session session, Object... param) {
				return session.createCriteria(TqmRole.class).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Role> findAllFirstLevelRoles() {
		return (List<Role>) txTemplate.executeList(new HibernateCommand() {
			@Transactional()
			public Object execute(Session session, Object... param) {
				return session.createCriteria(TqmRole.class).add(Restrictions.isNull("parentRole"))
				    .list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> findAllUser() {
		return (List<User>) txTemplate.executeList(new HibernateCommand() {
			@Transactional()
			public Object execute(Session session, Object... param) {
				return session.createQuery("select u from TqmUser u left join u.roles")
				    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> findUserByRoleName(final String roleName) {
		return (List<User>) txTemplate.executeList(new HibernateCommand() {
			@Transactional()
			public Object execute(Session session, Object... param) {
				return session.createQuery(
				    "select u from TqmUser u left join u.roles r where r.name=?").setString(0,
				    roleName).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				    .list();
			}
		});
	}
}