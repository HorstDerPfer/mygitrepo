package db.training.easy.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchConfig;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.SearchConfigService;
import db.training.bob.util.CollectionHelper;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.dao.UserDaoImpl;
import db.training.easy.core.model.User;
import db.training.logwrapper.Logger;
import db.training.security.SecurityAdministrationService;

/**
 * 
 * @author MarkusHohloch
 * 
 */
public class UserServiceImpl extends EasyServiceImpl<User, Serializable> implements UserService {

	private UserDaoImpl dao;

	private static Logger log = Logger.getLogger(UserServiceImpl.class);

	/* speichere den aktuellen Anwendungsnutzer im Thread */
	private static ThreadLocal<User> currentApplicationUser = new ThreadLocal<User>();

	private SecurityAdministrationService securityAdministrationService;

	public UserServiceImpl(SessionFactory sf) {
		super(User.class);
		sessionfactory = sf;
	}

	public void setDao(UserDaoImpl dao) {
		this.dao = dao;
	}

	public UserDaoImpl getDao() {
		return this.dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findAllUsers()
	 */
	@Transactional
	public List<User> findAllUsers() {
		List<User> users = dao.findAll();
		Collections.sort(users);
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.service.UserService#findUsersByRegionalbereich(db.training.bob.model
	 * .Regionalbereich)
	 */
	@Transactional
	public List<User> findUsersByRegionalbereich(Regionalbereich regionalbereich) {
		List<User> users = dao.findByRegionalbereich(regionalbereich);
		Collections.sort(users);
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUserById(java.lang.Integer)
	 */
	@Transactional
	public User findUserById(Integer userId) {
		User user = null;
		user = dao.findById(userId);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUsersByRoleName(java.lang.String)
	 */
	@Transactional
	public List<User> findUsersByRoleName(String roleName) {
		// TODO: Spring Konfiguration
		List<db.training.security.User> secUsers = securityAdministrationService
		    .findUserByRoleName(roleName);

		String loginNames[] = new String[secUsers.size()];
		for (int i = 0; i < loginNames.length; i++) {
			loginNames[i] = secUsers.get(i).getUsername();
		}

		List<User> users = new ArrayList<User>();
		if (loginNames.length > 0)
			users = findUsersByLoginnames(loginNames);

		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUserByEmail(java.lang.String)
	 */
	@Transactional
	public User findUserByEmail(String email) {
		User user = dao.findUserByEmail(email);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUserByLoginName(java.lang.String)
	 */
	@Transactional
	public User findUserByLoginName(String loginName) {
		User user = dao.findUserByLoginName(loginName);
		return user;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#deleteUser(java.lang.Integer)
	 */
	@Transactional(readOnly = false)
	public void deleteUser(Integer userID) {
		// Damit ein Benutzer geloescht werden kann, muss dieser User als Favorit(Bearbeiter) aus
		// allen Baumassnahmen entfernt werden!

		// Deklaration
		User user = dao.findById(userID);
		Session session = EasySessionFactory.getInstance().getCurrentSession();
		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		List<Baumassnahme> bmList = null;
		List<Integer> bearbeiterIds = new ArrayList<Integer>();
		// Liste aller Baumassnahmen in denen der angebene User Bearbeiter(Favorit) ist
		bmList = bmService.findByBearbeiter(user.getId());

		// Durchläiuft alle Baumassnahmen und deren Bearbeiter und vergleicht die Ids ob es sich um
		// den gesuchten User handelt
		// speichert alle zu löschenden Bearbeiter-Ids in einer Arraylist um diese dann per Query
		// auszufuehren
		for (Baumassnahme bm : bmList) {
			for (Bearbeiter b : bm.getBearbeiter()) {
				if (b.getUser().getId().equals(user.getId()))
					bearbeiterIds.add(b.getId());
			}
		}

		if (bearbeiterIds.size() != 0) {
			// Bearbeiter-Ids als Komma-Separierten String zurückgeben
			String bearbeiterIdList = CollectionHelper.toSeparatedStringListSize(bearbeiterIds,
			    ",", 0, bearbeiterIds.size());
			// Delete-Query auf Link-Tabelle ausfuehren
			Query query = session
			    .createSQLQuery("DELETE FROM bm_bearbeiter WHERE bearbeiter_id in ("
			        + bearbeiterIdList + ")");
			query.executeUpdate();

			// Bearbeiter loeschen
			String hql = "delete from Bearbeiter where user.id = :userId";
			Query hqlQuery = session.createQuery(hql);
			hqlQuery.setInteger("userId", user.getId());
			hqlQuery.executeUpdate();
		}

		// die gespeicherten SearchConfigs muessen ebenfalls geloescht werden
		SearchConfigService scService = EasyServiceFactory.getInstance()
		    .createSearchConfigService();
		SearchConfig sc = null;
		sc = scService.findByUser(user);
		if (sc!=null)
			scService.delete(sc);
		
		// Wenn alle Bearbeiter der Baumassnahmen geloescht sind, kann der User geloescht werden!
		dao.delete(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.service.UserService#createUser(db.training.easy.core.dao.model.User)
	 */
	@Transactional(readOnly = false)
	public void createUser(User user) {
		dao.save(user);
	}

	@Transactional(readOnly = false)
	public void updateUser(User user) {
		dao.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUsersByLoginnames(java.lang.String[])
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public List<User> findUsersByLoginnames(String... loginNames) {
		for (String string : loginNames) {
			if (log.isDebugEnabled())
				log.debug(string);
		}
		List<User> result = getCurrentSession().createCriteria(User.class).add(
		    Restrictions.in("loginName", loginNames)).list();
		return result;
	}

	public void setSecurityAdministrationService(SecurityAdministrationService securityService) {
		this.securityAdministrationService = securityService;
	}

	public static User getCurrentApplicationUser() {
		return currentApplicationUser.get();
	}

	public static void setCurrentApplicationUser(User user) {
		currentApplicationUser.set(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.service.UserService#findUserById(java.lang.Integer,
	 * db.training.easy.core.service.UserService.UserFetchPlan[])
	 */
	@Transactional
	public User findUserById(Integer userId, UserFetchPlan[] fetchPlans) {
		User user = dao.findById(userId);
		fillUser(user, fetchPlans);
		return user;
	}

	/**
	 * Fuelle User Objekt anhand der Fremdschluessel mit zugehoerigen Objekten
	 * 
	 * @param user
	 * @param fetchPlans
	 */
	private void fillUser(User user, UserFetchPlan[] fetchPlans) {
		if (user == null)
			return;
		@SuppressWarnings("unused")
		List<UserFetchPlan> plans;
		if (fetchPlans == null)
			plans = new ArrayList<UserFetchPlan>();
		else
			plans = Arrays.asList(fetchPlans);
	}

}