/**
 * 
 */
package db.training.osb.validation.rules;

import org.apache.struts.action.ActionMessage;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.Betriebsweise;
import db.training.osb.model.Gleissperrung;
import db.training.osb.validation.GleissperrungValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * Bei Totalsperrungen muss die Richtungsangabe immer = 0 sein.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 20
 */
public class TotalsperrungRule implements GleissperrungValidationRule {

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Betriebsweise betriebsweise = EasyServiceFactory.getInstance().createBetriebsweiseService()
		    .findById(form.getBetriebsweiseId());

		if (betriebsweise == null) {
			collector.handleResult(new ValidationResult("betriebsweiseId", new ActionMessage(
			    "error.invalid")));
			return;
		}

		// nur Totalsperrungen prüfen
		if (!betriebsweise.isTotalsperrung())
			return;

		if (form.getRichtungskennzahl() == null || form.getRichtungskennzahl() != 0) {
			collector.handleResult(new ValidationResult("richtungskennzahl", new ActionMessage(
			    "validation.rule.totalsperrung")));
		}
	}
}
