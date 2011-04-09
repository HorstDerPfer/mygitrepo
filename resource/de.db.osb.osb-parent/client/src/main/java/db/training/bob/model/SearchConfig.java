package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.model.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "searchconfig")
public class SearchConfig extends EasyPersistentObject {

	@OneToOne(fetch = FetchType.EAGER)
	private User user = null;

	@Column(name = "art_qs")
	private Boolean artQs = false;

	@Column(name = "art_ks")
	private Boolean artKs = false;

	@Column(name = "art_a")
	private Boolean artA = false;

	@Column(name = "art_b")
	private Boolean artB = false;

	@Column
	private String kigBauNr = null;

	@Column
	private String korridorNr = null;

	@Column
	private String korridorZeitfenster = null;

	@Column
	private String masId = null;

	@Column
	private String streckeBBP = null;

	@Column
	private String streckeVZG = null;

	@Column
	private String streckenAbschnitt = null;

	@Column
	private String vorgangsNummer;

	@Column
	private Boolean nurAktiv;

	@Column
	private Integer fahrplanjahr = null;

	// Baubeginn von
	@Column
	private String beginnDatum = null;

	// Baubeginn bis
	@Column
	private String endDatum = null;

	@Column
	private String bauZeitraumVon, bauZeitraumBis;

	@Column
	private Integer bearbeiter = null;

	@Column
	private String qsNr = null;

	@Column
	private String fploNr = null;

	@Column
	private String regionalbereichFpl = null;

	@Column
	private String bearbeitungsbereich = null;

	@Column
	private String viewMode = null;

	@Column
	private String[] milestones = null;

	@Column
	private Boolean onlyOpenMilestones;

	@Column
	private String sortDirection;

	@Column
	private String sortColumn;

	@Column
	private Integer letzteXWochen;

	@Column
	private Integer naechsteXWochen;

	@Column(length = 10)
	private String optionDatumZeitraum;

	@Column
	private Boolean ausfaelle;

	@Column
	private Boolean aenderungen;

	protected SearchConfig() {

	}

	public SearchConfig(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getArtQs() {
		return artQs;
	}

	public void setArtQs(Boolean artQs) {
		this.artQs = artQs;
	}

	public Boolean getArtKs() {
		return artKs;
	}

	public void setArtKs(Boolean artKs) {
		this.artKs = artKs;
	}

	public Boolean getArtA() {
		return artA;
	}

	public void setArtA(Boolean artA) {
		this.artA = artA;
	}

	public Boolean getArtB() {
		return artB;
	}

	public void setArtB(Boolean artB) {
		this.artB = artB;
	}

	public String getKigBauNr() {
		return kigBauNr;
	}

	public void setKigBauNr(String kigBauNr) {
		this.kigBauNr = kigBauNr;
	}

	public String getKorridorNr() {
		return korridorNr;
	}

	public void setKorridorNr(String korridorNr) {
		this.korridorNr = korridorNr;
	}

	public String getKorridorZeitfenster() {
		return korridorZeitfenster;
	}

	public void setKorridorZeitfenster(String korridorZeitfenster) {
		this.korridorZeitfenster = korridorZeitfenster;
	}

	public String getMasId() {
		return masId;
	}

	public void setMasId(String masId) {
		this.masId = masId;
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

	public String getStreckenAbschnitt() {
		return streckenAbschnitt;
	}

	public void setStreckenAbschnitt(String streckenAbschnitt) {
		this.streckenAbschnitt = streckenAbschnitt;
	}

	public String getVorgangsNummer() {
		return vorgangsNummer;
	}

	public void setVorgangsNummer(String vorgangsNummer) {
		this.vorgangsNummer = vorgangsNummer;
	}

	public Boolean getNurAktiv() {
		return nurAktiv;
	}

	public void setNurAktiv(Boolean nurAktiv) {
		this.nurAktiv = nurAktiv;
	}

	public Integer getFahrplanjahr() {
		return this.fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getBeginnDatum() {
		return beginnDatum;
	}

	public void setBeginnDatum(String beginnDatum) {
		this.beginnDatum = beginnDatum;
	}

	public String getEndDatum() {
		return endDatum;
	}

	public void setEndDatum(String endDatum) {
		this.endDatum = endDatum;
	}

	public String getBauZeitraumVon() {
		return bauZeitraumVon;
	}

	public void setBauZeitraumVon(String bauZeitraumVon) {
		this.bauZeitraumVon = bauZeitraumVon;
	}

	public String getBauZeitraumBis() {
		return bauZeitraumBis;
	}

	public void setBauZeitraumBis(String bauZeitraumBis) {
		this.bauZeitraumBis = bauZeitraumBis;
	}

	public Integer getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(Integer bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public String getQsNr() {
		return qsNr;
	}

	public void setQsNr(String qsNr) {
		this.qsNr = qsNr;
	}

	public String getFploNr() {
		return fploNr;
	}

	public void setFploNr(String fploNr) {
		this.fploNr = fploNr;
	}

	public String getRegionalbereichFpl() {
		return regionalbereichFpl;
	}

	public void setRegionalbereichFpl(String regionalbereichFpl) {
		this.regionalbereichFpl = regionalbereichFpl;
	}

	public String getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	public void setBearbeitungsbereich(String bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public String[] getMilestones() {
		return milestones;
	}

	public void setMilestones(String[] milestones) {
		this.milestones = milestones;
	}

	public Boolean getOnlyOpenMilestones() {
		return onlyOpenMilestones;
	}

	public void setOnlyOpenMilestones(Boolean onlyOpenMilestones) {
		this.onlyOpenMilestones = onlyOpenMilestones;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public Integer getLetzteXWochen() {
		return letzteXWochen;
	}

	public void setLetzteXWochen(Integer letzteXWochen) {
		this.letzteXWochen = letzteXWochen;
	}

	public Integer getNaechsteXWochen() {
		return naechsteXWochen;
	}

	public void setNaechsteXWochen(Integer naechsteXWochen) {
		this.naechsteXWochen = naechsteXWochen;
	}

	public String getOptionDatumZeitraum() {
		return optionDatumZeitraum;
	}

	public void setOptionDatumZeitraum(String optionDatumZeitraum) {
		this.optionDatumZeitraum = optionDatumZeitraum;
	}

	public Boolean getAusfaelle() {
		return ausfaelle;
	}

	public void setAusfaelle(Boolean ausfaelle) {
		this.ausfaelle = ausfaelle;
	}

	public Boolean getAenderungen() {
		return aenderungen;
	}

	public void setAenderungen(Boolean aenderungen) {
		this.aenderungen = aenderungen;
	}

}
