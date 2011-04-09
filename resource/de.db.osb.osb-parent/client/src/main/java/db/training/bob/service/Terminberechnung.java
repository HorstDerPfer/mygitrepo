package db.training.bob.service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Meilenstein;
import db.training.bob.model.StatusType;
import db.training.bob.model.TerminUebersichtEVU;
import db.training.bob.util.DateUtils;

public class Terminberechnung {

	public static Date calculateSolltermin(Date baubeginn, Meilenstein meilenstein) {
		return getTermin(baubeginn, meilenstein.getAnzahlWochenVorBaubeginn(), meilenstein
		    .getWochentag(), meilenstein.isMindestfrist());
	}

	private static Date getTermin(Date starttermin, int weeksBefore, int day, boolean mindestFrist) {
		GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
		GregorianCalendar result = null;
		calendar.setTime(starttermin);
		calendar = DateUtils.subtractWeeks(calendar, weeksBefore);
		if (day > 0 && day <= 7) {
			result = DateUtils.getDayOfWeek(calendar, day);
		} else {
			int currentWeekday = calendar.get(GregorianCalendar.DAY_OF_WEEK);
			if (currentWeekday == GregorianCalendar.SATURDAY
			    || currentWeekday == GregorianCalendar.SUNDAY)
				result = DateUtils.getDayOfWeek(calendar, GregorianCalendar.FRIDAY);
			else
				result = calendar;

		}
		if (mindestFrist == true && result.after(calendar))
			result = DateUtils.subtractWeeks(result, 1);
		return result.getTime();
	}

	public static StatusType getStatus(Date soll, Date ist, Boolean isErforderlich) {
		Date now = new Date();

		if ((isErforderlich != null && !isErforderlich) || soll == null) {
			return StatusType.NEUTRAL;
		}

		// keine Bearbeitung
		if (ist == null) {
			if (!soll.before(now)) {
				if (DateUtils.getDateDiffInDays(now, soll) <= 14) {
					// weniger als 14 Tage verbleibend bis zum Termin
					return StatusType.COUNTDOWN14;
				}

				// Termin nicht überschritten
				return StatusType.OFFEN;

			} else {
				// Termin überschritten
				return StatusType.LILA;
			}
		}

		// Bearbeitung ist erfolgt
		if (soll.before(ist)) {
			// Termin überschritten
			return StatusType.RED;
		}

		// Bearbeitung pünktlich
		return StatusType.GREEN;
	}

	public static Baumassnahme refreshSolltermine(Baumassnahme baumassnahme) {
		if (baumassnahme == null)
			return null;

		Date beginnDatum = baumassnahme.getBeginnFuerTerminberechnung();
		Art bmArt = baumassnahme.getArt();

		if (baumassnahme.getBaubetriebsplanung() != null)
			baumassnahme.getBaubetriebsplanung().setSollTermine(beginnDatum, bmArt);

		for (TerminUebersichtEVU evu : baumassnahme.getGevus()) {
			evu.setSollTermine(beginnDatum, bmArt);
		}

		for (TerminUebersichtEVU evu : baumassnahme.getPevus()) {
			evu.setSollTermine(beginnDatum, bmArt);
		}

		return baumassnahme;
	}
}
