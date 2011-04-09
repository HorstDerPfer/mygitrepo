package db.training.bob.model;

public class SearchBean {

	private String searchMasId = null;

	private String searchStreckeBBP = null;

	private String searchStreckeVZG = null;

	private String searchStreckenAbschnitt = null;

	private String vorgangsNummer;

	private String searchKorridorNr = null;

	private String searchKorridorZeitfenster = null;

	private String searchKigBauNr = null;

	private String searchQsNr = null;

	private Boolean nurAktiv;

	private String searchBeginnDatum = null;

	private String searchEndDatum = null;

	private Integer searchFahrplanjahr = null;

	private String bauZeitraumVon, bauZeitraumBis;

	private String searchFploNr = null;

	private Integer searchBearbeiter = null;

	private String searchRegionalbereichFpl = null;

	private String searchMeilenstein = null;

	private String searchArt = null;

	private Boolean searchArtA;

	private Boolean searchArtB;

	private Boolean searchArtKs;

	private Boolean searchArtQs;

	private String searchBearbeitungsbereich;

	private String[] searchMilestones = null;

	private String searchControllingBeginnDatum = null;

	private String searchControllingEndDatum = null;

	private Boolean onlyOpenMilestones;

	private String searchRegionalbereichBM = null;

	private String searchVerkehrstagBeginnDatum = null;

	private String searchVerkehrstagEndDatum = null;

	private String qsks;

	private boolean viewModeMilestones;

	private String letzteXWochen;

	private String naechsteXWochen;

	private boolean optionZeitraum;

	private Boolean searchAenderungen = null;

	private Boolean searchAusfaelle = null;

	public SearchBean() {
		// Standard: Lücken im Bauzeitraum nicht berücksichtigen
		nurAktiv = Boolean.FALSE;
		onlyOpenMilestones = Boolean.TRUE;
		// Standard: Listenansicht, statt Meilensteinanzeige
		viewModeMilestones = false;
		optionZeitraum = false;
		searchFahrplanjahr = null;
	}

	public String getSearchMasId() {
		return searchMasId;
	}

	public void setSearchMasId(String searchMasId) {
		this.searchMasId = searchMasId;
	}

	public String getSearchStreckeBBP() {
		return searchStreckeBBP;
	}

	public void setSearchStreckeBBP(String searchStreckeBBP) {
		this.searchStreckeBBP = searchStreckeBBP;
	}

	public String getSearchStreckeVZG() {
		return searchStreckeVZG;
	}

	public void setSearchStreckeVZG(String searchStreckeVZG) {
		this.searchStreckeVZG = searchStreckeVZG;
	}

	public String getSearchStreckenAbschnitt() {
		return searchStreckenAbschnitt;
	}

	public void setSearchStreckenAbschnitt(String searchStreckenAbschnitt) {
		this.searchStreckenAbschnitt = searchStreckenAbschnitt;
	}

	public String getVorgangsNummer() {
		return vorgangsNummer;
	}

	public void setVorgangsNummer(String vorgangsNummer) {
		this.vorgangsNummer = vorgangsNummer;
	}

	public String getSearchKorridorNr() {
		return searchKorridorNr;
	}

	public void setSearchKorridorNr(String searchKorridorNr) {
		this.searchKorridorNr = searchKorridorNr;
	}

	public String getSearchKorridorZeitfenster() {
		return searchKorridorZeitfenster;
	}

	public void setSearchKorridorZeitfenster(String searchKorridorZeitfenster) {
		this.searchKorridorZeitfenster = searchKorridorZeitfenster;
	}

	public String getSearchKigBauNr() {
		return searchKigBauNr;
	}

	public void setSearchKigBauNr(String searchKigBauNr) {
		this.searchKigBauNr = searchKigBauNr;
	}

	public String getSearchQsNr() {
		return searchQsNr;
	}

	public void setSearchQsNr(String searchQsNr) {
		this.searchQsNr = searchQsNr;
	}

	public Boolean getNurAktiv() {
		return nurAktiv;
	}

	public void setNurAktiv(Boolean nurAktiv) {
		this.nurAktiv = nurAktiv;
	}

	public void setSearchFahrplanjahr(Integer searchFahrplanjahr) {
		this.searchFahrplanjahr = searchFahrplanjahr;
	}

	public Integer getSearchFahrplanjahr() {
		return searchFahrplanjahr;
	}

	public void setSearchBeginnDatum(String searchBeginnDatum) {
		this.searchBeginnDatum = searchBeginnDatum;
	}

	public String getSearchBeginnDatum() {
		return searchBeginnDatum;
	}

	public String getSearchEndDatum() {
		return searchEndDatum;
	}

	public void setSearchEndDatum(String searchEndDatum) {
		this.searchEndDatum = searchEndDatum;
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

	public String getSearchFploNr() {
		return searchFploNr;
	}

	public void setSearchFploNr(String searchFploNr) {
		this.searchFploNr = searchFploNr;
	}

	public Integer getSearchBearbeiter() {
		return searchBearbeiter;
	}

	public void setSearchBearbeiter(Integer searchBearbeiter) {
		this.searchBearbeiter = searchBearbeiter;
	}

	public String getSearchRegionalbereichFpl() {
		return searchRegionalbereichFpl;
	}

	public void setSearchRegionalbereichFpl(String searchRegionalbereichFpl) {
		this.searchRegionalbereichFpl = searchRegionalbereichFpl;
	}

	public String getSearchMeilenstein() {
		return searchMeilenstein;
	}

	public void setSearchMeilenstein(String searchMeilenstein) {
		this.searchMeilenstein = searchMeilenstein;
	}

	public String getSearchArt() {
		return searchArt;
	}

	public void setSearchArt(String searchArt) {
		this.searchArt = searchArt;
	}

	public Boolean getSearchArtA() {
		return searchArtA;
	}

	public void setSearchArtA(Boolean searchArtA) {
		this.searchArtA = searchArtA;
	}

	public Boolean getSearchArtB() {
		return searchArtB;
	}

	public void setSearchArtB(Boolean searchArtB) {
		this.searchArtB = searchArtB;
	}

	public Boolean getSearchArtKs() {
		return searchArtKs;
	}

	public void setSearchArtKs(Boolean searchArtKs) {
		this.searchArtKs = searchArtKs;
	}

	public Boolean getSearchArtQs() {
		return searchArtQs;
	}

	public void setSearchArtQs(Boolean searchArtQs) {
		this.searchArtQs = searchArtQs;
	}

	public String getSearchBearbeitungsbereich() {
		return searchBearbeitungsbereich;
	}

	public void setSearchBearbeitungsbereich(String searchBearbeitungsbereich) {
		this.searchBearbeitungsbereich = searchBearbeitungsbereich;
	}

	public String[] getSearchMilestones() {
		return searchMilestones;
	}

	public void setSearchMilestones(String[] searchMilestones) {
		this.searchMilestones = searchMilestones;
	}

	public String getSearchControllingBeginnDatum() {
		return searchControllingBeginnDatum;
	}

	public void setSearchControllingBeginnDatum(String searchControllingBeginnDatum) {
		this.searchControllingBeginnDatum = searchControllingBeginnDatum;
	}

	public String getSearchControllingEndDatum() {
		return searchControllingEndDatum;
	}

	public void setSearchControllingEndDatum(String searchControllingEndDatum) {
		this.searchControllingEndDatum = searchControllingEndDatum;
	}

	public Boolean getOnlyOpenMilestones() {
		return onlyOpenMilestones;
	}

	public void setOnlyOpenMilestones(Boolean onlyOpenMilestones) {
		this.onlyOpenMilestones = onlyOpenMilestones;
	}

	public String getSearchRegionalbereichBM() {
		return searchRegionalbereichBM;
	}

	public void setSearchRegionalbereichBM(String searchRegionalbereichBM) {
		this.searchRegionalbereichBM = searchRegionalbereichBM;
	}

	public String getSearchVerkehrstagBeginnDatum() {
		return searchVerkehrstagBeginnDatum;
	}

	public void setSearchVerkehrstagBeginnDatum(String searchVerkehrstagBeginnDatum) {
		this.searchVerkehrstagBeginnDatum = searchVerkehrstagBeginnDatum;
	}

	public String getSearchVerkehrstagEndDatum() {
		return searchVerkehrstagEndDatum;
	}

	public void setSearchVerkehrstagEndDatum(String searchVerkehrstagEndDatum) {
		this.searchVerkehrstagEndDatum = searchVerkehrstagEndDatum;
	}

	public String getQsks() {
		return qsks;
	}

	public void setQsks(String qsks) {
		this.qsks = qsks;
	}

	public boolean isViewModeMilestones() {
		return viewModeMilestones;
	}

	public void setViewModeMilestones(boolean viewModeMilestones) {
		this.viewModeMilestones = viewModeMilestones;
	}

	public String getLetzteXWochen() {
		return letzteXWochen;
	}

	public void setLetzteXWochen(String letzteXWochen) {
		this.letzteXWochen = letzteXWochen;
	}

	public String getNaechsteXWochen() {
		return naechsteXWochen;
	}

	public void setNaechsteXWochen(String naechsteXWochen) {
		this.naechsteXWochen = naechsteXWochen;
	}

	public boolean isOptionZeitraum() {
		return optionZeitraum;
	}

	public void setOptionZeitraum(boolean optionZeitraum) {
		this.optionZeitraum = optionZeitraum;
	}

	public Boolean getSearchAenderungen() {
		return searchAenderungen;
	}

	public void setSearchAenderungen(Boolean searchAenderungen) {
		this.searchAenderungen = searchAenderungen;
	}

	public Boolean getSearchAusfaelle() {
		return searchAusfaelle;
	}

	public void setSearchAusfaelle(Boolean searchAusfaelle) {
		this.searchAusfaelle = searchAusfaelle;
	}
	/**
	 * Show all attributes for debugging purposes.
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("Attributes and values:\n");
		
		sb.append("[searchAusfaelle: "+ searchAusfaelle +"]\n ");
		sb.append("[searchAenderungen: "+ searchAenderungen +"]\n ");
		sb.append("[optionZeitraum: "+ optionZeitraum +"]\n ");
		sb.append("[naechsteXWochen: "+ naechsteXWochen +"]\n ");
		sb.append("[letzteXWochen: "+ letzteXWochen +"]\n ");
		sb.append("[viewModeMilestones: "+ viewModeMilestones +"]\n ");
		sb.append("[qsks: "+ qsks +"]\n ");
		sb.append("[searchVerkehrstagEndDatum: "+ searchVerkehrstagEndDatum +"]\n ");
		sb.append("[searchVerkehrstagBeginnDatum: "+ searchVerkehrstagBeginnDatum +"]\n ");
		sb.append("[searchRegionalbereichBM: "+ searchRegionalbereichBM +"]\n ");
		sb.append("[onlyOpenMilestones: "+ onlyOpenMilestones +"]\n ");
		sb.append("[searchControllingEndDatum: "+ searchControllingEndDatum +"]\n ");
		sb.append("[searchControllingBeginnDatum: "+ searchControllingBeginnDatum +"]\n ");
		sb.append("[searchMilestones: "+ searchMilestones +"]\n ");
		sb.append("[searchBearbeitungsbereich: "+ searchBearbeitungsbereich +"]\n ");
		sb.append("[searchArtQs: "+ searchArtQs +"]\n ");
		sb.append("[searchArtKs: "+ searchArtKs +"]\n ");
		sb.append("[searchArtB: "+ searchArtB +"]\n ");
		sb.append("[searchArtA: "+ searchArtA +"]\n ");
		sb.append("[searchArt: "+ searchArt +"]\n ");
		sb.append("[searchMeilenstein: "+ searchMeilenstein +"]\n ");
		sb.append("[searchRegionalbereichFpl: "+ searchRegionalbereichFpl +"]\n ");
		sb.append("[searchBearbeiter: "+ searchBearbeiter +"]\n ");
		sb.append("[searchFploNr: "+ searchFploNr +"]\n ");
		sb.append("[bauZeitraumBis: "+ bauZeitraumBis +"]\n ");
		sb.append("[bauZeitraumVon: "+ bauZeitraumVon +"]\n ");
		sb.append("[searchFahrplanjahr: "+ searchFahrplanjahr +"]\n ");
		sb.append("[searchEndDatum: "+ searchEndDatum +"]\n ");
		sb.append("[searchBeginnDatum: "+ searchBeginnDatum +"]\n ");
		sb.append("[nurAktiv: "+ nurAktiv +"]\n ");
		sb.append("[searchQsNr: "+ searchQsNr +"]\n ");
		sb.append("[searchKigBauNr: "+ searchKigBauNr +"]\n ");
		sb.append("[searchKorridorZeitfenster: "+ searchKorridorZeitfenster +"]\n ");
		sb.append("[searchKorridorNr: "+ searchKorridorNr +"]\n ");
		sb.append("[vorgangsNummer: "+ vorgangsNummer +"]\n ");
		sb.append("[searchStreckenAbschnitt: "+ searchStreckenAbschnitt +"]\n ");
		sb.append("[searchStreckeVZG: "+ searchStreckeVZG +"]\n ");
		sb.append("[searchStreckeBBP: "+ searchStreckeBBP +"]\n ");
		sb.append("[searchMasId: "+ searchMasId +"]\n ");

		return sb.toString();
	}

}