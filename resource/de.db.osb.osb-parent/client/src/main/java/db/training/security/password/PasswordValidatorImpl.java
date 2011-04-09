package db.training.security.password;

import java.util.ArrayList;
import java.util.List;

import db.training.security.User;
import db.training.security.password.rules.LengthRule;
import db.training.security.password.rules.NoReusedPasswordRule;
import db.training.security.password.rules.NotEmptyRule;
import db.training.security.password.rules.PasswordRule;
import db.training.security.password.rules.TrivialPasswordRule;
import db.training.security.password.rules.UsernameNotAllowedRule;

/**
 * Validiert ein Kennwort anhand der Bahnrichtlinien Es koennen beliebige Richtlinien ergaenzt
 * werden.
 * 
 * @author hennebrueder
 * 
 */
public class PasswordValidatorImpl implements PasswordValidator {
	private List<PasswordRule> rules = new ArrayList<PasswordRule>();

	public PasswordValidatorImpl() {
		rules.add(new NotEmptyRule());
		rules.add(new LengthRule());
		// Vom Auftraggeber nicht gewünscht
		// rules.add(new RequireSpecialCharacterNumberRule());
		// rules.add(new RequireUpperLowerRule());
		rules.add(new TrivialPasswordRule());
		rules.add(new UsernameNotAllowedRule());
		rules.add(new NoReusedPasswordRule());
	}

	public PasswordValidatorImpl(List<PasswordRule> rules) {
		super();
		this.rules = rules;
	}


    /* (non-Javadoc)
     * @see db.training.security.PasswordValidator#validate(db.training.security.User, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see db.training.security.PasswordValidator#validate(db.training.security.User, java.lang.String, java.lang.String)
     */
    public void validate(User user, String newPassword,
	    String newHashedPassword) throws PasswordException {
		for (PasswordRule rule : rules) {
		    rule.validate(user.getUsername(), user.getPasswordHistory(), newPassword, newHashedPassword);
		}
    }

	public void setRules(List<PasswordRule> rules) {
		this.rules = rules;
	}
}
