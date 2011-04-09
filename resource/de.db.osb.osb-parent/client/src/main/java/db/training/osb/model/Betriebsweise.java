/**
 * 
 */
package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * die möglichen Betriebsverfahren für die Betriebsführung unter Baubedingungen abgelegt. Zusätzlich
 * werden Daten geführt, die der Plausibilisierung der Betriebsweise bezüglich der örtlichen
 * Ausdehnung und der statistischen Auswertung dienen.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.16 Betriebsweise
 */
@Entity
@Table(name = "betriebsweise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Betriebsweise extends EasyPersistentExpirationObject {

	private static final long serialVersionUID = 5949932573204227040L;

	@Column(name = "kuerzel", length = 16, nullable = false)
	private String kuerzel;

	@Column(name = "la", nullable = false)
	private boolean la;

	@Column(name = "name")
	private String name;

	@Column(name = "stat", nullable = false)
	private boolean statistik;

	@Column(name = "tsp", nullable = false)
	private boolean totalsperrung;

	@Column(name = "zug", nullable = false)
	private boolean zug;

	@Column(name = "zweiBst", nullable = false)
	private boolean zweiBst;

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public boolean isLa() {
		return la;
	}

	public void setLa(boolean la) {
		this.la = la;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * sagt aus, ob die Betriebsweise bei statistischen Auswertungen berücksichtigt werden soll oder
	 * nicht.
	 * 
	 * @return
	 */
	public boolean isStatistik() {
		return statistik;
	}

	/**
	 * legt fest, ob die Betriebsweise bei statistischen Auswertungen berücksichtigt werden soll
	 * oder nicht.
	 * 
	 * @param statistik
	 */
	public void setStatistik(boolean statistik) {
		this.statistik = statistik;
	}

	/**
	 * Wenn die Strecke bei der betreffenden Betriebsweise nicht mehr befahren werden kann, dann ist
	 * dieses Datum <code>true</code>.
	 * 
	 * @return
	 */
	public boolean isTotalsperrung() {
		return totalsperrung;
	}

	/**
	 * @param totalsperrung
	 *            <code>true</code>, wenn die Strecke bei der betreffenden Betriebsweise nicht mehr
	 *            befahren werden kann.
	 */
	public void setTotalsperrung(boolean totalsperrung) {
		this.totalsperrung = totalsperrung;
	}

	/**
	 * @return <code>true</code>, wenn bei der Betriebsweise zwangsläufig Züge betroffen sind
	 */
	public boolean isZug() {
		return zug;
	}

	/**
	 * @param zug
	 *            <code>true</code>, wenn bei der Betriebsweise zwangsläufig Züge betroffen sind
	 */
	public void setZug(boolean zug) {
		this.zug = zug;
	}

	/**
	 * @return <code>true</code>, wenn es sich um eine Betriebsweise um eine Betriebsweise der
	 *         freien Strecke handelt.
	 */
	public boolean isZweiBst() {
		return zweiBst;
	}

	/**
	 * @param zweiBst
	 *            <code>true</code>, wenn es sich um eine Betriebsweise um eine Betriebsweise der
	 *            freien Strecke handelt.
	 */
	public void setZweiBst(boolean zweiBst) {
		this.zweiBst = zweiBst;
	}

	@Override
	public String toString() {
		return String.format("[%s-%s] %s", id, kuerzel, name);
	}
}