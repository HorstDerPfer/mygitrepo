package db.training.security.password;

/**
 * Exception, die verwendet wird, wenn eine Kennwortvalidierung nicht erfolgreich war. Das Attribut
 * Description enthaelt einen enum Wert, der die Exception weiter beschreibt.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class PasswordException extends Exception {

    private static final long serialVersionUID = -2502288816724365695L;

    public enum Description {
	UNSAFE_PASSWORD, EMPTY_PASSWORD, TRIVIAL_PASSWORD, REUSED_PASSWORD, PASSWORD_TO_SHORT
    }

    private Description description;

    public PasswordException(Description description) {
	this.description = description;
    }

    public PasswordException(String message, Description description) {
	super(message);
	this.description = description;
    }

    public Description getDescription() {
	return description;
    }

}
