package db.training.bob.model.fplo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zugcharakteristik")
public class Zugcharakteristik extends EasyPersistentObject implements Historizable {

	@ManyToOne(fetch = FetchType.EAGER)
	private Regionalbereich regionalBereichFpl;

	@Column(name = "Zugnummer")
	private Integer zugnr;

	@Column(name = "Kundennummer", length = 6)
	private String kundennummer;

	@Column(name = "Verkehrstag")
	@Temporal(TemporalType.DATE)
	private Date verkehrstag;

	@Column(name = "Gattungsnummer", length = 5)
	private String gattungsnr;

	@Column(name = "Gattungsbezeichnung", length = 15)
	private String gattungsbezeichnung;

	@Column(name = "StartBf", length = 6)
	private String startBf;

	@Column(name = "ZielBf", length = 6)
	private String zielBf;

	// TODO Datentyp und Wertebereich?
	@Column(name = "Tagwechsel")
	private String tagwechsel;

	@Column(name = "DatumKonstruktion")
	@Temporal(TemporalType.DATE)
	private Date datumKonstruktion;

	@Column(name = "IdZug", unique = true, nullable = false)
	// PK aus ISA
	private Integer idZug;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "zugcharakteristik_ID", nullable = false)
	@Cascade( { CascadeType.DELETE_ORPHAN, CascadeType.SAVE_UPDATE })
	private Set<Fahrplan> fahrplan = new HashSet<Fahrplan>();

	public Regionalbereich getRegionalBereichFpl() {
		return regionalBereichFpl;
	}

	public void setRegionalBereichFpl(Regionalbereich regionalBereichFpl) {
		this.regionalBereichFpl = regionalBereichFpl;
	}

	public Integer getZugnr() {
		return zugnr;
	}

	public void setZugnr(Integer zugnr) {
		this.zugnr = zugnr;
	}

	public String getKundennummer() {
		return kundennummer;
	}

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	public Date getVerkehrstag() {
		return verkehrstag;
	}

	public void setVerkehrstag(Date verkehrstag) {
		this.verkehrstag = verkehrstag;
	}

	public String getGattungsnr() {
		return gattungsnr;
	}

	public void setGattungsnr(String gattungsnr) {
		this.gattungsnr = gattungsnr;
	}

	public String getGattungsbezeichnung() {
		return gattungsbezeichnung;
	}

	public void setGattungsbezeichnung(String gattungsbezeichnung) {
		this.gattungsbezeichnung = gattungsbezeichnung;
	}

	public String getStartBf() {
		return startBf;
	}

	public void setStartBf(String startBf) {
		this.startBf = startBf;
	}

	public String getZielBf() {
		return zielBf;
	}

	public void setZielBf(String zielBf) {
		this.zielBf = zielBf;
	}

	public void setTagwechsel(String tagwechsel) {
		this.tagwechsel = tagwechsel;
	}

	public String getTagwechsel() {
		return tagwechsel;
	}

	public void setDatumKonstruktion(Date datumKonstruktion) {
		this.datumKonstruktion = datumKonstruktion;
	}

	public Date getDatumKonstruktion() {
		return datumKonstruktion;
	}

	public void setIdZug(Integer idZug) {
		this.idZug = idZug;
	}

	public Integer getIdZug() {
		return idZug;
	}

	public void setFahrplan(Set<Fahrplan> fahrplan) {
		this.fahrplan = fahrplan;
	}

	public Set<Fahrplan> getFahrplan() {
		return fahrplan;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((idZug == null) ? 0 : idZug.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// Weil Hibernate für Lazy-Initialization Proxy Klassen generiert, reicht
		// if (getClass() != obj.getClass())
		// nicht aus. Hibernate.getClass() liefert "Proxy-freie" Klassen
		if (Hibernate.getClass(this) != Hibernate.getClass(obj))
			return false;
		final Zugcharakteristik other = (Zugcharakteristik) obj;
		if (idZug == null) {
			if (other.idZug != null)
				return false;
		} else if (!idZug.equals(other.idZug))
			return false;
		return true;
	}
}
