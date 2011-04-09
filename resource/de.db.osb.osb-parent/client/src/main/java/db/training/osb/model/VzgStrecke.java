package db.training.osb.model;

import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.Streckenabschnitt;
import de.dbsystems.kolt.talo.TaloLogFactory;

@SuppressWarnings("serial")
@Entity
@Table(name = "strecke")
public class VzgStrecke extends EasyPersistentExpirationObject implements Historizable,
    Comparable<VzgStrecke> {

	@OneToMany(mappedBy = "vzgStrecke")
	private Set<Streckenabschnitt> abschnitte;

	@Column(length = 64)
	private String name;

	@Column(length = 255)
	private String beschreibung;

	@Column(name = "nummer", unique = true)
	private Integer nummer;

	@Column(name = "nummer", insertable = false, updatable = false)
	private String nummerAsString;

	@Column(name = "beginn_km", scale = 3, precision = 10, nullable = false, columnDefinition = "float default 0")
	private float kmBeginn;

	@Column(name = "ende_km", scale = 3, precision = 10, nullable = false, columnDefinition = "float default 0")
	private float kmEnde;

	@OneToMany(mappedBy = "strecke")
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	@Sort(type = SortType.NATURAL)
	private SortedSet<BetriebsstelleVzgStreckeLink> betriebsstellen;

	public String getNummerAsString() {
		return nummerAsString;
	}

	public VzgStrecke() {
	}

	public VzgStrecke(Integer nummer) {
		this.nummer = nummer;
	}

	public Set<Streckenabschnitt> getAbschnitte() {
		return abschnitte;
	}

	public void setAbschnitte(Set<Streckenabschnitt> abschnitte) {
		this.abschnitte = abschnitte;
	}

	public String getName() {
		return name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}

	public float getKmBeginn() {
		return kmBeginn;
	}

	public void setKmBeginn(float kmBeginn) {
		this.kmBeginn = kmBeginn;
	}

	public float getKmEnde() {
		return kmEnde;
	}

	public void setKmEnde(float kmEnde) {
		this.kmEnde = kmEnde;
	}

	public Betriebsstelle getFirstBst() {
		if (getBetriebsstellen() != null && getBetriebsstellen().size() > 0)
			return getBetriebsstellen().first().getBetriebsstelle();
		return null;
	}

	public Betriebsstelle getLastBst() {
		if (getBetriebsstellen() != null && getBetriebsstellen().size() > 0)
			return getBetriebsstellen().last().getBetriebsstelle();
		return null;
	}

	public SortedSet<BetriebsstelleVzgStreckeLink> getBetriebsstellen() {
		return betriebsstellen;
	}

	public void setBetriebsstellen(SortedSet<BetriebsstelleVzgStreckeLink> betriebsstellen) {
		this.betriebsstellen = betriebsstellen;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();

		try {
			if (nummer != null) {
				caption.append("[").append(nummer).append("]");
			}

			// Betriebsstellen muessen separat gefuellt werden
			if (getFirstBst() != null && getLastBst() != null) {
				caption.append(" ");
				caption.append(getFirstBst().getName());
				caption.append(" - ");
				caption.append(getLastBst().getName());
			}
		} catch (Exception ex) {
			TaloLogFactory.getLog(VzgStrecke.class).debug(
			    "VzgStrecke.getCaption( id = " + id + "; nummer = " + nummer, ex);
		}
		return caption.toString();
	}

	public static Integer getId(String caption) {
		if (caption == null) {
			return null;
		}
		if (caption.indexOf("[") > -1 && caption.indexOf("]") > -1) {
			String idString = caption.substring(caption.indexOf("[") + 1, caption.lastIndexOf("]"));
			if (idString == null || idString.isEmpty())
				return null;

			return Integer.parseInt(idString);
		}
		return FrontendHelper.castStringToInteger(caption);
	}

	public int compareTo(VzgStrecke other) {
		if (this == other)
			return 0;

		if (this.getNummer() < other.getNummer())
			return -1;
		else if (this.getNummer() > other.getNummer())
			return 1;

		return 0;
	}
}