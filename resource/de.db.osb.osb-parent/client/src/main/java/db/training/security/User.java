package db.training.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public abstract class User implements Serializable {

	private static int maxLoginFailures = 4;

	/** hashed password */
	protected String password;

	/** hashed temporaeres Kennwort bei Kennwortrequest */
	protected String newPassword;

	/** hash des ersten Kennworts */
	protected String firstPassword;

	/** Liste der letzten <code>historySize</code> Kennwoerter */
	protected List<String> passwordHistory = new ArrayList<String>();

	/** Benutzername bzw. Loginname */
	protected String username;

	/** false, wenn der Account nicht mehr gueltig ist (nach einem bestimmten Zeitraum) */
	protected boolean accountNonExpired = true;

	/** false, wenn der Account gesperrt wurden, z.B. nach einem bestimmten */
	protected boolean accountNonLocked = true;

	/** false, wenn das Kennwort veraltet ist */
	protected boolean credentialsNonExpired = true;

	/** false, wenn der Account nicht aktiviert ist, hat keinen Zeitraumbezug */
	protected boolean enabled = true;

	/** anzahl der falschen Logins */
	protected int failedLogin;

	/** Set von Rollen, die einem Benutzer zugeordnet sind */
	protected Set<Role> roles = new HashSet<Role>();

	/**
	 * Anzahl der Kennworter, die nicht wiederverwendet werden duerfen Konzernrichtlinie 114.0231A03
	 * Abchnitt 2.3
	 */
	protected int historySize = 6;

	public User() {
		this.unlock();
	}

	public User(String username) {
		super();
		this.username = username;
		this.password = firstPassword;
		this.unlock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.User#increaseFailedLogin()
	 */
	public void increaseFailedLogin() {
		failedLogin++;
		if (failedLogin > maxLoginFailures)
			lock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.User#resetFailedLogin()
	 */
	public void resetFailedLogin() {
		failedLogin = 0;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public List<String> getPasswordHistory() {
		return passwordHistory;
	}

	public void changePassword(String oldPassword, String newPassword) throws SecurityException {
		String trimmedPassword = null;
		if (newPassword != null)
			trimmedPassword = newPassword.trim();
		if (trimmedPassword == null || trimmedPassword.length() == 0)
			throw new SecurityException("Password is null or empty.");
		if (oldPassword == null || !password.equals(oldPassword))
			throw new SecurityException("Old password is not correct.");
		if (oldPassword.equals(newPassword))
			throw new SecurityException("Altes und neues Kennwort muss unterschiedlich sein.");
		this.password = trimmedPassword;
		// Kennwort zur Historie hinzufuegen und Historie auf max. Laenge kuerzen
		passwordHistory.add(oldPassword);
		while (passwordHistory.size() > historySize)
			passwordHistory.remove(0);

	}

	public void initPassword(String firstPassword) {
		this.firstPassword = firstPassword;
		password = firstPassword;
		passwordHistory.clear();

	}

	public void changePasswordRequest(String oldPassword, String newPassword)
	    throws SecurityException {
		String trimmedPassword = null;
		if (newPassword != null)
			trimmedPassword = newPassword.trim();
		if (trimmedPassword == null || trimmedPassword.length() == 0)
			throw new SecurityException("Password is null or empty.");
		if (oldPassword == null || !password.equals(oldPassword))
			throw new SecurityException("Old password is not correct.");
		this.newPassword = trimmedPassword;

	}

	public void confirmPasswordRequest() {
		// Kennwort zur Historie hinzufuegen und Historie auf max. Laenge kuerzen
		passwordHistory.add(password);
		while (passwordHistory.size() > historySize)
			passwordHistory.remove(0);
		password = newPassword;
		newPassword = null;

	}

	public void disable() {
		enabled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.User#enableUser()
	 */
	public void enable() {
		enabled = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.User#lock()
	 */
	public void lock() {
		accountNonLocked = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.security.User#unlock()
	 */
	public void unlock() {
		accountNonLocked = true;
		resetFailedLogin();
	}

	public int getLoginAttempts() {
		return failedLogin;
	}

	public void resetPassword() {
		password = firstPassword;
	}

	public int getHistorySize() {
		return historySize;
	}

	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}

	public boolean hasAuthorization(String authorizationName) {
		// boolean hasrole = false;
		// GrantedAuthority authoritys[] = getAuthorities();
		// for (GrantedAuthority authority : authoritys) {
		// if( authority.getAuthority().equals(role)){
		// hasrole = true;
		// break;
		// }
		//	
		return false;
	}
}
