package db.training.osb.validation.rules;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts.action.ActionMessage;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.babett.Phase;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;

/**
 * C-Maßnahmen dürfen mit keinem längeren Vorlauf als 26 Wochen eingegeben werden. Es gilt:
 * massnahme.beginn <= NOW + 184 Tage
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 1
 */
public class CPhaseVorlaufRule implements SAPMassnahmeValidationRule {

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Phase phase = EasyServiceFactory.getInstance().createPhaseService().findById(
		    form.getPhaseId());
		if (phase == null || !phase.getKuerzel().equalsIgnoreCase("C"))
			return;

		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, 184);

		Date massnahmeBeginn = FrontendHelper.castStringToDate(form.getBeginn());

		if (massnahmeBeginn.after(now.getTime())) {
			// TODO: Fehlermeldung !
			collector.handleResult(new ValidationResult("beginn", new ActionMessage(
			    "common.impossible")));
		}
	}
}