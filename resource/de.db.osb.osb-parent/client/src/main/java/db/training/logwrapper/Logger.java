package db.training.logwrapper;

import de.dbsystems.kolt.talo.TaloLog;
import de.dbsystems.kolt.talo.TaloLogFactory;

/**
 * Logwrapper fuer Talo und log4j wird ueber das Attribut isDevelopment konfiguriert Verwendung:
 * Logger logger = Logger.getLogger(ModelTest.class);
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class Logger {
	private static boolean isDevelopment = true;

	private org.apache.log4j.Logger log4jLog;

	private TaloLog taloLog;

	private Logger() {

	}

	public static Logger getLogger(String name) {
		Logger logger = new Logger();
		if (isDevelopment)
			logger.log4jLog = org.apache.log4j.Logger.getLogger(name);
		else
			logger.taloLog = TaloLogFactory.getLog(name);

		return logger;

	}

	@SuppressWarnings("unchecked")
	public static Logger getLogger(Class clazz) {
		Logger logger = new Logger();
		if (isDevelopment)
			logger.log4jLog = org.apache.log4j.Logger.getLogger(clazz);
		else
			logger.taloLog = TaloLogFactory.getLog(clazz);

		return logger;

	}

	/**
	 * Baut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param message -
	 *            eine oder mehrere Nachrichten
	 */
	public void debug(Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.debug(concetanated);
		else
			taloLog.debug(concetanated);
	}

	/**
	 * aut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param throwable
	 * @param message
	 */
	public void debug(Throwable throwable, Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.debug(concetanated, throwable);
		else
			taloLog.debug(concetanated, throwable);
	}

	/**
	 * Baut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param message -
	 *            eine oder mehrere Nachrichten
	 */
	public void info(Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.info(concetanated);
		else
			taloLog.info(concetanated);
	}

	/**
	 * aut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param throwable
	 * @param message
	 */
	public void info(Throwable throwable, Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.info(concetanated, throwable);
		else
			taloLog.info(concetanated, throwable);
	}

	/**
	 * Baut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param message -
	 *            eine oder mehrere Nachrichten
	 */
	public void warn(Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.warn(concetanated);
		else
			taloLog.warn(concetanated);
	}

	/**
	 * aut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param throwable
	 * @param message
	 */
	public void warn(Throwable throwable, Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.warn(concetanated, throwable);
		else
			taloLog.warn(concetanated, throwable);
	}

	/**
	 * Baut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param message -
	 *            eine oder mehrere Nachrichten
	 */
	public void error(Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.error(concetanated);
		else
			taloLog.error(concetanated);
	}

	/**
	 * aut alle Objekte zu einem String zusammen und logt diese abhaengig von isDevelopment nach
	 * Log4j oder TAlo
	 * 
	 * @param throwable
	 * @param message
	 */
	public void error(Throwable throwable, Object... message) {
		String concetanated = createMessage(message);
		if (isDevelopment)
			log4jLog.error(concetanated, throwable);
		else
			taloLog.error(concetanated, throwable);
	}

	/**
	 * Prüft ob Logger schreibt debug Level Log4j oder TAlo
	 * 
	 * @return - ergebnis
	 * 
	 */
	public boolean isDebugEnabled() {
		if (isDevelopment)
			return log4jLog.isDebugEnabled();
		else
			return taloLog.isDebugEnabled();
	}

	/**
	 * Prüft ob Logger schreibt info Level Log4j oder TAlo
	 * 
	 * @return - ergebnis
	 * 
	 */
	public boolean isInfoEnabled() {
		if (isDevelopment)
			return log4jLog.isInfoEnabled();
		else
			return taloLog.isInfoEnabled();
	}

	/**
	 * Pr�ft ob Logger schreibt error Level Log4j oder TAlo
	 * 
	 * @return - ergebniss
	 * 
	 */
	public boolean isErrorEnabled() {
		if (isDevelopment)
			return log4jLog.isEnabledFor(org.apache.log4j.Level.ERROR);
		else
			return taloLog.isErrorEnabled();
	}

	/**
	 * Pr�ft ob Logger schreibt warn Level Log4j oder TAlo
	 * 
	 * @return - ergebniss
	 * 
	 */
	public boolean isWarnEnabled() {
		if (isDevelopment)
			return log4jLog.isEnabledFor(org.apache.log4j.Level.WARN);
		else
			return taloLog.isWarnEnabled();
	}

	private String createMessage(Object... message) {
		StringBuilder builder = new StringBuilder();
		for (Object object : message) {
			builder.append(object);
			builder.append("  ");
		}
		return builder.toString();
	}

}
