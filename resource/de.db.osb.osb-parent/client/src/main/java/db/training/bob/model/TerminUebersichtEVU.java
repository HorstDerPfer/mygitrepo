package db.training.bob.model;

import java.util.Date;

public interface TerminUebersichtEVU {
	public EVUGruppe getEvuGruppe();

	public void setEvuGruppe(EVUGruppe evugruppe);

	/**
	 * aktualisiert die TerminStatus-Properties basierend auf den SOLL/IST-Terminen
	 */
	public void setTerminStatus();

	/**
	 * berechnet die SOLL-Termine abhängig vom Startdatum der Baumaßnahme und der Maßnahmenart neu
	 * wenn der Terminkette ein EVU zugeordnet ist und der jeweilige Termin noch nicht gesetzt
	 * wurde.
	 * 
	 * @param beginnDatum
	 * @param baumassnahmeArt
	 */
	public void setSollTermine(Date beginnDatum, Art baumassnahmeArt);
}
