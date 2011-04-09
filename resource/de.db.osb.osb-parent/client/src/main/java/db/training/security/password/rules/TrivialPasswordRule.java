package db.training.security.password.rules;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import db.training.security.password.PasswordException;
import db.training.security.password.PasswordException.Description;
/**
 * Klasse prueft ob das Kennwoert trivial ist
 * Konzernrichtlinie 114.0231A03 Abchnitt 2.2
 * @author hennebrueder
 *
 */
public class TrivialPasswordRule implements PasswordRule {

	final static String abc = "abcdefghijklmnopqrstuvwxyz";
	final static String zahlenfolge = "01234567890";
	final static String trivialPasswords[] = { "admin", "geheim", "kein" };
	
    public void validate(String username, List<String> passwordHistory, String newPassword, String newHashedPassword) throws PasswordException {

	/* Diese Kennwortregel ist nicht verantwortlich fuer leere Kennwoerter */
	if (newPassword == null || newPassword.trim().length() == 0)
	    return;

	String trimmedLowerPassword = newPassword.trim().toLowerCase();

	/* Validiere kennwort, dass nur aus einem bestimmten Zeichen besteht */
	char first = trimmedLowerPassword.charAt(0);
	if (StringUtils.containsOnly(trimmedLowerPassword, new char[] { first }))
	    throw new PasswordException(Description.TRIVIAL_PASSWORD);

	
	if (trimmedLowerPassword.length() <= abc.length())
	    if (abc.contains(trimmedLowerPassword))
		throw new PasswordException(Description.TRIVIAL_PASSWORD);

	
	if (trimmedLowerPassword.length() <= zahlenfolge.length())
	    if (zahlenfolge.contains(trimmedLowerPassword))
		throw new PasswordException(Description.TRIVIAL_PASSWORD);

	
	for (int i = 0; i < trivialPasswords.length; i++) {
	    if (trimmedLowerPassword.equals(trivialPasswords[i]))
		throw new PasswordException(Description.TRIVIAL_PASSWORD);
	}
    }

}
