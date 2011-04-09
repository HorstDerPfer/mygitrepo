package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;

/**
 * Klasse validiert die Laenge des Kennworts Konzernrichtlinie 114.0231A03 Abchnitt 2.2
 * 
 * @author hennebrueder
 * 
 */
public class LengthRule implements PasswordRule {
    int min = 8;

    public void validate(String username, List<String> history, String newPassword,
	    String newHashedPassword) throws PasswordException {

	String trimmedPassword = (newPassword == null) ? "" : newPassword.trim();
	if (trimmedPassword.length() < 6)
	    throw new PasswordException(Description.PASSWORD_TO_SHORT);
    }

}
