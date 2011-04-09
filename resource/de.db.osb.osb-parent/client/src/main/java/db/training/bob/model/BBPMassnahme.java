package db.training.bob.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "bbpmassnahme")
public class BBPMassnahme extends EasyPersistentObject implements Historizable,
		Comparable<BBPMassnahme> {

	@Column(name = "masid")
	private String masId;

	@Column(name = "bstvonlang")
	private String bstVonLang;

	@Column(name = "bstbislang")
	private String bstBisLang;

	@Column(name = "beginn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginn;

	@Column(name = "ende")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ende;

	@Column(name = "arbeiten")
	private String arbeiten;

	@Column(name = "streckebbp", length = 5)
	private String streckeBBP;

	@Column(name = "streckevzg", length = 5)
	private String streckeVZG;

	private String regionalbereich;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	// @Sort(type = SortType.NATURAL)
	private Set<Regelung> regelungen;

	/**
	 * kennzeichnet eine BBP-Massnahme beim Import, true bedeutet, dass alle
	 * enthaltenen Regelungen schon einmal importiert wurden
	 */
	@Transient
	private boolean allRegs = false;

	public BBPMassnahme() {
		regelungen = new HashSet<Regelung>();
	}

	public String getMasId() {
		return masId;
	}

	public void setMasId(String masId) {
		this.masId = masId;
	}

	public Set<Regelung> getRegelungen() {
		SortedSet<Regelung> sortedRegs = new TreeSet<Regelung>();
		sortedRegs.addAll(regelungen);
		return sortedRegs;
	}

	public void setRegelungen(Set<Regelung> regelungen) {
		this.regelungen = regelungen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		BBPMassnahme bbpMassnahme = (BBPMassnahme) obj;
		return (masId == bbpMassnahme.masId || (masId != null && masId
				.equals(bbpMassnahme.masId)));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == masId ? 0 : masId.hashCode());
		return hash;
	}

	public String getBstVonLang() {
		return bstVonLang;
	}

	public void setBstVonLang(String bstVonLang) {
		this.bstVonLang = bstVonLang;
	}

	public String getBstBisLang() {
		return bstBisLang;
	}

	public void setBstBisLang(String bstBisLang) {
		this.bstBisLang = bstBisLang;
	}

	public Date getBeginn() {
		return beginn;
	}

	public void setBeginn(Date beginn) {
		this.beginn = beginn;
	}

	public Date getEnde() {
		return ende;
	}

	public void setEnde(Date ende) {
		this.ende = ende;
	}

	public String getArbeiten() {
		return arbeiten;
	}

	public void setArbeiten(String arbeiten) {
		this.arbeiten = arbeiten;
	}

	public String getStreckeBBP() {
		return streckeBBP;
	}

	public void setStreckeBBP(String streckeBBP) {
		this.streckeBBP = streckeBBP;
	}

	public String getStreckeVZG() {
		return streckeVZG;
	}

	public void setStreckeVZG(String streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public String getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(String regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	@Transient
	public Regelung RegelungFuerTerminberechnung() {
		Regelung reg = null;

		for (Regelung regelung : getRegelungen()) {
			if (regelung.getBeginnFuerTerminberechnung() == true) {
				reg = regelung;
			}
		}

		return reg;
	}

	public boolean isAllRegs() {
		return allRegs;
	}

	public void setAllRegs(boolean allRegs) {
		this.allRegs = allRegs;
	}

	public int compareTo(BBPMassnahme other) {
		// compareTo soll 0 zurückgeben, wenn equals true zurück gibt
		if (this.equals(other))
			return 0;

		StringBuilder thisSortCriteria = new StringBuilder();
		thisSortCriteria.append(String.format("%s-%s", FrontendHelper
				.castDateToString(this.getBeginn(), "dd.MM.yyyy HH:mm"),
				FrontendHelper.castDateToString(this.getEnde(),
						"dd.MM.yyy HH:mm")));
		/*
		 * masId als Unterscheidungsmerkmal notwendig, da Zeiträume gleich sein
		 * können, aber BBPMassnahme nach erster Bedingung dieser Methode hier
		 * nicht mehr gleich sein kann. Ohne masId würde bei identischem
		 * Zeitraum 0 zurückgegeben, was bspw. beim Einfügen in ein TreeSet dazu
		 * führt, dass das Element nicht eingefügt wird.
		 */
		thisSortCriteria.append(this.masId.toString());

		StringBuilder otherSortCriteria = new StringBuilder();
		otherSortCriteria.append(String.format("%s-%s", FrontendHelper
				.castDateToString(other.getBeginn(), "dd.MM.yyyy HH:mm"),
				FrontendHelper.castDateToString(other.getEnde(),
						"dd.MM.yyy HH:mm")));
		otherSortCriteria.append(other.masId.toString());

		return thisSortCriteria.toString().compareTo(
				otherSortCriteria.toString());
	}

	public Set<Regelung> getRegelungenUnsorted() {
		return regelungen;
	}
}