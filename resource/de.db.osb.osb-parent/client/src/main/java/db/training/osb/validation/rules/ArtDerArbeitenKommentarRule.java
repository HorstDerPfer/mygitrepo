/**
 * 
 */
package db.training.osb.validation.rules;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.babett.Arbeitstyp;
import db.training.osb.service.ArbeitstypService;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;

/**
 * Ist bei der ausgewählten Art der Arbeiten eine zusätzliche Textinformation gefordert, darf das
 * zugehörige Textfeld nicht leer sein.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 4
 */
public class ArtDerArbeitenKommentarRule implements SAPMassnahmeValidationRule {

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		ArbeitstypService service = EasyServiceFactory.getInstance().createArbeitstypService();
		Arbeitstyp arbeitstyp = service.findById(form.getArbeitenId());

		if (arbeitstyp == null)
			return;

		if (arbeitstyp.isTextErforderlich()
		    && !FrontendHelper.stringNotNullOrEmpty(form.getArbeitenKommentar())) {
			MessageResources res = MessageResources.getMessageResources("MessageResources");

			collector.handleResult(new ValidationResult("arbeitenKommentar", new ActionMessage(
			    "error.required", res.getMessage("sperrpausenbedarf.arbeitenKommentar"))));
		}
	}

}
