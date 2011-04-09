/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * dient der Ablage der Versionsinformationen der Kundeninformation. Über diese Komponente ist
 * sowohl eine Versionierung innerhalb eines Fahrplanjahrs (aufsteigende Versionsnummer bei gleichem
 * Fahrplanjahr) als auch fahrplanjahrübergreifend (Änderung des Fahrplanjahrs möglich). Bei Anlage
 * einer neuen Version werden alle der Version zugeordneten <code>Master</code> und deren
 * nachgeordnete Dokumente für die Bearbeitung wieder freigegeben, d. h. die Attribute
 * "Arbeitsschluss_KBB" und "Arbeitsschluss_KBBT" in der Fachklasse <code>Master</code> bezüglich
 * der Version auf <code>false</code> gesetzt (Historisierung).
 * 
 * @author michels
 * 
 */
@Entity(name = "KDInfo_Version")
@Table(name = "kdinfo_version")
@SuppressWarnings("serial")
public class Version extends EasyPersistentExpirationObject {

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date erstellDatum;

	@Column(nullable = false)
	private int fahrplanjahr;

	@Column(nullable = false, length = 64)
	private String name;

	@Column(nullable = false)
	private boolean redaktionsschluss;

	@Column(nullable = false)
	private int teil;

	@Column(nullable = false)
	private int versionsnummer;

	@ManyToMany()
	private Set<Master> master;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;
	
	public Version() {
		redaktionsschluss = false;
	}

	public Date getErstellDatum() {
		// Standardwert
		return erstellDatum;
	}

	public void setErstellDatum(Date erstellDatum) {
		this.erstellDatum = erstellDatum;
	}

	public int getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(int fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Kennzeichnet den "Redaktionsschluss" einer Kundeninfo- Version. Default = falsch. Ist
	 *         das Attribut wahr, so können <code>Master</code> und deren nachgeordnete Dokumente in
	 *         der betreffenden Version nicht mehr geändert werden.
	 */
	public boolean isRedaktionsschluss() {
		return redaktionsschluss;
	}

	/**
	 * @param redaktionsschluss
	 *            kennzeichnet den "Redaktionsschluss" einer Kundeninfo- Version. Default = falsch.
	 *            Ist das Attribut wahr, so können <code>Master</code> und deren nachgeordnete
	 *            Dokumente in der betreffenden Version nicht mehr geändert werden.
	 */
	public void setRedaktionsschluss(boolean redaktionsschluss) {
		this.redaktionsschluss = redaktionsschluss;
	}

	/**
	 * @return Teil der Lisba, der bezogen auf das Fahrplanjahr betroffen ist. Es gibt Teil 1
	 *         (01.01. – 31.03.) und Teil 2 (01.04. – 31.12.), außerdem ist Teil = 0 für das
	 *         Gesamtjahr vorzusehen (Intention des Fachdienstes, auf Teile verzichten zu wollen)
	 */
	public int getTeil() {
		return teil;
	}

	/**
	 * @param teil
	 *            Teil der Lisba, der bezogen auf das Fahrplanjahr betroffen ist. Es gibt Teil 1
	 *            (01.01. – 31.03.) und Teil 2 (01.04. – 31.12.), außerdem ist Teil = 0 für das
	 *            Gesamtjahr vorzusehen (Intention des Fachdienstes, auf Teile verzichten zu wollen)
	 */
	public void setTeil(int teil) {
		this.teil = teil;
	}

	/**
	 * @return Versionsnummer bezogen auf Fahrplanjahr und Teil des Lisba-/KiGbau-Kommunikationstyps
	 */
	public int getVersionsnummer() {
		return versionsnummer;
	}

	/**
	 * @param versionsnummer
	 *            Versionsnummer bezogen auf Fahrplanjahr und Teil des
	 *            Lisba-/KiGbau-Kommunikationstyps
	 */
	public void setVersionsnummer(int versionsnummer) {
		this.versionsnummer = versionsnummer;
	}

	public Set<Master> getMaster() {
		return master;
	}

	public void setMaster(Set<Master> master) {
		this.master = master;
	}

	public String getBbpMassnahmeId() {
    	return bbpMassnahmeId;
    }

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
    	this.bbpMassnahmeId = bbpMassnahmeId;
    }

	public Integer getBbpRglNr() {
    	return bbpRglNr;
    }

	public void setBbpRglNr(Integer bbpRglNr) {
    	this.bbpRglNr = bbpRglNr;
    }

}
