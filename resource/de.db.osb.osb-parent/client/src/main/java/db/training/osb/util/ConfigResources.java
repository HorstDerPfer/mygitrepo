package db.training.osb.util;

import java.util.MissingResourceException;
import org.apache.struts.util.MessageResources;
import db.training.easy.util.FrontendHelper;

/**
 * Sammlung verschiedener Hilfsfunktionen zum Umgang mit Verkehrstageregelungen und
 * Wochentagschluesseln
 * 
 * @author Andreas Plonne
 * 
 */
public class ConfigResources {

	private static ConfigResources instance;

	private MessageResources configRes;

	private ConfigResources() {
		configRes = MessageResources.getMessageResources("ConfigResources");
	}

	/**
	 * 
	 * @return gibt eine Instanz des ConfigResources.Properties zurück
	 */
	public static ConfigResources getInstance() {
		if (instance == null) {
			instance = new ConfigResources();
		}
		return instance;
	}

	/**
	 * gibt den kompletten Link des Gruppenlaufwerks für das angegebene Jahr zurück.
	 * 
	 * @param jahr
	 * @return den Link des Gruppenlaufwerks als String
	 * @throws RuntimeException
	 */
	public String getGruppenlaufwerkLink(Integer jahr) throws RuntimeException {
		if (jahr == null)
			throw new IllegalArgumentException("Parameter Jahr wurde nicht übergeben");
		String link = configRes.getMessage("gruppenlfw." + jahr);
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Link des Gruppenlaufwerks für das angegebene Jahr nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "gruppenlfw." + jahr);
		}
	}

	/**
	 * 
	 * @return gibt den Titel der Anwendung zurück
	 * @throws RuntimeException
	 */
	public String getApplicationTitle() throws RuntimeException {
		String link = configRes.getMessage("application.title");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Titel der Anwendung nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "application.title");
		}
	}

	/**
	 * 
	 * @return gibt die Version der Anwendung zurück
	 * @throws RuntimeException
	 */
	public String getApplicationVersion() throws RuntimeException {
		String link = configRes.getMessage("application.version");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Version der Anwendung nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "application.version");
		}
	}

	/**
	 * 
	 * @return gibt die Url der Anwendung zurück
	 * @throws RuntimeException
	 */
	public String getApplicationUri() throws RuntimeException {
		String link = configRes.getMessage("application.uri");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Uri der Anwendung nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "application.uri");
		}
	}

	/**
	 * 
	 * @return gibt den Absender einer Mail zurück. Bsp.: noreply@bahn.de
	 * @throws RuntimeException
	 */
	public String getAutoMailSender() throws RuntimeException {
		String link = configRes.getMessage("application.automail");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Mail Absender nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "application.automail");
		}
	}

	/**
	 * 
	 * @return gibt den SMTP Port des Mail Servers zurück
	 * @throws RuntimeException
	 */
	public String getSmtpPort() throws RuntimeException {
		String link = configRes.getMessage("mail.smtpPort");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "SMTP Port nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "mail.smtpPort");
		}
	}

	/**
	 * 
	 * @return gibt die Adresse des SMTP Servers zurück
	 * @throws RuntimeException
	 */
	public String getSmtpServer() throws RuntimeException {
		String link = configRes.getMessage("mail.smtpServer");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "SMTP Server nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "mail.smtpServer");
		}
	}

	/**
	 * 
	 * @return gibt den Absender einer Mail zurück. Bsp.: noreply@bahn.de
	 * @throws RuntimeException
	 */
	public String getMailFromAdress() throws RuntimeException {
		String link = configRes.getMessage("mail.fromAddress");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "Absender der Mail nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "mail.fromAddress");
		}
	}

	/**
	 * 
	 * @return gibt das Datumsformat zurück. Bsp.: dd.MM.
	 * @throws RuntimeException
	 */
	public String getDateFormatShort() throws RuntimeException {
		String link = configRes.getMessage("dateFormat.short");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "kurzes Datumsformat nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "dateFormat.short");
		}
	}

	/**
	 * 
	 * @return gibt das Datumsformat zurück. Bsp.: dd.MM.yyyy
	 * @throws RuntimeException
	 */
	public String getDateFormatLong() throws RuntimeException {
		String link = configRes.getMessage("dateFormat.long");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return link;
		} else {
			throw new MissingResourceException(
			    "langes Datumsformat nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "dateFormat.long");
		}
	}

	/**
	 * 
	 * @return gibt die maximale ANZAHL einer KigBauNr zurück
	 * @throws RuntimeException
	 */
	public Integer getMaxAnzahlKigbaunr() throws RuntimeException {
		String link = configRes.getMessage("baumassnahme.anzahl.max.kigbaunr");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return FrontendHelper.castStringToInteger(link);
		} else {
			throw new MissingResourceException(
			    "maximale ANZAHL einer KigBauNr nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "baumassnahme.anzahl.max.kigbaunr");
		}
	}

	/**
	 * 
	 * @return gibt die maximale LAENGE einer KigBauNr zurück
	 * @throws RuntimeException
	 */
	public Integer getMaxLaengeKigbaunr() throws RuntimeException {
		String link = configRes.getMessage("baumassnahme.laenge.max.kigbaunr");
		if (FrontendHelper.stringNotNullOrEmpty(link)) {
			return FrontendHelper.castStringToInteger(link);
		} else {
			throw new MissingResourceException(
			    "maximale LAENGE einer KigBauNr nicht gefunden. ConfigResources überprüfen!",
			    ConfigResources.class.getName(), "baumassnahme.laenge.max.kigbaunr");
		}
	}

	public long getMaxZeitraumControlling() throws MissingResourceException {
		String res = configRes.getMessage("max.zeitraum.controlling");
		if (FrontendHelper.stringNotNullOrEmpty(res)) {
			return Long.parseLong(res);
		}

		throw new MissingResourceException(
		    "max. Suchzeitraum für Arbeitssteuerung nicht definiert.",
		    ConfigResources.class.getName(), "max.zeitraum.controlling");
	}

	public long getMaxZeitraumVerkehrstag() throws MissingResourceException {
		String res = configRes.getMessage("max.zeitraum.verkehrstag");
		if (FrontendHelper.stringNotNullOrEmpty(res)) {
			return Long.parseLong(res);
		}

		throw new MissingResourceException("max. Suchzeitraum für Verkehrstage nicht definiert.",
		    ConfigResources.class.getName(), "max.zeitraum.verkehrstag");
	}
}