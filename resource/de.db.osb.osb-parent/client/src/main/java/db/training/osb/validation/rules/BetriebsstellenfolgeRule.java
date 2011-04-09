package db.training.osb.validation.rules;

import java.util.Date;

import org.apache.struts.action.ActionMessage;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.validation.GleissperrungValidationRule;
import db.training.osb.validation.LangsamfahrstelleValidationRule;
import db.training.osb.validation.OberleitungValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * Richtungsangabe und Betriebsstellenfolge dürfen sich nicht widersprechen:
 * <ol>
 * <li>Richtungskennzahl = 0: keine Prüfung, Betriebsstellenfolge beliebig</li>
 * <li>Richtungskennzahl = 1: entweder Betriebsstelle von = Betriebsstelle bis, oder km
 * (Betriebsstelle von) <b><</b> km (Betriebsstelle bis)
 * <li>Richtungskennzahl = 2: entweder Betriebsstelle von = Betriebsstelle bis, oder km
 * (Betriebsstelle von) <b>></b> km (Betriebsstelle bis)
 * </ol>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 2
 * 
 */
public class BetriebsstellenfolgeRule implements SAPMassnahmeValidationRule,
    GleissperrungValidationRule, LangsamfahrstelleValidationRule, OberleitungValidationRule {

	private void validateIntern(Integer richtungskennzahl, Integer vzgStreckeId,
	    Integer betriebsstelleVonId, Integer betriebsstelleBisId, int fahrplanjahr,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Date firstDayOfYear = EasyDateFormat.getFirstDayOfYear(fahrplanjahr);
		Date lastDayOfYear = EasyDateFormat.getLastDayOfYear(fahrplanjahr);

		validateIntern(richtungskennzahl, vzgStreckeId, betriebsstelleVonId, betriebsstelleBisId,
		    firstDayOfYear, lastDayOfYear, collector);
	}

	private void validateIntern(Integer richtungskennzahl, Integer vzgStreckeId,
	    Integer betriebsstelleVonId, Integer betriebsstelleBisId, Date gueltigVon, Date gueltigBis,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (richtungskennzahl == null || richtungskennzahl == 0) {
			return;
		} else if (richtungskennzahl == 2) {
			// Wenn die Richtungskennzahl = "Gegenrichtung", dann Betriebsstellen für Prüfung
			// tauschen
			Integer temp = betriebsstelleVonId;
			betriebsstelleVonId = betriebsstelleBisId;
			betriebsstelleBisId = temp;
		} else if (richtungskennzahl > 2) {
			// Fehlermeldung ausgeben
			collector.handleResult(new ValidationResult("richtungskennzahl", new ActionMessage(
			    "common.impossible")));
		}

		// Betriebsstellen laden
		BetriebsstelleService service = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();
		BetriebsstelleVzgStreckeLink von = service.findByVzgStrecke(betriebsstelleVonId,
		    vzgStreckeId, gueltigVon, gueltigBis);
		BetriebsstelleVzgStreckeLink bis = service.findByVzgStrecke(betriebsstelleBisId,
		    vzgStreckeId, gueltigVon, gueltigBis);

		// Fehlermeldung ausgeben, wenn "Bst von" im Streckenverlauf nach "Bst bis" liegt
		if (von.getKm() > bis.getKm()) {
			collector.handleResult(new ValidationResult("richtungskennzahl", new ActionMessage(
			    "validation.rule.betriebsstellenfolge")));
		}
	}

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || FrontendHelper.stringNotNullOrEmpty(form.getEnde())) {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), FrontendHelper
			    .castStringToDate(form.getBeginn()), FrontendHelper
			    .castStringToDate(form.getEnde()), collector);
		} else {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), Integer.parseInt(form
			    .getFahrplanjahr()), collector);
		}
	}

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || FrontendHelper.stringNotNullOrEmpty(form.getEnde())) {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), FrontendHelper
			    .castStringToDate(form.getBeginn()), FrontendHelper
			    .castStringToDate(form.getEnde()), collector);
		} else {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), gleissperrung
			    .getFahrplanjahr(), collector);
		}
	}

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || FrontendHelper.stringNotNullOrEmpty(form.getEnde())) {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), FrontendHelper
			    .castStringToDate(form.getBeginn()), FrontendHelper
			    .castStringToDate(form.getEnde()), collector);
		} else {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), langsamfahrstelle
			    .getFahrplanjahr(), collector);
		}
	}

	@Override
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || FrontendHelper.stringNotNullOrEmpty(form.getEnde())) {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), FrontendHelper
			    .castStringToDate(form.getBeginn()), FrontendHelper
			    .castStringToDate(form.getEnde()), collector);
		} else {
			validateIntern(form.getRichtungskennzahl(), VzgStrecke.getId(form.getStrecke()), form
			    .getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), oberleitung
			    .getFahrplanjahr(), collector);
		}
	}
}
