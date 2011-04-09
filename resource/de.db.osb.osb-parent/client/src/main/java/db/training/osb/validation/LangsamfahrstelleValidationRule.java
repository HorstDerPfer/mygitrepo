package db.training.osb.validation;

import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public interface LangsamfahrstelleValidationRule {

	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException;

}
