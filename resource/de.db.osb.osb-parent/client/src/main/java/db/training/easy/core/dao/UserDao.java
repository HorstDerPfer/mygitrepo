package db.training.easy.core.dao;

import java.util.List;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDao;
import db.training.easy.core.model.User;

/**
 * Dao für User-Objekt
 * 
 * @author MarkusHohloch
 * 
 */
public interface UserDao extends BasicDao<User, Integer> {

	/**
	 * Sucht User ueber E-Mail-Adresse
	 * 
	 * @param email
	 * @return gesuchter User oder null
	 */
	public User findUserByEmail(String email);

	/**
	 * Find user by login name
	 * 
	 * @param login
	 * @return gesuchter User oder null
	 */
	public User findUserByLoginName(String login);

	public List<User> findByRegionalbereich(Regionalbereich regionalbereich);
}