/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Oberleitung;

/**
 * Informationen, die für die Interaktion KBB – KBBT benötigt werden
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "kdinfo_master")
@SuppressWarnings("serial")
public class Master extends EasyPersistentObject {

	@ManyToOne()
	private Art art;

	@Column(nullable = false)
	private boolean gestrichen;

	@Column(nullable = false)
	private boolean ks;

	@Column(nullable = false)
	private boolean qs;

	@Column(nullable = false)
	private boolean quib;

	@Column(nullable = false)
	private boolean qp;

	@Column(length = 32)
	private String qiQsKsNummerNfpl;

	@Column(length = 32)
	private String qiQsKsNummerSonst;

	@Column(nullable = false)
	private boolean qsVorschlagKbb;

	@Column()
	@Lob()
	private String bemerkungKbb;

	@Column()
	@Lob()
	private String bemerkungKbbt;

	@Column(nullable = false)
	private boolean fsAnforderungKbb;

	@Column(nullable = false)
	private boolean fsAnnahmeKbbt;

	@Column(nullable = false)
	private boolean spfv;

	@Column(nullable = false)
	private boolean spnv;

	@Column(nullable = false)
	private boolean sgv;

	@Column(nullable = false)
	private boolean arbeitsschlussKbb;

	@Column(nullable = false)
	private boolean arbeitsschlussKbbt;

	@Column(length = 16)
	private String bbpLisba;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	@ManyToMany(mappedBy = "master")
	private Set<Version> versionen;

	@ManyToMany()
	private Set<Oberleitung> oberleitungen;

	@ManyToMany()
	private Set<Gleissperrung> gleissperrungen;

	@ManyToMany()
	private Set<Fahrplanregelung> fahrplanregelungen;

	@ManyToMany(mappedBy = "master")
	private Set<Kommunikationsbeleg> kommunikationsbelege;

	public Master() {
		gestrichen = false;
		ks = false;
		qs = false;
		quib = false;
		qp = false;
		qsVorschlagKbb = false;
		fsAnforderungKbb = false;
		fsAnnahmeKbbt = false;
		spfv = false;
		spnv = false;
		sgv = false;
		arbeitsschlussKbb = false;
		arbeitsschlussKbbt = false;
	}

	public Art getArt() {
		return art;
	}

	public void setArt(Art art) {
		this.art = art;
	}

	public boolean isGestrichen() {
		return gestrichen;
	}

	public void setGestrichen(boolean gestrichen) {
		this.gestrichen = gestrichen;
	}

	/** gibt an, ob für die Instanz von <code>Master</code> eine Konzeptschätzung erstellt wird */
	public boolean isKs() {
		return ks;
	}

	/** legt fest, ob für die Instanz von <code>Master</code> eine Konzeptschätzung erstellt wird */
	public void setKs(boolean ks) {
		this.ks = ks;
	}

	/** gibt an, ob für die Instanz von <code>Master</code> eine Konzeptschätzung erstellt wird */
	public boolean isQs() {
		return qs;
	}

	/**
	 * legt fest, ob für die Instanz von <code>Master</code> eine qualifizierte Schätzung erstellt
	 * wird
	 */
	public void setQs(boolean qs) {
		this.qs = qs;
	}

	/** gibt an, ob für die Instanz von <code>Master</code> eine QUIB erstellt wird */
	public boolean isQuib() {
		return quib;
	}

	/** legt fest, ob für die Instanz von <code>Master</code> eine QUIB erstellt wird */
	public void setQuib(boolean quib) {
		this.quib = quib;
	}

	/**
	 * gibt an, ob für die Instanz von <code>Master</code> eine qualifizierte Prognose erstellt wird
	 */
	public boolean isQp() {
		return qp;
	}

	/**
	 * legt fest, ob für die Instanz von <code>Master</code> eine qualifizierte Prognose erstellt
	 * wird
	 */
	public void setQp(boolean qp) {
		this.qp = qp;
	}

	/**
	 * @return QUIB-, QP-, QS- oder KS-Nummer, die für den Netzfahrplan vergeben wird
	 */
	public String getQiQsKsNummerNfpl() {
		return qiQsKsNummerNfpl;
	}

	/**
	 * @param qiQsKsNummerNfpl
	 *            QUIB-, QP-, QS- oder KS-Nummer, die für den Netzfahrplan vergeben wird
	 */
	public void setQiQsKsNummerNfpl(String qiQsKsNummerNfpl) {
		this.qiQsKsNummerNfpl = qiQsKsNummerNfpl;
	}

	/**
	 * @return QUIB-, QP-, QS- oder KS-Nummer, die außerhalb des Netzfahrplans vergeben wird
	 */
	public String getQiQsKsNummerSonst() {
		return qiQsKsNummerSonst;
	}

	/**
	 * @param qiQsKsNummerNfpl
	 *            QUIB-, QP-, QS- oder KS-Nummer, die außerhalb des Netzfahrplans vergeben wird
	 */
	public void setQiQsKsNummerSonst(String qiQsKsNummerSonst) {
		this.qiQsKsNummerSonst = qiQsKsNummerSonst;
	}

	/**
	 * Vorschlags der KBB, eine KS/QS für die betreffende Kundeninformation zu erstellen
	 */
	public boolean isQsVorschlagKbb() {
		return qsVorschlagKbb;
	}

	/**
	 * Vorschlags der KBB, eine KS/QS für die betreffende Kundeninformation zu erstellen
	 */
	public void setQsVorschlagKbb(boolean qsVorschlagKbb) {
		this.qsVorschlagKbb = qsVorschlagKbb;
	}

	/** @return zusätzliche Informationen der KBB */
	public String getBemerkungKbb() {
		return bemerkungKbb;
	}

	/**
	 * @param bemerkungKbb
	 *            zusätzliche Informationen der KBB
	 */
	public void setBemerkungKbb(String bemerkungKbb) {
		this.bemerkungKbb = bemerkungKbb;
	}

	/** @return zusätzliche Informationen der KBBT */
	public String getBemerkungKbbt() {
		return bemerkungKbbt;
	}

	/**
	 * @param bemerkungKbbt
	 *            zusätzliche Informationen der KBBT
	 */
	public void setBemerkungKbbt(String bemerkungKbbt) {
		this.bemerkungKbbt = bemerkungKbbt;
	}

	/**
	 * Anforderung einer Fahrplanstudie durch die KBB bei der KBBT
	 */
	public boolean isFsAnforderungKbb() {
		return fsAnforderungKbb;
	}

	/**
	 * Anforderung einer Fahrplanstudie durch die KBB bei der KBBT
	 */
	public void setFsAnforderungKbb(boolean fsAnforderungKbb) {
		this.fsAnforderungKbb = fsAnforderungKbb;
	}

	/**
	 * Annahme einer von der KBB angeforderten Fahrplanstudie durch die KBBT
	 */
	public boolean isFsAnnahmeKbbt() {
		return fsAnnahmeKbbt;
	}

	/**
	 * Annahme einer von der KBB angeforderten Fahrplanstudie durch die KBBT
	 */
	public void setFsAnnahmeKbbt(boolean fsAnnahmeKbbt) {
		this.fsAnnahmeKbbt = fsAnnahmeKbbt;
	}

	/** @return Betroffenheit des SPFV */
	public boolean isSpfv() {
		return spfv;
	}

	/**
	 * @param spfv
	 *            Betroffenheit des SPFV
	 */
	public void setSpfv(boolean spfv) {
		this.spfv = spfv;
	}

	/** @return Betroffenheit des SPNV */
	public boolean isSpnv() {
		return spnv;
	}

	/**
	 * @param spnv
	 *            Betroffenheit des SPNV
	 */
	public void setSpnv(boolean spnv) {
		this.spnv = spnv;
	}

	/** @return Betroffenheit des SGV */
	public boolean isSgv() {
		return sgv;
	}

	/**
	 * @param sgv
	 *            Betroffenheit des SGV
	 */
	public void setSgv(boolean sgv) {
		this.sgv = sgv;
	}

	public boolean isArbeitsschlussKbb() {
		return arbeitsschlussKbb;
	}

	public void setArbeitsschlussKbb(boolean arbeitsschlussKbb) {
		this.arbeitsschlussKbb = arbeitsschlussKbb;
	}

	public boolean isArbeitsschlussKbbt() {
		return arbeitsschlussKbbt;
	}

	public void setArbeitsschlussKbbt(boolean arbeitsschlussKbbt) {
		this.arbeitsschlussKbbt = arbeitsschlussKbbt;
	}

	public String getBbpLisba() {
		return bbpLisba;
	}

	public void setBbpLisba(String bbpLisba) {
		this.bbpLisba = bbpLisba;
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

	public Set<Version> getVersionen() {
		return versionen;
	}

	public void setVersionen(Set<Version> versionen) {
		this.versionen = versionen;
	}

	public Set<Oberleitung> getOberleitungen() {
		return oberleitungen;
	}

	public void setOberleitungen(Set<Oberleitung> oberleitungen) {
		this.oberleitungen = oberleitungen;
	}

	public Set<Gleissperrung> getGleissperrungen() {
		return gleissperrungen;
	}

	public void setGleissperrungen(Set<Gleissperrung> gleissperrungen) {
		this.gleissperrungen = gleissperrungen;
	}

	public Set<Fahrplanregelung> getFahrplanregelungen() {
		return fahrplanregelungen;
	}

	public void setFahrplanregelungen(Set<Fahrplanregelung> fahrplanregelungen) {
		this.fahrplanregelungen = fahrplanregelungen;
	}

	public Set<Kommunikationsbeleg> getKommunikationsbelege() {
		return kommunikationsbelege;
	}

	public void setKommunikationsbelege(Set<Kommunikationsbeleg> kommunikationsbelege) {
		this.kommunikationsbelege = kommunikationsbelege;
	}
}
