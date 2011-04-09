/**
 * 
 */
package db.training.osb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.osb.service.StreckenbandService;

/**
 * @author michels
 * 
 */
@SuppressWarnings("serial")
public class Streckenband extends ArrayList<StreckenbandZeile> {

	private static Logger log = Logger.getLogger(Streckenband.class);

	public Streckenband() {
		super();
	}

	public Streckenband(Collection<? extends StreckenbandZeile> c) {
		super(c);
	}

	public Streckenband(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * entfernt die Zeile, die die angegebene Gleissperrung enthält aus dem Streckenband und
	 * nummieriert alle Einträge neu.<br />
	 * <em>Streckenband wird automatisch gespeichert.</em>
	 * 
	 * @param gleissperrung
	 * @see StreckenbandService
	 */
	public void delete(Gleissperrung gleissperrung) {
		if (gleissperrung == null) {
			return;
		}
		StreckenbandService service = EasyServiceFactory.getInstance().createStreckenbandService();

		for (ListIterator<StreckenbandZeile> iter = this.listIterator(); iter.hasNext();) {
			int i = iter.nextIndex();
			StreckenbandZeile row = iter.next();

			// Nummerierung anpassen
			row.setRowNum(i);

			if (row.getGleissperrung().equals(gleissperrung)) {
				// Zeile von Streckenband löschen
				iter.remove();
				service.delete(row);
			}
		}

		service.update(this);
	}

	/**
	 * fügt eine neue Gleissperrung am Ende des Streckenbandes (höchste ID) hinzu.<br />
	 * <em>Wird nicht automatisch in DB gespeichert.</em>
	 * 
	 * @param massnahme
	 */
	public void add(Gleissperrung gleissperrung) {
		if (log.isDebugEnabled())
			log.debug("add(Gleissperrung)");

		if (gleissperrung == null)
			return;

		StreckenbandZeile row = new StreckenbandZeile();
		VzgStrecke strecke = null;
		boolean isNew = false;
		if (this.size() == 0) {
			isNew = true;
			strecke = gleissperrung.getVzgStrecke();
		} else
			strecke = this.get(0).getStrecke();

		// neue Zeile initialisieren
		row.setGleissperrung(gleissperrung);
		row.setRowNum(this.size());
		row.setStrecke(strecke);

		// an Streckenband anfügen
		this.add(row);
		if (isNew) {
			StreckenbandService service = EasyServiceFactory.getInstance()
			    .createStreckenbandService();
			service.update(this);
		}
	}
}
