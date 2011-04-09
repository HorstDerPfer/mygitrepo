/**
 * 
 */
package db.training.osb.validation.rules;

import org.apache.struts.action.ActionMessage;

import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.validation.LangsamfahrstelleValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * Bei Langsamfahrstellen darf die La Geschwindigkeit nicht größer als (V(VzG) - 5km/h) sein.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 18
 */
public class LaGeschwindigkeitRule implements LangsamfahrstelleValidationRule {

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (!FrontendHelper.stringNotNullOrEmpty(form.getGeschwindigkeitVzg())) {
			collector.handleResult(new ValidationResult("geschwindigkeitVzg", new ActionMessage(
			    "error.required")));
			return;
		}

		float geschwindigkeitVzg = FrontendHelper.castStringToFloat(form.getGeschwindigkeitVzg());
		float geschwindigkeitLa = FrontendHelper.castStringToFloat(form.getGeschwindigkeitLa());

		if ((geschwindigkeitVzg - geschwindigkeitLa) < 5) {
			collector.handleResult(new ValidationResult("geschwindigkeitLa", new ActionMessage(
			    "validation.rule.lageschwindigkeit")));
		}
	}
}
