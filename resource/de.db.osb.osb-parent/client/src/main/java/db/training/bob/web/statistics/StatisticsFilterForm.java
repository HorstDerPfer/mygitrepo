package db.training.bob.web.statistics;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class StatisticsFilterForm extends ActionForm {

	private String art = null;

	private String kigBauNr = null;

	private String korridorNr = null;

	private String qsNr = null;

	private String beginnDatum = null;

	private String endDatum = null;

	private Boolean nurAktiv;

	private String bauZeitraumVon, bauZeitraumBis;

	private String evu = null;

	private String regionalbereichFpl = null;

	private String controllingBeginnDatum = null;

	private String controllingEndDatum = null;

	private String[] milestones = null;

	private String bearbeitungsbereich;

	private String regionalbereichBM = null;

	private String zeitraumVerkehrstagVon;

	private String zeitraumVerkehrstagBis;

	private String qsks;

	private String letzteXWochen;

	private String naechsteXWochen;

	private String optionDatumZeitraum;

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

	public void reset() {
		setArt(null);
		setKigBauNr(null);
		setKorridorNr(null);
		setQsNr(null);
		setBeginnDatum(null);
		setEndDatum(null);
		setEvu("pevu"); // pevu oder gevu
		setRegionalbereichFpl(null);

		setBauZeitraumVon(null);
		setBauZeitraumBis(null);

		setRegionalbereichBM(null);
		setZeitraumVerkehrstagVon(null);
		setZeitraumVerkehrstagBis(null);
		setQsks("-1");

		// Suchbereich für Arbeitssteuerung auf aktuelle Arbeitswoche (Montag-Freitag)
		// vorbelegen
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		controllingBeginnDatum = FrontendHelper.castDateToString(cal.getTime());

		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
		controllingEndDatum = FrontendHelper.castDateToString(cal.getTime());
		setOptionDatumZeitraum("datum");

		setBearbeitungsbereich(null);
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		resetProperty(arg1, this, "milestones", null);
		resetProperty(arg1, this, "nurAktiv", null);
	}

	private void resetProperty(HttpServletRequest request, ActionForm form, String property,
	    Object value) {
		// super.reset Workaround
		// Checkboxen zurücksetzen, wenn keine Box ausgewählt ist, und request von eigener URI
		// kommt

		Logger log = Logger.getLogger(this.getClass());

		try {
			if (request.getParameter(property) == null && request.getHeader("referer") != null
			    && request.getHeader("referer").indexOf(request.getRequestURI()) >= 0) {
				PropertyUtils.setProperty(form, property, value);
				if (log.isDebugEnabled())
					log.debug(property + " reset.");
			}
		} catch (NoSuchMethodException ex) {
			if (log.isDebugEnabled())
				log.debug("Property not found: " + property);
			if (log.isDebugEnabled())
				log.debug(ex.getMessage());
		} catch (IllegalAccessException ex) {
			if (log.isDebugEnabled())
				log.debug(ex.getMessage(), ex);
		} catch (InvocationTargetException ex) {
			if (log.isDebugEnabled())
				log.debug(ex.getMessage(), ex);
		}
	}

	/**
	 * @param art
	 *            the art to set
	 */
	public void setArt(String art) {
		this.art = art;
	}

	/**
	 * @return the art
	 */
	public String getArt() {
		return art;
	}

	/**
	 * @param kigBauNr
	 *            the kigBauNr to set
	 */
	public void setKigBauNr(String kigBauNr) {
		this.kigBauNr = kigBauNr;
	}

	/**
	 * @return the kigBauNr
	 */
	public String getKigBauNr() {
		return kigBauNr;
	}

	/**
	 * @param korridorNr
	 *            the korridorNr to set
	 */
	public void setKorridorNr(String korridorNr) {
		this.korridorNr = korridorNr;
	}

	/**
	 * @return the korridorNr
	 */
	public String getKorridorNr() {
		return korridorNr;
	}

	/**
	 * @param qsNr
	 *            the qsNr to set
	 */
	public void setQsNr(String qsNr) {
		this.qsNr = qsNr;
	}

	/**
	 * @return the qsNr
	 */
	public String getQsNr() {
		return qsNr;
	}

	/**
	 * @param beginnDatum
	 *            the beginnDatum to set
	 */
	public void setBeginnDatum(String beginnDatum) {
		this.beginnDatum = beginnDatum;
	}

	/**
	 * @return the beginnDatum
	 */
	public String getBeginnDatum() {
		return beginnDatum;
	}

	/**
	 * @param endDatum
	 *            the endDatum to set
	 */
	public void setEndDatum(String endDatum) {
		this.endDatum = endDatum;
	}

	/**
	 * @return the endDatum
	 */
	public String getEndDatum() {
		return endDatum;
	}

	/**
	 * @param evu
	 *            the evu to set
	 */
	public void setEvu(String evu) {
		this.evu = evu;
	}

	/**
	 * @return the evu
	 */
	public String getEvu() {
		return evu;
	}

	/**
	 * @param regionalbereichFpl
	 *            the regionalbereichFpl to set
	 */
	public void setRegionalbereichFpl(String regionalbereichFpl) {
		this.regionalbereichFpl = regionalbereichFpl;
	}

	/**
	 * @return the regionalbereichFpl
	 */
	public String getRegionalbereichFpl() {
		return regionalbereichFpl;
	}

	/**
	 * @param controllingBeginnDatum
	 *            the controllingBeginnDatum to set
	 */
	public void setControllingBeginnDatum(String controllingBeginnDatum) {
		this.controllingBeginnDatum = controllingBeginnDatum;
	}

	/**
	 * @return the controllingBeginnDatum
	 */
	public String getControllingBeginnDatum() {
		return controllingBeginnDatum;
	}

	/**
	 * @param controllingEndDatum
	 *            the controllingEndDatum to set
	 */
	public void setControllingEndDatum(String controllingEndDatum) {
		this.controllingEndDatum = controllingEndDatum;
	}

	/**
	 * @return the controllingEndDatum
	 */
	public String getControllingEndDatum() {
		return controllingEndDatum;
	}

	/**
	 * @param milestones
	 *            the milestones to set
	 */
	public void setMilestones(String[] milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the milestones
	 */
	public String[] getMilestones() {
		return milestones;
	}

	/**
	 * @param bearbeitungsbereich
	 *            the bearbeitungsbereich to set
	 */
	public void setBearbeitungsbereich(String bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	/**
	 * @return the bearbeitungsbereich
	 */
	public String getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	public Boolean getNurAktiv() {
		return nurAktiv;
	}

	public void setNurAktiv(Boolean nurAktiv) {
		this.nurAktiv = nurAktiv;
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

	public String getRegionalbereichBM() {
		return regionalbereichBM;
	}

	public void setRegionalbereichBM(String regionalbereichBM) {
		this.regionalbereichBM = regionalbereichBM;
	}

	public String getZeitraumVerkehrstagVon() {
		return zeitraumVerkehrstagVon;
	}

	public void setZeitraumVerkehrstagVon(String zeitraumVerkehrstagVon) {
		this.zeitraumVerkehrstagVon = zeitraumVerkehrstagVon;
	}

	public String getZeitraumVerkehrstagBis() {
		return zeitraumVerkehrstagBis;
	}

	public void setZeitraumVerkehrstagBis(String zeitraumVerkehrstagBis) {
		this.zeitraumVerkehrstagBis = zeitraumVerkehrstagBis;
	}

	public String getQsks() {
		return qsks;
	}

	public void setQsks(String qsks) {
		this.qsks = qsks;
	}

	public void setOptionDatumZeitraum(String optionDatumZeitraum) {
		this.optionDatumZeitraum = optionDatumZeitraum;
	}

	public String getOptionDatumZeitraum() {
		return optionDatumZeitraum;
	}
}
