package db.training.osb.validation;

import db.training.osb.model.Gleissperrung;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public interface GleissperrungValidationRule {

	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException;

}
