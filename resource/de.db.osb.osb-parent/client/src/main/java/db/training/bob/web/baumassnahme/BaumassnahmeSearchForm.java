package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.SearchConfig;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseValidatorForm;

/**
 * @author michels
 * 
 */
@SuppressWarnings("serial")
public class BaumassnahmeSearchForm extends BaseValidatorForm {

	private Boolean artKs;

	private Boolean artQs;

	private Boolean artA;

	private Boolean artB;

	private String kigBauNr = null;

	private String korridorNr = null;

	private String korridorZeitfenster = null;

	private String masId = null;

	private String streckeBBP = null;

	private String streckeVZG = null;

	private String streckenAbschnitt = null;

	private String vorgangsNummer;

	private Boolean nurAktiv;

	// Baubeginn von
	private String beginnDatum = null;

	// Baubeginn bis
	private String endDatum = null;

	private String bauZeitraumVon, bauZeitraumBis;

	private Integer bearbeiter = null;

	private String qsNr = null;

	private String fploNr = null;

	private String regionalbereichFpl = null;

	private String regionalbereichBM = null;

	private String bearbeitungsbereich = null;

	private List<Bearbeitungsbereich> bearbeitungsbereichList = new ArrayList<Bearbeitungsbereich>();

	private String viewMode = null;

	private String controllingBeginnDatum = null;

	private String controllingEndDatum = null;

	private String[] milestones = null;

	private Boolean onlyOpenMilestones;

	private String sortDirection;

	private String sortColumn;

	private String letzteXWochen;

	private String naechsteXWochen;

	private String optionDatumZeitraum;

	private Integer fahrplanjahr;

	private String ausfaelle = null;

	private String aenderungen = null;

	public BaumassnahmeSearchForm() {
		super();
		reset();
	}

	public void reset() {
		masId = null;
		ausfaelle = "false";
		aenderungen = null;
		artQs = true;
		artKs = true;
		artA = true;
		artB = true;
		korridorZeitfenster = null;

		kigBauNr = null;
		korridorNr = null;
		streckeBBP = null;
		streckeVZG = null;
		streckenAbschnitt = null;
		setVorgangsNummer(null);
		setNurAktiv(Boolean.FALSE);
		beginnDatum = null;
		endDatum = null;
		setBauZeitraumVon(null);
		setBauZeitraumBis(null);
		bearbeiter = null;
		qsNr = null;
		fploNr = null;
		regionalbereichFpl = null;
		regionalbereichBM = null;
		setBearbeitungsbereich(null);
		milestones = null;
		viewMode = "listView";
		setOnlyOpenMilestones(Boolean.TRUE);
		bearbeitungsbereichList = new ArrayList<Bearbeitungsbereich>();
		setSortDirection(SortOrderEnum.ASCENDING.getName());
		sortColumn = null;
		letzteXWochen = null;
		naechsteXWochen = null;
		optionDatumZeitraum = "datum";
		setFahrplanjahr(null);

		setPage(0);

		// Suchbereich für Arbeitssteuerung auf aktuelle Arbeitswoche
		// (Montag-Freitag)
		// vorbelegen
		Calendar cal = GregorianCalendar.getInstance();

		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		controllingBeginnDatum = FrontendHelper.castDateToString(cal.getTime());

		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
		controllingEndDatum = FrontendHelper.castDateToString(cal.getTime());
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		// super.reset darf hier nicht verwendet werden, da die Multiboxen dadurch gelöscht werden!
		// super.reset(arg0, request);

		if (request.getParameter("method") != null) {
			if (!request.getParameter("method").startsWith("xls")
				&& request.getParameter("fromViewBaumassnahmeToList") == null) {
				resetProperty(request, this, "milestones", null);
				resetProperty(request, this, "nurAktiv", null);
				resetProperty(request, this, "onlyOpenMilestones", null);
				resetProperty(request, this, "artA", null);
				resetProperty(request, this, "artB", null);
				resetProperty(request, this, "artKs", null);
				resetProperty(request, this, "artQs", null);
			}
		}
	}

	public Boolean getArtKs() {
		return artKs;
	}

	public void setArtKs(Boolean artKs) {
		this.artKs = artKs;
	}

	public Boolean getArtQs() {
		return artQs;
	}

	public void setArtQs(Boolean artQs) {
		this.artQs = artQs;
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

	public String getRegionalbereichBM() {
		return regionalbereichBM;
	}

	public void setRegionalbereichBM(String regionalbereichBM) {
		this.regionalbereichBM = regionalbereichBM;
	}

	public String getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	public void setBearbeitungsbereich(String bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	public List<Bearbeitungsbereich> getBearbeitungsbereichList() {
		return bearbeitungsbereichList;
	}

	public void setBearbeitungsbereichList(List<Bearbeitungsbereich> bearbeitungsbereichList) {
		this.bearbeitungsbereichList = bearbeitungsbereichList;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public String getControllingBeginnDatum() {
		return controllingBeginnDatum;
	}

	public void setControllingBeginnDatum(String controllingBeginnDatum) {
		this.controllingBeginnDatum = controllingBeginnDatum;
	}

	public String getControllingEndDatum() {
		return controllingEndDatum;
	}

	public void setControllingEndDatum(String controllingEndDatum) {
		this.controllingEndDatum = controllingEndDatum;
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

	public String getOptionDatumZeitraum() {
		return optionDatumZeitraum;
	}

	public void setOptionDatumZeitraum(String optionDatumZeitraum) {
		this.optionDatumZeitraum = optionDatumZeitraum;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getAusfaelle() {
		return ausfaelle;
	}

	public void setAusfaelle(String ausfaelle) {
		this.ausfaelle = ausfaelle;
	}

	public String getAenderungen() {
		return aenderungen;
	}

	public void setAenderungen(String aenderungen) {
		this.aenderungen = aenderungen;
	}

	public void fillFromSearchConfig(SearchConfig searchConfig) {
		if (searchConfig != null) {
			masId = searchConfig.getMasId();
			if (searchConfig.getAusfaelle() != null) {
				ausfaelle = searchConfig.getAusfaelle().toString();
			} else {
				ausfaelle = "";
			}
			if (searchConfig.getAenderungen() != null) {
				aenderungen = searchConfig.getAenderungen().toString();
			} else {
				aenderungen = "";
			}
			artQs = searchConfig.getArtQs();
			artKs = searchConfig.getArtKs();
			artA = searchConfig.getArtA();
			artB = searchConfig.getArtB();
			korridorZeitfenster = searchConfig.getKorridorZeitfenster();

			kigBauNr = searchConfig.getKigBauNr();
			korridorNr = searchConfig.getKorridorNr();
			streckeBBP = searchConfig.getStreckeBBP();
			streckeVZG = searchConfig.getStreckeVZG();
			streckenAbschnitt = searchConfig.getStreckenAbschnitt();
			vorgangsNummer = searchConfig.getVorgangsNummer();
			nurAktiv = searchConfig.getNurAktiv();
			fahrplanjahr = searchConfig.getFahrplanjahr();
			beginnDatum = searchConfig.getBeginnDatum();
			endDatum = searchConfig.getEndDatum();
			bauZeitraumVon = searchConfig.getBauZeitraumVon();
			bauZeitraumBis = searchConfig.getBauZeitraumBis();
			bearbeiter = searchConfig.getBearbeiter();
			qsNr = searchConfig.getQsNr();
			fploNr = searchConfig.getFploNr();
			regionalbereichFpl = searchConfig.getRegionalbereichFpl();
			bearbeitungsbereich = searchConfig.getBearbeitungsbereich();
			milestones = searchConfig.getMilestones();
			viewMode = searchConfig.getViewMode();
			onlyOpenMilestones = searchConfig.getOnlyOpenMilestones();
			sortDirection = searchConfig.getSortDirection();
			sortColumn = searchConfig.getSortColumn();
			letzteXWochen = FrontendHelper.castIntegerToStringStandard(searchConfig
			    .getLetzteXWochen());
			naechsteXWochen = FrontendHelper.castIntegerToStringStandard(searchConfig
			    .getNaechsteXWochen());
			optionDatumZeitraum = searchConfig.getOptionDatumZeitraum();

			// Suchbereich für Arbeitssteuerung auf aktuelle Arbeitswoche
			// (Montag-Freitag)
			// vorbelegen
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			controllingBeginnDatum = FrontendHelper.castDateToString(cal.getTime());

			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
			controllingEndDatum = FrontendHelper.castDateToString(cal.getTime());
		} else {
			reset();
		}
	}

	public SearchConfig fillSearchConfig(SearchConfig searchConfig) {
		if (searchConfig != null) {
			searchConfig.setMasId(masId);
			if (FrontendHelper.stringNotNullOrEmpty(ausfaelle)) {
				searchConfig.setAusfaelle(Boolean.valueOf(ausfaelle));
			} else {
				searchConfig.setAusfaelle(null);
			}
			if (FrontendHelper.stringNotNullOrEmpty(aenderungen)) {
				searchConfig.setAenderungen(Boolean.valueOf(aenderungen));
			} else {
				searchConfig.setAenderungen(null);
			}
			searchConfig.setArtQs(artQs);
			searchConfig.setArtKs(artKs);
			searchConfig.setArtA(artA);
			searchConfig.setArtB(artB);
			searchConfig.setKorridorZeitfenster(korridorZeitfenster);

			searchConfig.setKigBauNr(kigBauNr);
			searchConfig.setKorridorNr(korridorNr);
			searchConfig.setStreckeBBP(streckeBBP);
			searchConfig.setStreckeVZG(streckeVZG);
			searchConfig.setStreckenAbschnitt(streckenAbschnitt);
			searchConfig.setVorgangsNummer(vorgangsNummer);
			searchConfig.setNurAktiv(nurAktiv);
			searchConfig.setFahrplanjahr(fahrplanjahr);
			searchConfig.setBeginnDatum(beginnDatum);
			searchConfig.setEndDatum(endDatum);
			searchConfig.setBauZeitraumVon(bauZeitraumVon);
			searchConfig.setBauZeitraumBis(bauZeitraumBis);
			searchConfig.setBearbeiter(bearbeiter);
			searchConfig.setQsNr(qsNr);
			searchConfig.setFploNr(fploNr);
			searchConfig.setRegionalbereichFpl(regionalbereichFpl);
			searchConfig.setBearbeitungsbereich(bearbeitungsbereich);
			searchConfig.setMilestones(milestones);
			searchConfig.setViewMode(viewMode);
			searchConfig.setOnlyOpenMilestones(onlyOpenMilestones);
			searchConfig.setSortDirection(sortDirection);
			searchConfig.setSortColumn(sortColumn);
			searchConfig.setLetzteXWochen(FrontendHelper.castStringToInteger(letzteXWochen));
			searchConfig.setNaechsteXWochen(FrontendHelper.castStringToInteger(naechsteXWochen));
			searchConfig.setOptionDatumZeitraum(optionDatumZeitraum);
		}
		return searchConfig;
	}

}
