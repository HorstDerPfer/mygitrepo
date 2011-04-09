/**
 * 
 */
package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.easy.core.model.EasyPersistentObject;

/**
 * beschreibt eine Maßnahme der Sperrbedarfsliste und nimmt zusätzlich die Zeilennummer für die
 * Ausgabe in der grafischen Darstellung des Streckenbands auf. Beim Zugriff auf ein Streckenband
 * werden vorhandene Zeilen geladen. Sofern weitere Maßnahmen auf dieser Strecke existieren, werden
 * neue <c>StreckenbandZeile</c> Objekte erzeugt.
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "massnahme_strecke_zeile", uniqueConstraints = @UniqueConstraint(columnNames = {
        "strecke_ID", "gleissperrung_ID", "fahrplanjahr" }))
public class StreckenbandZeile extends EasyPersistentObject {

	private static final long serialVersionUID = -1603306344623835073L;

	/**
	 * die zum Streckenband gehörende VZG-Strecke wird redundant zu der VZG-Strecke der Maßnahme
	 * gespeichert
	 */
	@ManyToOne
	private VzgStrecke strecke;

	/**
	 * Zeilennummer der Maßnahme auf dem Streckenband
	 */
	@Column(nullable = false, name = "rowNum_")
	private int rowNum;

	@ManyToOne
	private Gleissperrung gleissperrung;

	@Column(name = "fahrplanjahr", nullable = false)
	private Integer fahrplanjahr;

	public VzgStrecke getStrecke() {
		return strecke;
	}

	public void setStrecke(VzgStrecke strecke) {
		this.strecke = strecke;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int row) {
		this.rowNum = row;
	}

	public Gleissperrung getGleissperrung() {
		return gleissperrung;
	}

	public void setGleissperrung(Gleissperrung gleissperrung) {
		this.gleissperrung = gleissperrung;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(30);

		sb.append("StreckenbandZeile: ");
		sb.append("ID=" + getId() + "; ");
		sb.append("VZGNum=" + getStrecke().getNummer() + "; ");
		sb.append("RowNum=" + getRowNum() + "; ");
		sb.append("\n");

		return sb.toString();

	}

}
