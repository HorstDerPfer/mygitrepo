package db.training.security;

import org.acegisecurity.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

	/**
	 * gibt den aktuellen Nutzer des Sicherheitskontextes zurueck
	 * 
	 * @return
	 */
	public User getCurrentUser();

	/**
	 * Wird verwendet von einem ApplicationListener, um die anzahl der fehlgeschlagenen Logins zu
	 * erhöhen
	 * 
	 * @param userName
	 */
	public void loginFailed(String userName);

	/**
	 * Wird bei einem erfolgreichen Login aufgerufen, um fehlgeschlagenen Logins zurueckzusetzen.
	 * 
	 * @param userName
	 */
	public void loginSuccess(String userName);
}
