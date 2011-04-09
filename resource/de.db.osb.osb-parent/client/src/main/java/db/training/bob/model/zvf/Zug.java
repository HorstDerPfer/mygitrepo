package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import db.training.bob.model.Regionalbereich;
import db.training.bob.model.zvf.helper.DateAdapter;
import db.training.bob.model.zvf.helper.EinsNullLeerAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "firstbst", "tageswechsel", "regelweg", "bemerkung", "kvProfil",
        "streckenKlasse", "bza", "abweichung", "zugdetails", "knotenzeiten", "qs_ks" })
@Entity
@Table(name = "zug")
public class Zug extends EasyPersistentObject implements Comparable<Zug> {

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "bedarf")
	protected Boolean bedarf;

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	@Column(name = "verkehrstag")
	@Temporal(TemporalType.DATE)
	protected Date verkehrstag;

	@XmlAttribute(required = true)
	@Column(name = "zugnr")
	protected Integer zugnr;

	@XmlAttribute(required = true)
	@Column(name = "zugbez", length = 10)
	protected String zugbez;

	@XmlAttribute(required = true)
	@Column(name = "betreiber", length = 5)
	protected String betreiber; // EVU

	@XmlTransient
	@Column(name = "laufendenr")
	private Integer laufendeNr;

	@XmlElement(required = true)
	@Column(name = "firstbst")
	protected String firstbst;

	@XmlElement(required = false)
	@Column(name = "tageswechsel", length = 2)
	protected String tageswechsel;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY)
	@Cascade( { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	protected Regelweg regelweg;

	@XmlElement(required = true)
	@Column(name = "bemerkung")
	protected String bemerkung;

	@XmlElement(name = "klv")
	@Column(name = "kvprofil", length = 20)
	protected String kvProfil;

	@XmlElement(name = "skl")
	@Column(name = "streckenklasse", length = 20)
	protected String streckenKlasse;
	
	@XmlElement(name = "bza")
	@Column(name = "bza")
	protected String bza;

	@XmlElement(required = true)
	@Embedded
	protected Abweichung abweichung;

	@XmlElement(required = true)
	@Embedded
	protected Zugdetails zugdetails;

	@XmlElement(name = "knotenzeit")
	@XmlElementWrapper(name = "knotenzeiten")
	@IndexColumn(name = "nr")
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade( { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	protected List<Knotenzeit> knotenzeiten;

	@XmlElement(name = "qs_ks")
	@Column(name = "qs_ks")
	// 0=nicht aktiv, 1=QS, 2=KS
	protected Integer qs_ks;

	@XmlTransient
	@CollectionOfElements(fetch = FetchType.LAZY)
	private Map<Regionalbereich, Boolean> bearbeitungsStatusMap;

	@XmlTransient
	@Column(name = "richtung")
	// false = 1, true = 2
	protected Boolean richtung;

	public Zug() {
		this.regelweg = new Regelweg();
		this.abweichung = new Abweichung();
		this.zugdetails = new Zugdetails();
		// this.knotenzeiten = new ArrayList<Knotenzeit>();
		// this.bearbeitungsStatusMap = new HashMap<Regionalbereich, Boolean>();
	}

	public void setTeilbearbeitet(Regionalbereich rb, boolean status) {
		bearbeitungsStatusMap.put(rb, status);
	}

	public boolean isFertigBearbeitet() {
		Iterator<Boolean> itValues = bearbeitungsStatusMap.values().iterator();
		while (itValues.hasNext()) {
			if (itValues.next() == false)
				return false;
		}
		return true;
	}

	public String getFirstbst() {
		return firstbst;
	}

	public void setFirstbst(String value) {
		this.firstbst = value;
	}

	public String getTageswechsel() {
		return tageswechsel;
	}

	public void setTageswechsel(String value) {
		this.tageswechsel = value;
	}

	public Regelweg getRegelweg() {
		return regelweg;
	}

	public void setRegelweg(Regelweg value) {
		this.regelweg = value;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public Abweichung getAbweichung() {
		return abweichung;
	}

	public void setAbweichung(Abweichung value) {
		this.abweichung = value;
	}

	public Zugdetails getZugdetails() {
		if (zugdetails == null)
			return new Zugdetails();
		return zugdetails;
	}

	public void setZugdetails(Zugdetails value) {
		this.zugdetails = value;
	}

	public Integer getZugnr() {
		return zugnr;
	}

	public void setZugnr(Integer value) {
		this.zugnr = value;
	}

	public String getZugbez() {
		return zugbez;
	}

	public void setZugbez(String value) {
		this.zugbez = value;
	}

	public Date getVerkehrstag() {
		return verkehrstag;
	}

	public void setVerkehrstag(Date value) {
		this.verkehrstag = value;
	}

	public Boolean getBedarf() {
		return bedarf;
	}

	public void setBedarf(Boolean value) {
		if (value == null)
			this.bedarf = false;
		this.bedarf = value;
	}

	public int compareTo(Zug o) {
		if (laufendeNr == null & o.laufendeNr == null)
			return 0;
		if (laufendeNr == null)
			return -1;
		if (o.laufendeNr == null)
			return 1;
		if (laufendeNr < o.laufendeNr)
			return -1;
		if (laufendeNr > o.laufendeNr)
			return 1;
		return 0;
	}

	public void setLaufendeNr(Integer laufendeNr) {
		this.laufendeNr = laufendeNr;
	}

	public Integer getLaufendeNr() {
		return laufendeNr;
	}

	public String getBetreiber() {
		return betreiber;
	}

	public void setBetreiber(String betreiber) {
		this.betreiber = betreiber;
	}

	public List<Knotenzeit> getKnotenzeiten() {
		if (knotenzeiten == null)
			return new ArrayList<Knotenzeit>();
		return knotenzeiten;
	}

	public void setKnotenzeiten(List<Knotenzeit> knotenzeiten) {
		this.knotenzeiten = knotenzeiten;
	}

	public String getKvProfil() {
		return kvProfil;
	}

	public void setKvProfil(String kvProfil) {
		this.kvProfil = kvProfil;
	}

	public String getStreckenKlasse() {
		return streckenKlasse;
	}

	public void setStreckenKlasse(String streckenKlasse) {
		this.streckenKlasse = streckenKlasse;
	}
	
	public String getBza() {
		return bza;
	}

	public void setBza(String bza) {
		this.bza = bza;
	}

	/**
	 * Gibt eine Map mit den Regionalbereichen (Key) und dazugeh�rigem Bearbeitungsstatus (value)
	 * zur�ck. Der Bearbeitungsstatus hat die Auspr�gungen <code>true</code> f�r "teilbearbeitet"
	 * und <code>false</code> f�r unbearbeitet.
	 * 
	 * @return
	 */
	public Map<Regionalbereich, Boolean> getBearbeitungsStatusMap() {
		return bearbeitungsStatusMap;
	}

	public void setBearbeitungsStatusMap(Map<Regionalbereich, Boolean> bearbeitungsStatusMap) {
		this.bearbeitungsStatusMap = bearbeitungsStatusMap;
	}

	public Integer getQs_ks() {
		if (qs_ks == null)
			return 0;
		return qs_ks;
	}

	public void setQs_ks(Integer qs_ks) {
		if (qs_ks == null)
			this.qs_ks = 0;
		this.qs_ks = qs_ks;
	}

	public Boolean getRichtung() {
		if (richtung == null)
			return false;
		return richtung;
	}

	public void setRichtung(Boolean richtung) {
		if (richtung == null)
			this.richtung = false;
		this.richtung = richtung;
	}

}
