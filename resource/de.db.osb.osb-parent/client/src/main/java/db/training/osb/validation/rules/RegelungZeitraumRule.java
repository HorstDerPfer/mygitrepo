package db.training.osb.validation.rules;

import java.util.Calendar;

import org.apache.struts.action.ActionMessage;

import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.validation.GleissperrungValidationRule;
import db.training.osb.validation.LangsamfahrstelleValidationRule;
import db.training.osb.validation.OberleitungValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * <ol start="14">
 * <li>Der früheste Beginn für eine Regelung liegt max. 48 Stunden vor Maßnahmebeginn.</li>
 * <li>Das späteste Ende einer Regelung darf max. 2 Wochen nach Ende der Baumaßnahme liegen.</li>
 * <li>Eine Regelung darf nicht vor Beginn der Baumaßnahme enden.</li>
 * <li>Eine Regelung darf nicht nach Ende der Baumaßnahme beginnen.<br/>
 * Ausnahme: Langsamfahrstelle</li>
 * </ol>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 14, 15, 16, 17
 */
public class RegelungZeitraumRule implements GleissperrungValidationRule,
    LangsamfahrstelleValidationRule, OberleitungValidationRule {

	private void validateIntern(Regelung regelung, ResultHandler<ValidationResult> collector)
	    throws ValidationAbortedException {

		if (regelung.getMassnahme() == null)
			throw new ValidationAbortedException(String.format(
			    "Zu dieser Regelung existiert keine Baumaßnahme. ID: %s", regelung.getId()));

		SAPMassnahme massnahme = regelung.getMassnahme();

		// frühesten Starttermin für Regelung berechnen
		Calendar minBeginn = Calendar.getInstance();
		minBeginn.setTime(massnahme.getBauterminStart());
		minBeginn.roll(Calendar.MINUTE, -2881);

		// spätesten Endtermin für Regelung berechnen
		Calendar maxEnde = Calendar.getInstance();
		maxEnde.setTime(massnahme.getBauterminEnde());
		maxEnde.roll(Calendar.HOUR, 14 * 24);

		if (regelung.getZeitVon().before(minBeginn.getTime()))
			collector.handleResult(new ValidationResult("zeitVon", new ActionMessage(
			    "validation.rule.regelungzeitraumrule.minbeginn")));

		if (regelung.getZeitBis().after(maxEnde.getTime()))
			collector.handleResult(new ValidationResult("zeitBis", new ActionMessage(
			    "validation.rule.regelungzeitraumrule.maxende")));

		if (regelung.getZeitBis().before(massnahme.getBauterminStart()))
			collector.handleResult(new ValidationResult("zeitBis", new ActionMessage(
			    "validation.rule.regelungzeitraumrule.endevormassnahme")));

		if (!(regelung instanceof Langsamfahrstelle)
		    && regelung.getZeitVon().after(massnahme.getBauterminEnde()))
			collector.handleResult(new ValidationResult("zeitVon", new ActionMessage(
			    "validation.rule.regelungzeitraumrule.beginnnachmassnahme")));
	}

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(gleissperrung, collector);
	}

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(langsamfahrstelle, collector);
	}

	@Override
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		validateIntern(oberleitung, collector);
	}

}
