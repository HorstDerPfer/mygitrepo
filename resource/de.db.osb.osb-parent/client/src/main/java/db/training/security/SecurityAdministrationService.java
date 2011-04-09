package db.training.security;

import java.util.List;

import db.training.security.hibernate.TqmRole;
import db.training.security.password.PasswordException;
import db.training.security.password.PasswordValidator;

/**
 * Service Methoden, um Benutzer, Rollen und Rechte zu administrieren.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public interface SecurityAdministrationService {

	/**
	 * erlaubt einen Kennwordvalidator zu setzen
	 * 
	 * @param passwordRule
	 */
	public void setPasswordValidator(PasswordValidator passwordValidator);

	/**
	 * Aendert das Kennwort fuer den Benutzer, wenn das alte Kennwort korrekt ist.
	 * 
	 * @throws SecurityException,
	 *             wenn das alte Kennwort nicht gueltig ist
	 * @throws PasswordException,
	 *             wenn das neue nicht den Regeln entspricht.
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePassword(User user, String oldPassword, String newPassword)
	    throws SecurityException, PasswordException;

	/**
	 * Erstellt einen Benutzer und generiert ein initiales Kennwort.
	 * 
	 * @param user
	 * @param password
	 *            Initialkennwort
	 * @throws PasswordException -
	 *             wenn das Kennwort nicht den Bahn Regeln entsprich
	 */
	public void createUser(User user, String password) throws PasswordException;

	/**
	 * Loescht einen Benutzer
	 * 
	 * @param user
	 */
	public void deleteUser(User user);

	/**
	 * Aktualisiert den Benutzer.
	 * 
	 * @param user
	 */
	public void updateUser(User user);

	/**
	 * Setzt das sowohl das erste als auch das aktuelle Kennwort eines Benutzers und l�scht die
	 * Kennworthistorie.
	 * 
	 * @throws PasswordException,
	 *             wenn das neue Kennwort nicht den Regeln entspricht.
	 * @param user
	 * @param firstPassword
	 */
	public void initPassword(User user, String newPassword) throws PasswordException;

	/**
	 * Setzt ein Kennwort auf das initiale Kennwort zurueck.
	 * 
	 * @param user
	 */
	public void resetPassword(User user);

	/**
	 * Speichert das neue Kennwort fuer den Benutzer in einer Variable, dass bestaetigt werden muss.
	 * Das erlaubt einen zweistufigen Kennwort-Aenderungsprozess. Im ersten Schritt wird ein
	 * temporares Kennwort erstellt. Der Benutzer koennte eine Emailbestaetigung erhalten, die einen
	 * Link zu einem Dialog enthaelt, in dem die Aenderung bestaetigt wird.
	 * 
	 * @throws SecurityException,
	 *             wenn das alte Kennwort nicht gueltig ist.
	 * @throws PasswordException,
	 *             wenn das neue Kennwort nicht den Regeln entspricht.
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePasswordRequest(User user, String oldPassword, String newPassword)
	    throws SecurityException, PasswordException;

	/**
	 * Erzeugt einen Hash aus Passwort und dem Loginnamen eines Benutzers.
	 * 
	 * @param newPassword
	 * @param user
	 */
	public String createPasswordHash(String newPassword, User user);

	/**
	 * Setzt das Kennwort auf das temporaere Kennwort und loescht das temporaere Kennwort.
	 * 
	 * @param user
	 */
	public void confirmPasswordRequest(User user);

	/**
	 * Erzeugt die Rolle
	 * 
	 * @param role
	 */
	public void createRole(Role role);

	/**
	 * Loescht eine Rolle und entfernt diese bei allen Benutzern
	 * 
	 * @param role
	 */
	public void deleteRole(Role role);

	/**
	 * Gibt eine Liste aller Benutzer zurueck
	 * 
	 * @return
	 */
	public List<User> findAllUser();

	/**
	 * Gibt eine Liste aller aktiven Benutzer zurueck
	 * 
	 * @return
	 */
	public List<User> findAllActiveuser();

	/**
	 * Gibt eine Liste der Rollen, der ersten Ebene zurueck.
	 * 
	 * @return
	 */
	public List<Role> findAllFirstLevelRoles();

	/**
	 * Gibt alle Benutzer zurueck, die eine bestimmte Rolle haben
	 * 
	 * @param roleName
	 * @return
	 */
	public List<User> findUserByRoleName(String roleName);

	/**
	 * Gibt eine Liste aller Rollen zurueck
	 * 
	 * @return
	 */
	public List<TqmRole> findAllRoles();

	/**
	 * Fuegt dem Benutzer eine Rolle hinzu. Wenn die Rolle bereits hinzugefuegt wurde, wird nichts
	 * unternommen.
	 * 
	 * @param user
	 * @param role
	 *            die neue Rolle des Benutzers
	 */
	public void addRole(User user, Role role);

	/**
	 * entfernt eine Rolle, wenn die Rolle nicht gefunden wird, wird kein Fehler geworfen
	 * 
	 * @param user
	 * @param role
	 *            die neue Rolle des Benutzers
	 */
	public void removeRole(User user, Role role);
}
