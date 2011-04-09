package db.training.security.password;

import db.training.security.User;

public interface PasswordValidator {

    /* (non-Javadoc)
     * @see db.training.security.PasswordValidator#validate(db.training.security.User, java.lang.String, java.lang.String)
     */
    public abstract void validate(User user, String newPassword,
	    String newHashedPassword) throws PasswordException;

}