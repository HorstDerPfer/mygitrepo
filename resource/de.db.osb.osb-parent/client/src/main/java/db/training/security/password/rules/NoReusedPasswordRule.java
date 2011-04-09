package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;

/**
 * Prueft, ob ein Kennwort in der Historie enthalten ist. Wenn die uebergebenen Werte Null sind,
 * wird angenommen, dass der Wert in der Historie enthalten sind. Konzernrichtlinie 114.0231A03
 * Abchnitt 2.3
 * 
 * @author hennebrueder
 * 
 */
public class NoReusedPasswordRule implements PasswordRule {

	public void validate(String username, List<String> passwordHistory, String newPassword, String newHashedPassword)
	    throws PasswordException {
		if (passwordHistory == null || newHashedPassword == null)
			throw new PasswordException(Description.REUSED_PASSWORD);
		String trimmedpassword = newHashedPassword.trim();

		for (String oldPassword : passwordHistory) {
			if (trimmedpassword.equals(oldPassword))
				throw new PasswordException(Description.REUSED_PASSWORD);
		}
	}
}
