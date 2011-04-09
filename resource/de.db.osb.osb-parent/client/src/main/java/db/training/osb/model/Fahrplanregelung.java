package db.training.osb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.Regelungsart;

@SuppressWarnings("serial")
@Entity
@Table(name = "fahrplanregelung")
public class Fahrplanregelung extends EasyPersistentObjectVc implements Historizable {

	public enum AufnahmeArt {
		FALSE, GE, AR
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "buendel_fahrplanregelung", joinColumns = { @JoinColumn(name = "fahrplanregelung_id") }, inverseJoinColumns = { @JoinColumn(name = "buendel_id") })
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Buendel> buendel = new HashSet<Buendel>();

	@Column(name = "fahrplanjahr", nullable = false, length = 4)
	private Integer fahrplanjahr;

	@Column(name = "nummer", length = 7)
	private Integer nummer;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Regionalbereich regionalbereich;

	@OneToMany(mappedBy = "fahrplanregelung")
	@Cascade(CascadeType.DELETE_ORPHAN)
	private Set<Regelungsart> regelungsarten;

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsweise betriebsweise;

	/** the 'x' for compatiblity with oracle */
	@Column(name = "namex")
	private String name;

	@Column(name = "aufnahme_nfp_vorschlag", nullable = true)
	@Enumerated(value = EnumType.ORDINAL)
	private AufnahmeArt aufnahmeNfpVorschlag;

	@Column(name = "aufnahme_nfp", nullable = true)
	@Enumerated(value = EnumType.ORDINAL)
	private AufnahmeArt aufnahmeNfp;

	@Column(name = "behandlung_ks")
	private boolean behandlungKS;

	@Column(name = "nachtsperrpause")
	private boolean nachtsperrpause;

	@Column(name = "verkehrstagMo")
	private boolean verkehrstag_mo;

	@Column(name = "verkehrstagDi")
	private boolean verkehrstag_di;

	@Column(name = "verkehrstagMi")
	private boolean verkehrstag_mi;

	@Column(name = "verkehrstagDo")
	private boolean verkehrstag_do;

	@Column(name = "verkehrstagFr")
	private boolean verkehrstag_fr;

	@Column(name = "verkehrstagSa")
	private boolean verkehrstag_sa;

	@Column(name = "verkehrstagSo")
	private boolean verkehrstag_so;

	@Column(name = "planstart")
	private Date planStart;

	@Column(name = "planende")
	private Date planEnde;

	/** the 'x' for compatiblity with oracle */
	@Column(name = "startx")
	private Date start;

	@Column(name = "ende")
	private Date ende;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleVon;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleBis;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleVonSPFV;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleBisSPFV;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleVonSPNV;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleBisSPNV;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleVonSGV;

	@ManyToOne(fetch = FetchType.LAZY)
	private Betriebsstelle betriebsstelleBisSGV;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fahrplanregelung")
	private Set<UmleitungFahrplanregelungLink> umleitungFahrplanregelungLinks;

	@Column(name = "totalAusfallSPFV")
	private Integer totalAusfallSpfv;

	@Column(name = "totalAusfallSPNV")
	private Integer totalAusfallSpnv;

	@Column(name = "totalAusfallSGV")
	private Integer totalAusfallSgv;

	@OneToMany(mappedBy = "fahrplanregelung")
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Gleissperrung> gleissperrungen;

	@Column(name = "relevanz_bzu")
	private boolean relevanzBzu;

	private boolean fixiert;

	private boolean deleted;

	private boolean importiert;

	public Set<UmleitungFahrplanregelungLink> getUmleitungFahrplanregelungLinks() {
		return umleitungFahrplanregelungLinks;
	}

	public void setUmleitungFahrplanregelungLinks(
	    Set<UmleitungFahrplanregelungLink> umleitungFahrplanregelungLinks) {
		this.umleitungFahrplanregelungLinks = umleitungFahrplanregelungLinks;
	}

	public Set<Buendel> getBuendel() {
		return buendel;
	}

	public void setBuendel(Set<Buendel> buendel) {
		this.buendel = buendel;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Set<Regelungsart> getRegelungsarten() {
		return regelungsarten;
	}

	public void setRegelungsarten(Set<Regelungsart> regelungsarten) {
		this.regelungsarten = regelungsarten;
	}

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AufnahmeArt getAufnahmeNfpVorschlag() {
		return aufnahmeNfpVorschlag;
	}

	public void setAufnahmeNfpVorschlag(AufnahmeArt aufnahmeNfpVorschlag) {
		this.aufnahmeNfpVorschlag = aufnahmeNfpVorschlag;
	}

	public AufnahmeArt getAufnahmeNfp() {
		return aufnahmeNfp;
	}

	public void setAufnahmeNfp(AufnahmeArt aufnahmeNfp) {
		this.aufnahmeNfp = aufnahmeNfp;
	}

	public boolean isBehandlungKS() {
		return behandlungKS;
	}

	public void setBehandlungKS(boolean behandlungKS) {
		this.behandlungKS = behandlungKS;
	}

	public Date getPlanStart() {
		return planStart;
	}

	public void setPlanStart(Date planStart) {
		this.planStart = planStart;
	}

	public Date getPlanEnde() {
		return planEnde;
	}

	public void setPlanEnde(Date planEnde) {
		this.planEnde = planEnde;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnde() {
		return ende;
	}

	public void setEnde(Date ende) {
		this.ende = ende;
	}

	public Betriebsstelle getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(Betriebsstelle betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public Betriebsstelle getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(Betriebsstelle betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
	}

	public Betriebsstelle getBetriebsstelleVonSPFV() {
		return betriebsstelleVonSPFV;
	}

	public void setBetriebsstelleVonSPFV(Betriebsstelle betriebsstelleVonSPFV) {
		this.betriebsstelleVonSPFV = betriebsstelleVonSPFV;
	}

	public Betriebsstelle getBetriebsstelleBisSPFV() {
		return betriebsstelleBisSPFV;
	}

	public void setBetriebsstelleBisSPFV(Betriebsstelle betriebsstelleBisSPFV) {
		this.betriebsstelleBisSPFV = betriebsstelleBisSPFV;
	}

	public Betriebsstelle getBetriebsstelleVonSPNV() {
		return betriebsstelleVonSPNV;
	}

	public void setBetriebsstelleVonSPNV(Betriebsstelle betriebsstelleVonSPNV) {
		this.betriebsstelleVonSPNV = betriebsstelleVonSPNV;
	}

	public Betriebsstelle getBetriebsstelleBisSPNV() {
		return betriebsstelleBisSPNV;
	}

	public void setBetriebsstelleBisSPNV(Betriebsstelle betriebsstelleBisSPNV) {
		this.betriebsstelleBisSPNV = betriebsstelleBisSPNV;
	}

	public Betriebsstelle getBetriebsstelleVonSGV() {
		return betriebsstelleVonSGV;
	}

	public void setBetriebsstelleVonSGV(Betriebsstelle betriebsstelleVonSGV) {
		this.betriebsstelleVonSGV = betriebsstelleVonSGV;
	}

	public Betriebsstelle getBetriebsstelleBisSGV() {
		return betriebsstelleBisSGV;
	}

	public void setBetriebsstelleBisSGV(Betriebsstelle betriebsstelleBisSGV) {
		this.betriebsstelleBisSGV = betriebsstelleBisSGV;
	}

	public Integer getTotalAusfallSpfv() {
		return totalAusfallSpfv;
	}

	public void setTotalAusfallSpfv(Integer totalAusfallSpfv) {
		this.totalAusfallSpfv = totalAusfallSpfv;
	}

	public Integer getTotalAusfallSpnv() {
		return totalAusfallSpnv;
	}

	public void setTotalAusfallSpnv(Integer totalAusfallSpnv) {
		this.totalAusfallSpnv = totalAusfallSpnv;
	}

	public Integer getTotalAusfallSgv() {
		return totalAusfallSgv;
	}

	public void setTotalAusfallSgv(Integer totalAusfallSgv) {
		this.totalAusfallSgv = totalAusfallSgv;
	}

	public Betriebsweise getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(Betriebsweise betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public boolean isNachtsperrpause() {
		return nachtsperrpause;
	}

	public void setNachtsperrpause(boolean nachtsperrpause) {
		this.nachtsperrpause = nachtsperrpause;
	}

	public boolean isVerkehrstag_mo() {
		return verkehrstag_mo;
	}

	public void setVerkehrstag_mo(boolean verkehrstag_mo) {
		this.verkehrstag_mo = verkehrstag_mo;
	}

	public boolean isVerkehrstag_di() {
		return verkehrstag_di;
	}

	public void setVerkehrstag_di(boolean verkehrstag_di) {
		this.verkehrstag_di = verkehrstag_di;
	}

	public boolean isVerkehrstag_mi() {
		return verkehrstag_mi;
	}

	public void setVerkehrstag_mi(boolean verkehrstag_mi) {
		this.verkehrstag_mi = verkehrstag_mi;
	}

	public boolean isVerkehrstag_do() {
		return verkehrstag_do;
	}

	public void setVerkehrstag_do(boolean verkehrstag_do) {
		this.verkehrstag_do = verkehrstag_do;
	}

	public boolean isVerkehrstag_fr() {
		return verkehrstag_fr;
	}

	public void setVerkehrstag_fr(boolean verkehrstag_fr) {
		this.verkehrstag_fr = verkehrstag_fr;
	}

	public boolean isVerkehrstag_sa() {
		return verkehrstag_sa;
	}

	public void setVerkehrstag_sa(boolean verkehrstag_sa) {
		this.verkehrstag_sa = verkehrstag_sa;
	}

	public boolean isVerkehrstag_so() {
		return verkehrstag_so;
	}

	public void setVerkehrstag_so(boolean verkehrstag_so) {
		this.verkehrstag_so = verkehrstag_so;
	}

	public Set<Gleissperrung> getGleissperrungen() {
		return gleissperrungen;
	}

	public void setGleissperrungen(Set<Gleissperrung> gleissperrungen) {
		this.gleissperrungen = gleissperrungen;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isRelevanzBzu() {
		return relevanzBzu;
	}

	public void setRelevanzBzu(boolean relevanzBzu) {
		this.relevanzBzu = relevanzBzu;
	}

	public boolean isFixiert() {
		return fixiert;
	}

	public void setFixiert(boolean fixiert) {
		this.fixiert = fixiert;
	}

	public boolean isImportiert() {
		return importiert;
	}

	public void setImportiert(boolean importiert) {
		this.importiert = importiert;
	}

	@Transient
	public String getFahrplanregelungId() {
		String rbId = null;
		String fj = null;
		String nrF = null;
		String nrB = null;

		StringBuffer sb = new StringBuffer();

		// Fahrplanregelung ist keinem Buendel zugeordnet
		if (getBuendel() == null || getBuendel().size() == 0) {
			sb.append("F00.00.000");
			// Fahrplanjahr der Fahrplanregelung
			if (getFahrplanjahr() != null && getFahrplanjahr() > 1900) {
				fj = Integer.toString(getFahrplanjahr());
				fj = fj.substring(2);
				sb.replace(1, 3, fj);
			}
			// Regionalbereich der Fahrplanregelung
			if (getRegionalbereich() != null) {
				rbId = Integer.toString(getRegionalbereich().getId());
				sb.replace(6 - rbId.length(), 6, rbId);
			}
			// Nummer der Fahrplanregelung
			if (getNummer() != null) {
				nrF = Integer.toString(getNummer());
				sb.replace(10 - nrF.length(), 10, nrF);
			}
		}
		// Fahrplanregelung ist einem Buendel zugeordnet
		else if (getBuendel().size() == 1) {
			sb.append("00.00.0000_000");
			// Buendel wird gesetzt
			Buendel buendel = null;
			for (Buendel b : getBuendel()) {
				buendel = b;
				break;
			}
			// Regionalbereich des Buendels
			if (buendel.getRegionalbereich() != null) {
				rbId = Integer.toString(buendel.getRegionalbereich().getId());
				sb.replace(2 - rbId.length(), 2, rbId);
			}
			// Fahrplanjahr des Buendels
			if (buendel.getFahrplanjahr() != null && buendel.getFahrplanjahr() > 1900) {
				fj = Integer.toString(buendel.getFahrplanjahr());
				fj = fj.substring(2);
				sb.replace(3, 5, fj);
			}
			// Nummer des Buendels
			if (buendel.getLfdNr() != null) {
				nrB = Integer.toString(buendel.getLfdNr());
				sb.replace(10 - nrB.length(), 10, nrB);
			}
			// Nummer der Fahrplanregelung
			if (getNummer() != null) {
				nrF = Integer.toString(getNummer());
				sb.replace(14 - nrF.length(), 14, nrF);
			}
		}
		// Fahrplanregelung ist mehreren Buendeln zugeordnet
		else if (getBuendel().size() > 1) {
			sb.append("X_000");

			// Nummer der Fahrplanregelung
			if (getNummer() != null) {
				nrF = Integer.toString(getNummer());
				sb.replace(5 - nrF.length(), 5, nrF);
			}
		}

		return sb.toString();
	}

	@Transient
	public String getVerkehrstage() {
		StringBuilder result = new StringBuilder();
		if (verkehrstag_mo)
			result.append("Mo, ");
		if (verkehrstag_di)
			result.append("Di, ");
		if (verkehrstag_mi)
			result.append("Mi, ");
		if (verkehrstag_do)
			result.append("Do, ");
		if (verkehrstag_fr)
			result.append("Fr, ");
		if (verkehrstag_sa)
			result.append("Sa, ");
		if (verkehrstag_so)
			result.append("So, ");
		if (result.length() > 1)
			result.replace(result.length() - 2, result.length(), "");
		return result.toString();
	}

	@Transient
	public Integer getAnzahlUmleitungswege() {
		Integer anz = 0;
		for (UmleitungFahrplanregelungLink ufl : getUmleitungFahrplanregelungLinks()) {
			if (ufl.getUmleitung() != null && ufl.getUmleitung().getUmleitungswege() != null
			    && !ufl.getUmleitung().isDeleted()) {
				anz += ufl.getUmleitung().getUmleitungswege().size();
			}
		}
		return anz;
	}

	@Transient
	public Integer getAnzahlSpfv() {
		Integer anz = 0;
		for (UmleitungFahrplanregelungLink ufl : getUmleitungFahrplanregelungLinks()) {
			if (ufl.getUmleitung() != null && !ufl.getUmleitung().isDeleted()) {
				if (ufl.getAnzahlSPFV() != null) {
					anz += ufl.getAnzahlSPFV();
				}
				if (ufl.getAnzahlSPFVGegenRich() != null) {
					anz += ufl.getAnzahlSPFVGegenRich();
				}
			}
		}
		return anz;
	}

	@Transient
	public Integer getAnzahlSpnv() {
		Integer anz = 0;
		for (UmleitungFahrplanregelungLink ufl : getUmleitungFahrplanregelungLinks()) {
			if (ufl.getUmleitung() != null && !ufl.getUmleitung().isDeleted()) {
				if (ufl.getAnzahlSPNV() != null) {
					anz += ufl.getAnzahlSPNV();
				}
				if (ufl.getAnzahlSPNVGegenRich() != null) {
					anz += ufl.getAnzahlSPNVGegenRich();
				}
			}
		}
		return anz;
	}

	@Transient
	public Integer getAnzahlSgv() {
		Integer anz = 0;
		for (UmleitungFahrplanregelungLink ufl : getUmleitungFahrplanregelungLinks()) {
			if (ufl.getUmleitung() != null && !ufl.getUmleitung().isDeleted()) {
				if (ufl.getAnzahlSGV() != null) {
					anz += ufl.getAnzahlSGV();
				}
				if (ufl.getAnzahlSGVGegenRich() != null) {
					anz += ufl.getAnzahlSGVGegenRich();
				}
			}
		}
		return anz;
	}

	@Transient
	public Fahrplanregelung clone() {
		Fahrplanregelung fr = new Fahrplanregelung();
		fr.setId(null);
		fr.setName(getName());
		fr.setNummer(getNummer());
		fr.setLastChangeDate(getLastChangeDate());
		fr.setAufnahmeNfp(getAufnahmeNfp());
		fr.setAufnahmeNfpVorschlag(getAufnahmeNfpVorschlag());
		fr.setBehandlungKS(isBehandlungKS());
		fr.setBetriebsstelleBis(getBetriebsstelleBis());
		fr.setBetriebsstelleBisSGV(getBetriebsstelleBisSGV());
		fr.setBetriebsstelleBisSPFV(getBetriebsstelleBisSPFV());
		fr.setBetriebsstelleBisSPNV(getBetriebsstelleBisSPNV());
		fr.setBetriebsstelleVon(getBetriebsstelleVon());
		fr.setBetriebsstelleVonSGV(getBetriebsstelleVonSGV());
		fr.setBetriebsstelleVonSPFV(getBetriebsstelleVonSPFV());
		fr.setBetriebsweise(getBetriebsweise());
		fr.setBuendel(getBuendel());
		fr.setDeleted(isDeleted());
		fr.setEnde(getEnde());
		fr.setFahrplanjahr(getFahrplanjahr());
		fr.setFixiert(isFixiert());
		fr.setGleissperrungen(getGleissperrungen());
		fr.setNachtsperrpause(isNachtsperrpause());
		fr.setPlanEnde(getPlanEnde());
		fr.setPlanStart(getPlanStart());
		fr.setRegionalbereich(getRegionalbereich());
		fr.setRelevanzBzu(isRelevanzBzu());
		fr.setStart(getStart());
		fr.setTotalAusfallSgv(getTotalAusfallSgv());
		fr.setTotalAusfallSpfv(getTotalAusfallSpfv());
		fr.setTotalAusfallSpnv(getTotalAusfallSpnv());
		fr.setVerkehrstag_mo(isVerkehrstag_mo());
		fr.setVerkehrstag_di(isVerkehrstag_di());
		fr.setVerkehrstag_mi(isVerkehrstag_mi());
		fr.setVerkehrstag_do(isVerkehrstag_do());
		fr.setVerkehrstag_fr(isVerkehrstag_fr());
		fr.setVerkehrstag_sa(isVerkehrstag_sa());
		fr.setVerkehrstag_so(isVerkehrstag_so());
		if (getRegelungsarten() != null && getRegelungsarten().size() > 0) {
			fr.setRegelungsarten(new TreeSet<Regelungsart>());
			fr.getRegelungsarten().addAll(getRegelungsarten());
		}
		// UmleitungFahrplanregelungLinks muessen separat geclont werden
		// Set<UmleitungFahrplanregelungLink> ufls = new TreeSet<UmleitungFahrplanregelungLink>();
		// for (UmleitungFahrplanregelungLink ufl : getUmleitungFahrplanregelungLinks()) {
		// ufls.add(ufl.clone());
		// }
		// fr.setUmleitungFahrplanregelungLinks(ufls);
		return fr;
	}
}