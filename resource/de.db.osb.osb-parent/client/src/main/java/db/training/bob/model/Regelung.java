package db.training.bob.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "regelung")
public class Regelung extends EasyPersistentObject implements Historizable, Comparable<Regelung> {

	@Column(name = "regelungId", unique = true)
	private String regelungId;

	@Column(name = "beginn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginn;

	@Column(name = "ende")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ende;

	@Column(name = "betriebsstellevon")
	private String betriebsStelleVon;

	@Column(name = "betriebsstellebis")
	private String betriebsStelleBis;

	@Column(name = "betriebsweise")
	private String betriebsweise;

	@Column(name = "streckebbp", length = 5)
	private String streckeBBP;

	@Column(name = "streckevzg", length = 5)
	private String streckeVZG;

	@Column(name = "sperrkz")
	private SperrKz sperrKz;

	@Column(name = "regelvts")
	private Integer regelVTS;

	@Column(name = "bplarttext")
	private String bplArtText;

	@Column(name = "lisbanr", length = 10)
	private String lisbaNr;

	@Column(name = "bemerkungenbpl")
	private String bemerkungenBpl;

	@Column(name = "isbeginnfuerterminberechnung")
	private boolean beginnFuerTerminberechnung;

	@Transient
	private List<Integer> vorgangsnr;

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

	public String getBetriebsStelleVon() {
		return betriebsStelleVon;
	}

	public void setBetriebsStelleVon(String betriebsStelleVon) {
		this.betriebsStelleVon = betriebsStelleVon;
	}

	public String getBetriebsStelleBis() {
		return betriebsStelleBis;
	}

	public void setBetriebsStelleBis(String betriebsStelleBis) {
		this.betriebsStelleBis = betriebsStelleBis;
	}

	public String getRegelungId() {
		return regelungId;
	}

	public void setRegelungId(String regelungId) {
		this.regelungId = regelungId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Regelung regelung = (Regelung) obj;
		return (regelungId == regelung.regelungId || (regelungId != null && regelungId
		    .equals(regelung.regelungId)));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == regelungId ? 0 : regelungId.hashCode());
		return hash;
	}

	public String getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(String betriebsweise) {
		this.betriebsweise = betriebsweise;
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

	public SperrKz getSperrKz() {
		return sperrKz;
	}

	public void setSperrKz(SperrKz sperrKz) {
		this.sperrKz = sperrKz;
	}

	public Integer getRegelVTS() {
		return regelVTS;
	}

	public void setRegelVTS(Integer regelVTS) {
		this.regelVTS = regelVTS;
	}

	public String getBplArtText() {
		return bplArtText;
	}

	public void setBplArtText(String bplArtText) {
		this.bplArtText = bplArtText;
	}

	public void setLisbaNr(String lisbaNr) {
		this.lisbaNr = lisbaNr;
	}

	public String getLisbaNr() {
		return lisbaNr;
	}

	public String getBemerkungenBpl() {
		return bemerkungenBpl;
	}

	public void setBemerkungenBpl(String bemerkungenBpl) {
		this.bemerkungenBpl = bemerkungenBpl;
	}

	public void setBeginnFuerTerminberechnung(boolean beginnFuerTerminberechnung) {
		this.beginnFuerTerminberechnung = beginnFuerTerminberechnung;
	}

	public boolean getBeginnFuerTerminberechnung() {
		return beginnFuerTerminberechnung;
	}

	public void setVorgangsnr(List<Integer> vorgangsnr) {
		this.vorgangsnr = vorgangsnr;
	}

	public List<Integer> getVorgangsnr() {
		return vorgangsnr;
	}

	public int compareTo(Regelung other) {
		if (this == other)
			return 0;

		return this.getRegelungId().compareTo(other.getRegelungId());
	}
}
