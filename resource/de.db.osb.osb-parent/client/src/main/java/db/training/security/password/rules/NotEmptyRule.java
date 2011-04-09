package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;
/**
 * Prueft, ob das Kennwort null oder nur aus Leerzeichen bzw. Tabulatoren besteht.
 * Konzernrichtlinie 114.0231A03 Abchnitt 2.2
 * @author hennebrueder
 *
 */
public class NotEmptyRule implements PasswordRule {

    public void validate(String username, List<String> passwordHistory, String newPassword, String newHashedPassword) throws PasswordException {
	if (newPassword == null || newPassword.trim().equals(""))
	    throw new PasswordException(Description.EMPTY_PASSWORD);

    }

}
