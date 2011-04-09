package db.training.security.password;

import java.util.Random;

/**
 * Erstellt Default Kennwoerter fuer die Anwendung
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class PasswordGenerator {
    private static String chars = "abcdefghijlkmnopqrstuvwxyz";

    private static String digits = "0123456789";

    private static String charOrDigit = chars + digits;

    private static String specialChars = "-_()+-=?{}";

    /**
         * erstellt ein 6 stelliges Kennwort, aus zufaelligen Zahlen und Buchstaben Struktur:
         * 
         * <pre>
         * 	1. Zeichen Buchstabe oder Ziffer,
         *  2. Zeichen Buchstabe,
         *  3. Zeichen Ziffer oder Sonderzeichen,
         *  4. Zeichen Ziffer oder Sonderzeichen,
         *  5. Zeichen Buchstabe Gross oder klein
         *  6. Zeichen Buchstabe grossschreibung wenn 5 klein ist, sonst klein,
         * 
         * </pre>
         * 
         * @return
         */
    public static String createNewPassword() {
	Random r = new Random(System.currentTimeMillis());

	StringBuffer password = new StringBuffer();

	int random = r.nextInt(charOrDigit.length());
	password.append(charOrDigit.substring(random, random + 1));

	random = r.nextInt(chars.length());
	password.append(chars.substring(random, random + 1));

	random = r.nextInt(digits.length());
	password.append(digits.substring(random, random + 1));

	random = r.nextInt(specialChars.length());
	password.append(specialChars.substring(random, random + 1));

	boolean firstUpper = r.nextBoolean();
	random = r.nextInt(chars.length());
	password.append(firstUpper ? chars.substring(random, random + 1).toUpperCase() : chars
		.substring(random, random + 1));

	random = r.nextInt(chars.length());
	password.append(firstUpper ? chars.substring(random, random + 1) : chars.substring(random,
		random + 1).toUpperCase());

	return password.toString();
    }
}
