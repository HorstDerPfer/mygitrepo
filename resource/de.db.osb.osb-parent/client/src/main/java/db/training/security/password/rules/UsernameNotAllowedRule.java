package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;

/**
 * Prueft, dass der Benutzername nicht im Kennwort enthalten ist.
 * 
 * <pre>
 * Konzernrichtlinie 114.0231A03 Abchnitt 2.5
 * </pre>
 * 
 * @author hennebrueder
 * 
 */
public class UsernameNotAllowedRule implements PasswordRule {

    public void validate(String username, List<String> passwordHistory, String newPassword,
	    String newHashedPassword) throws PasswordException {

	/* Diese Kennwortregel ist nicht verantwortlich fuer leere Kennwoerter oder Benutzernamen */
	if (username == null
		|| username.trim().length() == 0 || newPassword == null
		|| newPassword.trim().length() == 0)
	    return;

	String trimmedPassword = newPassword.trim();

	if (trimmedPassword.length() >= username.length()
		&& trimmedPassword.contains(username))
	    throw new PasswordException(Description.UNSAFE_PASSWORD);

    }

}
