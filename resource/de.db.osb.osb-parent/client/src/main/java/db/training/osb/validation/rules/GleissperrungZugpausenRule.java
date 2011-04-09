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
 * Bei Gleissperrungen, die keine Sperrungen in Zugpausen enthalten, muss die Betroffenheit
 * mindestens eines Verkehrssegments angegeben sein.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 19
 */
public class GleissperrungZugpausenRule implements GleissperrungValidationRule {

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

		// nur Gleissperrungen prüfen, bei denen aus der Betriebsweise zwangsläufig auf
		// Zugbetroffenheit zu schließen ist
		if (!betriebsweise.isZug())
			return;

		if ((gleissperrung.getBetroffenSgv() == null || gleissperrung.getBetroffenSgv() == false)
		    && (gleissperrung.getBetroffenSpfv() == null || gleissperrung.getBetroffenSpfv() == false)
		    && (gleissperrung.getBetroffenSpnv() == null || gleissperrung.getBetroffenSpnv() == false)) {
			collector.handleResult(new ValidationResult("betriebsweiseId", new ActionMessage(
			    "validation.rule.gleissperrungzugpausen")));
		}
	}
}
