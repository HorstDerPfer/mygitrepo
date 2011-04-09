package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;

/**
 * Prueft, ob maximale Anzahl an Passwort Fehleingaben erreicht ist.
 * 
 * @author hennebrueder
 * 
 */
public class LimitBadLoginAttemptsRule implements PasswordRule {

    /* maximale Zahl erlaubter Loginversuche */
    @SuppressWarnings("unused")
    private int maxLogin = 5;

    public LimitBadLoginAttemptsRule() {

    }

    public LimitBadLoginAttemptsRule(int maxLogin) {
	super();
	this.maxLogin = maxLogin;
    }

    public void validate(String username, List<String> passwordHistory, String newPassword,
	String newHashedPassword) throws PasswordException {
	throw new RuntimeException("Not yet implemented");
    }
}
