package db.training.bob.model.fplo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_ZUG")
public class ISA_Zug implements Serializable {

	@Id
	@Column(name = "ID_ZUG", nullable = false)
	private Integer idZug;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_BESTELLUNG")
	private ISA_Bestellung bestellung;// where

	@Column(name = "ZUGNUMMER", length = 50)
	private String zugnummer;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATUM_KONSTRUKTION", length = 7)
	private Date datumKonstruktion;

	@Column(name = "FERTIG", nullable = false)
	private boolean fertig; // where

	@Column(name = "TAGWECHSEL", length = 250)
	private String tagwechsel;

	@Temporal(TemporalType.DATE)
	@Column(name = "ERSTERVTAG", length = 7)
	private Date erstervtag;

	@Column(name = "STARTBF", length = 10)
	private String startbf;

	@Column(name = "ZIELBF", length = 10)
	private String zielbf;

	@Column(name = "GATTUNG", length = 50)
	private String gattung;

	@Column(name = "B2", nullable = false)
	private boolean b2;// where

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ZUG")
	private Set<ISA_Fahrplan> fahrplan;

	public ISA_Zug() {
	}

	public Integer getIdZug() {
		return this.idZug;
	}

	public void setIdZug(Integer idZug) {
		this.idZug = idZug;
	}

	public void setBestellung(ISA_Bestellung bestellung) {
		this.bestellung = bestellung;
	}

	public ISA_Bestellung getBestellung() {
		return bestellung;
	}

	// public Integer getIdBestellung() {
	// return this.idBestellung;
	// }
	//
	// public void setIdBestellung(Integer idBestellung) {
	// this.idBestellung = idBestellung;
	// }

	public String getZugnummer() {
		return this.zugnummer;
	}

	public void setZugnummer(String zugnummer) {
		this.zugnummer = zugnummer;
	}

	public Date getDatumKonstruktion() {
		return this.datumKonstruktion;
	}

	public void setDatumKonstruktion(Date datumKonstruktion) {
		this.datumKonstruktion = datumKonstruktion;
	}

	public boolean isFertig() {
		return this.fertig;
	}

	public void setFertig(boolean fertig) {
		this.fertig = fertig;
	}

	public String getTagwechsel() {
		return this.tagwechsel;
	}

	public void setTagwechsel(String tagwechsel) {
		this.tagwechsel = tagwechsel;
	}

	public Date getErstervtag() {
		return this.erstervtag;
	}

	public void setErstervtag(Date erstervtag) {
		this.erstervtag = erstervtag;
	}

	public String getStartbf() {
		return this.startbf;
	}

	public void setStartbf(String startbf) {
		this.startbf = startbf;
	}

	public String getZielbf() {
		return this.zielbf;
	}

	public void setZielbf(String zielbf) {
		this.zielbf = zielbf;
	}

	public String getGattung() {
		return this.gattung;
	}

	public void setGattung(String gattung) {
		this.gattung = gattung;
	}

	public boolean isB2() {
		return this.b2;
	}

	public void setB2(boolean b2) {
		this.b2 = b2;
	}

	public void setFahrplan(Set<ISA_Fahrplan> fahrplan) {
		this.fahrplan = fahrplan;
	}

	public Set<ISA_Fahrplan> getFahrplan() {
		return fahrplan;
	}

}
