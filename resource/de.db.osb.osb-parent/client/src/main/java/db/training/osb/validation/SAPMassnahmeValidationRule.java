package db.training.osb.validation;

import db.training.osb.model.SAPMassnahme;
import db.training.osb.web.massnahme.MassnahmeForm;

public interface SAPMassnahmeValidationRule {

	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException;

}
