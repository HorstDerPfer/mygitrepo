package db.training.security.password.rules;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;
/**
 * Prueft, dass ein Kennwort mindestens eine Zahl oder ein Sonderzeichen enthaelt.
 * Konzernrichtlinie 114.0231A03 Abchnitt 2.3
 * @author hennebrueder
 *
 */
public class RequireSpecialCharacterNumberRule implements PasswordRule {

    public void validate(String username, List<String> passwordHistory, String newPassword,
	    String newHashedPassword) throws PasswordException {
	String trimmedPassword = newPassword == null ? "" :  newPassword.trim();

	if (StringUtils.containsNone(trimmedPassword, "0123456789") && 
		StringUtils.containsNone(trimmedPassword, "(!“§$%&/(){}[]=?.,:;-_#+*<>") )
	    throw new PasswordException(Description.UNSAFE_PASSWORD);

    }

}
