/**
 * 
 */
package db.training.osb.validation.rules;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.SAPMassnahme;
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
 * <ol start="6">
 * <li>Zwischen Beginn und Ende von Maßnahmen und Regelungen müssen mindestens 10 Minuten liegen.</li>
 * <li>Das Ende von Regelungen muss später als der Beginn liegen. (durch Regel 6 erfüllt.)</li>
 * <li>Das Ende von Maßnahmen und Regelungen darf nicht am oder vor dem aktuellen Tag liegen.</li>
 * <li>Das Ende von Baumaßnahmen darf nicht größer als aktuelles Datum + 10 Jahre (23:59:59) sein.</li>
 * </ol>
 * 
 * <ol start="24">
 * <li>Zwischen Beginn und Ende einer Maßnahme oder einer Regelung dürfen maximal 373 Tage liegen.
 * (= max. Länge eines Fahrplanjahres)</li>
 * <li>Der Zeitraum zwischen Beginn und Ende einer Maßnahme oder Regelung darf keinen
 * Fahrplanwechseltermin enthalten.</li>
 * </ol>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 6, 7, 8, 9, 24, 25
 */
public class MassnahmeZeitraumRule implements GleissperrungValidationRule,
    SAPMassnahmeValidationRule, OberleitungValidationRule, LangsamfahrstelleValidationRule {

	private void validateIntern(Date beginnDate, Date endeDate,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		MessageResources res = MessageResources.getMessageResources("MessageResources");

		Calendar beginn = Calendar.getInstance();
		Calendar ende = Calendar.getInstance();

		// Startzeit + 10 Minuten
		beginn.setTime(beginnDate);
		beginn.roll(Calendar.MINUTE, 10);

		ende.setTime(endeDate);

		// (Beginn + 10 Minuten) > Ende (Regel 6)
		if (beginn.after(ende)) {
			collector.handleResult(new ValidationResult("beginn", new ActionMessage(
			    "validation.rule.massnahmezeitraumrule")));
		}

		// Ende > (heute 23:59:59) (Regel 8)
		Calendar minEnde = Calendar.getInstance();
		EasyDateFormat.setToEndOfDay(minEnde);
		if (ende.before(minEnde)) {
			collector.handleResult(new ValidationResult("ende", new ActionMessage("error.invalid",
			    res.getMessage("sperrpausenbedarf.bautermin.ende"))));
		}

		// Ende < (heute + 10 Jahre) (Regel 9)
		Calendar maxEnde = Calendar.getInstance();
		maxEnde.roll(Calendar.YEAR, 10);
		EasyDateFormat.setToEndOfDay(maxEnde);
		if (ende.after(maxEnde)) {
			collector.handleResult(new ValidationResult("ende", new ActionMessage("error.invalid",
			    res.getMessage("sperrpausenbedarf.bautermin.ende"))));
		}

		// Dauer < max. Länge eines Fahrplanjahres prüfen
		beginn.setTime(beginnDate);
		beginn.roll(Calendar.HOUR, 24 * 373);
		ende.setTime(endeDate);
		if (beginn.after(ende)) {
			collector.handleResult(new ValidationResult(null, new ActionMessage(
			    "validation.rule.massnahmezeitraumrule.fahrplanjahr")));
		}

		// TODO: Berechnung des Fahrplanjahr-Wechseltermins klären!
	}

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}
}