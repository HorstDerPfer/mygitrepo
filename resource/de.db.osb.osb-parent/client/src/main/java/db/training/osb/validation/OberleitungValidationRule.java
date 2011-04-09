package db.training.osb.validation;

import db.training.osb.model.Oberleitung;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public interface OberleitungValidationRule {
	
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException;
	
}
