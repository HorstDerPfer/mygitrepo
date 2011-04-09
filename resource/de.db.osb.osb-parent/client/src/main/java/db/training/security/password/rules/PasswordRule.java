package db.training.security.password.rules;

import java.util.List;

import db.training.security.password.PasswordException;

public interface PasswordRule {

    /**
         * Implementiert eine Kennwortregel der Deutschen Bahn. Wirf eine Exception, wenn das
         * Kennwort nicht der Regel entspricht. Die Exception enthaelt ein Attribut
         * {@code description}, das eine Beschreibung der Ursache enthaelt.
         * 
         * @param username
         * @param passwordHistory
         * @param newPassword Klartextkennwort
         * @param newHashedPassword Das gehaeschte Kennwort.
         * @throws PasswordException
         */
    public void validate(String username, List<String> passwordHistory, String newPassword, String newHashedPassword) throws PasswordException;

}
