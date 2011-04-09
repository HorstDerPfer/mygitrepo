package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import db.training.bob.model.Art;
import db.training.bob.model.zvf.helper.BBPStreckeAdapter;
import db.training.bob.model.zvf.helper.DateAdapter;
import db.training.bob.model.zvf.helper.EinsNullLeerAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "masterniederlassung", "zvfid", "baumassnahmenart", "qsKsVesNr",
        "korridor", "kigbau", "kennung", "extension", "bbp", "vorgangsNr", "festgelegtSPFV",
        "festgelegtSPNV", "festgelegtSGV", "antwort", "baudatevon", "baudatebis", "endStueckZvf",
        "version", "strecke", "kID1", "kID2", "kID3", "kID4", "allgregelungen", "fplonr", "zug" })
@Entity
@Table(name = "massnahme")
public class Massnahme extends EasyPersistentObject {

	@XmlElement(required = true)
	@Column(name = "masterniederlassung", length = 10)
	protected String masterniederlassung;

	@XmlElement(required = true)
	@Column(name = "zvfid", length = 17)
	protected String zvfid;

	@XmlElement(required = true)
	@Column(name = "art")
	protected Art baumassnahmenart;

	@XmlElement(required = true, name = "qsbaumassnahme")
	@Column(name = "qs_ks_ves_nr", length = 255)
	private String qsKsVesNr;

	@XmlElement(required = true)
	@Column(name = "korridor", length = 5)
	protected String korridor;

	@XmlElement(required = true)
	@Column(name = "kigbau", length = 255)
	protected String kigbau;

	@XmlElement(required = true)
	@Column(name = "kennung")
	protected String kennung;

	@XmlElement(required = true)
	@Column(name = "extension")
	protected String extension;

	/**
	 * FetchPlan.ZVF_MN_BBPSTRECKE
	 */
	@XmlElementWrapper(name = "bbpliste")
	@XmlElement(name = "bbp", required = true)
	@XmlJavaTypeAdapter(value = BBPStreckeAdapter.class, type = BBPStrecke.class)
	@ManyToMany(fetch = FetchType.LAZY)
	@IndexColumn(name = "nr", base = 0)
	protected List<BBPStrecke> bbp = new ArrayList<BBPStrecke>();

	@XmlElement(name = "master_fplo", required = true)
	@Column(name = "vorgangsnr")
	private Integer vorgangsNr;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "festgelegt_spfv")
	protected Boolean festgelegtSPFV;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "festgelegt_spnv")
	protected Boolean festgelegtSPNV;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "festgelegt_sgv")
	protected Boolean festgelegtSGV;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	@Column(name = "antwort")
	@Temporal(TemporalType.DATE)
	protected Date antwort;

	@XmlElement(required = false)
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	@Column(name = "baudatevon")
	@Temporal(TemporalType.DATE)
	protected Date baudatevon;

	@XmlElement(required = false)
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	@Column(name = "baudatebis")
	@Temporal(TemporalType.DATE)
	protected Date baudatebis;

	@XmlElement(name = "endStueckZvf")
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "endstueckzvf")
	protected Boolean endStueckZvf;

	/**
	 * FetchPlan.ZVF_MN_VERSION
	 */
	@XmlElement(required = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected Version version;

	/**
	 * FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_STRECKE_STRECKEVZG
	 */
	@XmlElementWrapper(name = "streckenabschnitte")
	@XmlElement(name = "strecke")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@IndexColumn(name = "nr")
	protected List<Strecke> strecke;

	@XmlElement(name = "KID1", required = true)
	@Column(name = "KID1")
	@Embedded
	protected KID kID1;

	@XmlElement(name = "KID2", required = true)
	@Column(name = "KID2")
	protected Integer kID2;

	@XmlElement(name = "KID3", required = true)
	@Column(name = "KID3")
	protected Integer kID3;

	@XmlElementWrapper(name = "KID4Liste")
	@XmlElement(name = "KID4", required = true)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@IndexColumn(name = "nr", base = 0)
	protected List<KID> kID4 = new ArrayList<KID>();

	@XmlElementWrapper(name = "allgregelungen")
	@XmlElement(name = "allgregelung")
	@Lob()
	@CollectionOfElements(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	protected List<String> allgregelungen;

	/**
	 * FetchPlan.UEB_MN_FPLO, FetchPlan.MN_FPLO_NIEDERLASSUNGEN
	 */
	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected Fplonr fplonr;

	@XmlElementWrapper(name = "zuege")
	@XmlElement(name = "zug")
	@IndexColumn(name = "nr")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected List<Zug> zug;

	public Massnahme() {
		festgelegtSGV = false;
		festgelegtSPFV = false;
		festgelegtSPNV = false;
		this.version = new Version();
		this.strecke = new ArrayList<Strecke>();
		this.allgregelungen = new ArrayList<String>();
		this.fplonr = new Fplonr();
		this.zug = new ArrayList<Zug>();
	}

	public String getMasterniederlassung() {
		return masterniederlassung;
	}

	public void setMasterniederlassung(String value) {
		this.masterniederlassung = value;
	}

	public String getZvfid() {
		return zvfid;
	}

	public void setZvfid(String zvfid) {
		this.zvfid = zvfid;
	}

	public Art getBaumassnahmenart() {
		return baumassnahmenart;
	}

	public void setBaumassnahmenart(Art value) {
		this.baumassnahmenart = value;
	}

	public String getKorridor() {
		return korridor;
	}

	public void setKorridor(String korridor) {
		this.korridor = korridor;
	}

	public String getKigbau() {
		return kigbau;
	}

	public void setKigbau(String value) {
		this.kigbau = value;
	}

	public String getKennung() {
		return kennung;
	}

	public void setKennung(String value) {
		this.kennung = value;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public List<BBPStrecke> getBbp() {
		return bbp;
	}

	public void setBbp(List<BBPStrecke> value) {
		this.bbp = value;
	}

	public Integer getVorgangsNr() {
		return vorgangsNr;
	}

	public void setVorgangsNr(Integer vorgangsNr) {
		this.vorgangsNr = vorgangsNr;
	}

	public String getQsKsVesNr() {
		return qsKsVesNr;
	}

	public void setQsKsVesNr(String qsKsVesNr) {
		this.qsKsVesNr = qsKsVesNr;
	}

	public Boolean getFestgelegtSPFV() {
		return festgelegtSPFV;
	}

	public void setFestgelegtSPFV(Boolean festgelegtSPFV) {
		if (festgelegtSPFV == null)
			this.festgelegtSPFV = false;
		this.festgelegtSPFV = festgelegtSPFV;
	}

	public Boolean getFestgelegtSPNV() {
		return festgelegtSPNV;
	}

	public void setFestgelegtSPNV(Boolean festgelegtSPNV) {
		if (festgelegtSPNV == null)
			this.festgelegtSPNV = false;
		this.festgelegtSPNV = festgelegtSPNV;
	}

	public Boolean getFestgelegtSGV() {
		return festgelegtSGV;
	}

	public void setFestgelegtSGV(Boolean festgelegtSGV) {
		if (festgelegtSGV == null)
			this.festgelegtSGV = false;
		this.festgelegtSGV = festgelegtSGV;
	}

	public Date getAntwort() {
		return antwort;
	}

	public void setAntwort(Date value) {
		this.antwort = value;
	}

	public Date getBaudatevon() {
		return baudatevon;
	}

	public void setBaudatevon(Date baudatevon) {
		this.baudatevon = baudatevon;
	}

	public Date getBaudatebis() {
		return baudatebis;
	}

	public void setBaudatebis(Date baudatebis) {
		this.baudatebis = baudatebis;
	}

	public Integer getKID2() {
		return kID2;
	}

	public void setKID2(Integer kid2) {
		kID2 = kid2;
	}

	public Integer getKID3() {
		return kID3;
	}

	public void setKID3(Integer kid3) {
		kID3 = kid3;
	}

	public KID getKID1() {
		return kID1;
	}

	public void setKID1(KID kid1) {
		kID1 = kid1;
	}

	public Boolean getEndStueckZvf() {
		return endStueckZvf;
	}

	public void setEndStueckZvf(Boolean endStueckZvf) {
		this.endStueckZvf = endStueckZvf;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version value) {
		this.version = value;
	}

	public List<Strecke> getStrecke() {
		if (strecke == null)
			return new ArrayList<Strecke>();
		return strecke;
	}

	public void setStrecke(List<Strecke> value) {
		this.strecke = value;
	}

	public List<String> getAllgregelungen() {
		if (allgregelungen == null)
			return new ArrayList<String>();
		return allgregelungen;
	}

	public void setAllgregelungen(List<String> value) {
		this.allgregelungen = value;
	}

	public Fplonr getFplonr() {
		return fplonr;
	}

	public void setFplonr(Fplonr fplonr) {
		this.fplonr = fplonr;
	}

	public List<Zug> getZug() {
		if (zug == null) {
			zug = new ArrayList<Zug>();
		}
		return zug;
	}

	public void setZug(List<Zug> value) {
		if (value == null)
			return;
		Iterator<Zug> it = value.iterator();
		TreeSet<Zug> set = new TreeSet<Zug>();
		Zug z;
		int i = 1;
		while (it.hasNext()) {
			z = it.next();
			z.setLaufendeNr(i++);
			set.add(z);
		}

		this.zug = new ArrayList<Zug>(set);
	}

	public void addZug(Zug z) {
		z.setLaufendeNr(getZug().size() + 1);
		zug.add(z);
	}

	public List<KID> getkID4() {
		return kID4;
	}

	public void setkID4(List<KID> kID4) {
		this.kID4 = kID4;
	}

	/**
	 * Übernimmt Daten aus importierter Massnahme bzw. fügt Daten hinzu. Zugdaten werden nicht
	 * übernommen
	 * 
	 * @param massnahme
	 */
	public void importMassnahme(Massnahme massnahme) {
		this.masterniederlassung = massnahme.masterniederlassung;
		this.baumassnahmenart = massnahme.baumassnahmenart;
		this.korridor = massnahme.korridor;
		this.kigbau = massnahme.kigbau;
		this.kennung = massnahme.kennung;
		this.bbp = massnahme.bbp;
		this.vorgangsNr = massnahme.vorgangsNr;
		this.qsKsVesNr = massnahme.qsKsVesNr;
		this.festgelegtSGV = massnahme.festgelegtSGV;
		this.festgelegtSPFV = massnahme.festgelegtSPFV;
		this.festgelegtSPNV = massnahme.festgelegtSPNV;
		this.antwort = massnahme.antwort;
		this.version = massnahme.version;
		this.strecke.addAll(massnahme.strecke);
		this.allgregelungen.addAll(massnahme.allgregelungen);
		this.fplonr = massnahme.fplonr;
	}

	/**
	 * übernimmt die züge aus der übergebenen Massnahme
	 * 
	 * @param massnahme
	 */
	public void importZuege(Massnahme massnahme) {
		this.zug.addAll(massnahme.getZug());
	}
}
