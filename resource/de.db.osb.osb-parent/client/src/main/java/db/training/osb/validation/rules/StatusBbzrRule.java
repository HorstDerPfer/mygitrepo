package db.training.osb.validation.rules;

import org.apache.struts.action.ActionMessage;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.babett.Phase;
import db.training.osb.model.babett.StatusBbzr;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;

/**
 * Status Zugregelung FE/FS darf nur bei Baukapa-Maßnahmen benutzt werden. Es gilt:
 * 
 * <code>({WENN(maßnahme.phase_id <> phasentyp.id[Baukapa1, Baukapa2]} DANN {maßnahme.status_bbzr_id <> status_bbzr.id[FE/FS]}</code>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 12
 */
public class StatusBbzrRule implements SAPMassnahmeValidationRule {

	/**
	 * Kürzel für Phase "Baukapazitätsmanagement I"
	 */
	public static final String PHASE_BAUKAPA1 = "K1";

	/**
	 * Kürzel für Phase "Baukapazitätsmanagement II"
	 */
	public static final String PHASE_BAUKAPA2 = "K2";

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (!FrontendHelper.stringNotNullOrEmpty(form.getStatusBbzr()))
			return;

		StatusBbzr statusBbzr = null;
		try {
			statusBbzr = StatusBbzr.valueOf(form.getStatusBbzr());
		} catch (Exception ex) {
			throw new ValidationAbortedException("StatusBbzr enthält keinen gültigen Wert.", ex);
		}

		// beim Laden der Phase wird vorausgesetzt, dass nur bzgl. ihres Zeitraums gültige Phasen
		// ausgewählt werden konnten!
		Integer phaseId = form.getPhaseId();
		Phase phase = EasyServiceFactory.getInstance().createPhaseService().findById(phaseId);

		if (!phase.getKuerzel().equalsIgnoreCase(PHASE_BAUKAPA1)
		    && !phase.getKuerzel().equalsIgnoreCase(PHASE_BAUKAPA2)) {

			if (statusBbzr == StatusBbzr.FE_FS)
				collector.handleResult(new ValidationResult("statusBbzr", new ActionMessage(
				    "validation.rule.statusbbzr")));
		}
	}
}
