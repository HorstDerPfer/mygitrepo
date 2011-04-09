package db.training.easy.core.service;

import java.util.List;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.User;

/**
 * 
 * @author MarkusHohloch
 * 
 */
public interface UserService {

	public enum UserFetchPlan {
	}

	/**
	 * Sucht alle im System vorhandenen User
	 * 
	 * @return Liste mit User Objekten
	 */
	public List<User> findAllUsers();

	/**
	 * Sucht im System vorhandenen User einer Regionalbereich
	 * 
	 * @param regionalbereich
	 * @return Liste mit User Objekten
	 */
	public List<User> findUsersByRegionalbereich(Regionalbereich regionalbereich);

	/**
	 * Sucht einen User anhand der User-ID
	 * 
	 * @param userId
	 * @return User
	 */
	public User findUserById(Integer userId);

	/**
	 * Sucht einen User anhand der User-ID und laed Objekte anhand FetchPlan nach
	 * 
	 * @param userId
	 * @param fetchPlans
	 * @return
	 */
	public User findUserById(Integer userId, UserFetchPlan[] fetchPlans);

	/**
	 * 
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email);

	/**
	 * Sucht alle Benutzer, welche die angegebene Role besitzen
	 * 
	 * @param roleName
	 * @return
	 */
	public List<User> findUsersByRoleName(String roleName);

	/**
	 * 
	 * @param loginName
	 * @return
	 */
	public User findUserByLoginName(String loginName);

	/**
	 * findet Benutzer ueber den Loginnamen. Es koennen mehrere LoginNamen uebergeben werden.
	 * 
	 * @param loginNames
	 * @return
	 */
	public List<User> findUsersByLoginnames(String... loginNames);

	/**
	 * 
	 * @param userID
	 */
	public void deleteUser(Integer userID);

	/**
	 * Erzeugt einen neuen Benutzer.
	 * 
	 * @param user
	 */
	public void createUser(User user);

	public void updateUser(User user);
}
