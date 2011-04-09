package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.osb.model.babett.Netzsegment;
import db.training.osb.model.babett.Wirkfaktor;

/**
 * @see Feinkonzept (25.1.10), 7.1.3.9 Verknüpfung Betriebsstelle-Strecke
 * @author michels
 * 
 */
@Entity
@Table(name = "bst_strecke", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "betriebsstelle_ID", "strecke_ID", "gueltig_von", "gueltig_bis" }) })
public class BetriebsstelleVzgStreckeLink extends EasyPersistentExpirationObject implements
    Comparable<BetriebsstelleVzgStreckeLink> {

	private static final long serialVersionUID = -5130708949235905731L;

	@ManyToOne(optional = false)
	private Betriebsstelle betriebsstelle;

	@ManyToOne(optional = false)
	private VzgStrecke strecke;

	@Column(scale = 7, precision = 3)
	private float km;

	@Enumerated(EnumType.STRING)
	private Wirkfaktor wirkfaktor;

	@Enumerated(EnumType.STRING)
	private Netzsegment netzsegment;

	public Betriebsstelle getBetriebsstelle() {
		return betriebsstelle;
	}

	public void setBetriebsstelle(Betriebsstelle bst) {
		this.betriebsstelle = bst;
	}

	public VzgStrecke getStrecke() {
		return strecke;
	}

	public void setStrecke(VzgStrecke strecke) {
		this.strecke = strecke;
	}

	public float getKm() {
		return km;
	}

	public void setKm(float km) {
		this.km = km;
	}

	public Wirkfaktor getWirkfaktor() {
		return wirkfaktor;
	}

	public void setWirkfaktor(Wirkfaktor wirkfaktor) {
		this.wirkfaktor = wirkfaktor;
	}

	public Netzsegment getNetzsegment() {
		return netzsegment;
	}

	public void setNetzsegment(Netzsegment netzsegment) {
		this.netzsegment = netzsegment;
	}

	public String getCompareString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%025.15f", getKm())).append("_");
		if (getStrecke() != null)
			sb.append(getStrecke().getNummer()).append("_");
		if (getBetriebsstelle() != null)
			sb.append(getBetriebsstelle().getKuerzel());

		return sb.toString();
	}

	public int compareTo(BetriebsstelleVzgStreckeLink that) {
		if (getCompareString() == null || that == null || that.getCompareString() == null) {
			return -1;
		}
		return getCompareString().toLowerCase().compareTo(that.getCompareString().toLowerCase());
	}

	public boolean equals(BetriebsstelleVzgStreckeLink that) {
		if (that == null)
			return false;
		else if (getCompareString() == null && that.getCompareString() == null)
			return true;
		else if ((getCompareString() != null || that.getCompareString() == null)
		    || (getCompareString() == null && that.getCompareString() != null))
			return false;
		else if (getCompareString().toLowerCase().compareTo(that.getCompareString().toLowerCase()) == 0)
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return getCompareString() != null ? getCompareString().toLowerCase().hashCode() : 0;
	}

	/**
	 * gibt die Verknüpfung zwischen Betriebsstelle und VzG-Strecke zurück
	 * 
	 * @param bst
	 * @param strecke
	 * @return
	 */
	public static BetriebsstelleVzgStreckeLink getLink(Betriebsstelle bst, VzgStrecke strecke) {
		if (bst == null || strecke == null)
			return null;

		for (BetriebsstelleVzgStreckeLink link : bst.getStrecken()) {
			if (link.getStrecke().equals(strecke))
				return link;
		}

		return null;
	}
}
