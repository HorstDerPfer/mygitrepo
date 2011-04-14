package fm.justintime.model;

import java.util.Date;

import javax.persistence.Entity;

import fm.justintime.framework.hibernate.PersistentObject;

@Entity
public class Event extends PersistentObject {

	/**
	 * Name der Veranstaltung
	 */
	private String name;

	/**
	 * zusätzliche allgemeine Informationen
	 */
	private String description;

	/**
	 * gibt den Veranstaltungstag an
	 */
	private Date date;

	/**
	 * gibt den Zeitpunkt an, ab dem die Voranmeldung freigeschaltet wird
	 */
	private Date beginnOfRegistration;

	/**
	 * gibt den Zeitpunkt an, an dem die Voranmeldung geschlossen wird
	 */
	private Date endOfRegistration;

	/**
	 * gibt den Zeitpunkt an, ab dem die Nachmeldung geschlossen wird
	 */
	private Date endOfLateRegistration;

	/**
	 * Meldegebühr
	 */
	private Float registrationFee;

	/**
	 * Zahlungsweg
	 */
	private Payment[] methodsOfPayment;
}
