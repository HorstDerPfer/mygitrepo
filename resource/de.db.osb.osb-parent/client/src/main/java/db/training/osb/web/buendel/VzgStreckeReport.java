/**
 * 
 */
package db.training.osb.web.buendel;

import java.util.HashSet;
import java.util.TreeSet;

import org.hibernate.Hibernate;

import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.Streckenabschnitt;

/**
 * @author michels
 * 
 */
public class VzgStreckeReport extends VzgStrecke {

	private static final long serialVersionUID = 594385206434651886L;

	private Betriebsstelle firstBst;

	private Betriebsstelle lastBst;

	public VzgStreckeReport(VzgStrecke vzgStrecke) {
		super();

		// EasyPersistentObject
		this.setId(vzgStrecke.getId());

		// EasyPersistentExpirationObject
		this.setGueltigVon(vzgStrecke.getGueltigVon());
		this.setGueltigBis(vzgStrecke.getGueltigBis());

		if (vzgStrecke != null) {
			this.setName(vzgStrecke.getName());
			this.setBeschreibung(vzgStrecke.getBeschreibung());
			this.setNummer(vzgStrecke.getNummer());
			this.setKmBeginn(vzgStrecke.getKmBeginn());
			this.setKmEnde(vzgStrecke.getKmEnde());

			if (Hibernate.isInitialized(vzgStrecke.getAbschnitte())
			    && vzgStrecke.getAbschnitte() != null)
				this.setAbschnitte(new HashSet<Streckenabschnitt>(vzgStrecke.getAbschnitte()));

			if (Hibernate.isInitialized(vzgStrecke.getBetriebsstellen())
			    && vzgStrecke.getBetriebsstellen() != null)
				this.setBetriebsstellen(new TreeSet<BetriebsstelleVzgStreckeLink>(vzgStrecke
				    .getBetriebsstellen()));
		}
	}

	public void setFirstBst(Betriebsstelle firstBst) {
		this.firstBst = firstBst;
	}

	@Override
	public Betriebsstelle getFirstBst() {
		if (firstBst != null)
			return firstBst;
		else if (getBetriebsstellen() != null && getBetriebsstellen().size() > 0)
			return getBetriebsstellen().first().getBetriebsstelle();
		return null;
	}

	public void setLastBst(Betriebsstelle lastBst) {
		this.lastBst = lastBst;
	}

	@Override
	public Betriebsstelle getLastBst() {
		if (lastBst != null)
			return lastBst;
		else if (getBetriebsstellen() != null && getBetriebsstellen().size() > 0)
			return getBetriebsstellen().last().getBetriebsstelle();
		return null;
	}

	@Override
	public String toString() {
		return String.format("VzgStreckeReport: Nr=%s", id);
	}
}
