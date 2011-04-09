package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;

/**
 * Prueft, dass ein Kennwort Zeichen in Gross und Kleinschreibung enthaelt. Konzernrichtlinie
 * 114.0231A03 Abchnitt 2.4
 * 
 * @author hennebrueder
 * 
 */
public class RequireUpperLowerRule implements PasswordRule {

    public void validate(String username, List<String> passwordHistory, String newPassword,
	    String newHashedPassword) throws PasswordException {
	String trimmedPassword = newPassword == null ? "" : newPassword.trim();

	if (trimmedPassword.toLowerCase().equals(trimmedPassword)
		|| trimmedPassword.toUpperCase().equals(trimmedPassword))
	    throw new PasswordException(Description.UNSAFE_PASSWORD);

    }

}
