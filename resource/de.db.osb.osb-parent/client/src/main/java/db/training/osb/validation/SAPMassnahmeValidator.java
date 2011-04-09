package db.training.osb.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.validation.rules.ArtDerArbeitenKommentarRule;
import db.training.osb.validation.rules.ArtDerArbeitenOrtRule;
import db.training.osb.validation.rules.BetriebsstelleRegionalbereichRule;
import db.training.osb.validation.rules.BetriebsstellenfolgeRule;
import db.training.osb.validation.rules.CPhaseVorlaufRule;
import db.training.osb.validation.rules.MassnahmeZeitraumRule;
import db.training.osb.validation.rules.StatusBbzrRule;
import db.training.osb.validation.rules.VerkehrstageregelungRule;
import db.training.osb.web.massnahme.MassnahmeForm;

public class SAPMassnahmeValidator {

	private static final Logger log = LoggerFactory.getLogger(SAPMassnahmeValidator.class);

	private List<SAPMassnahmeValidationRule> rules;

	public SAPMassnahmeValidator() {
		this(new ArrayList<SAPMassnahmeValidationRule>());
		rules.add(new ArtDerArbeitenKommentarRule());
		rules.add(new ArtDerArbeitenOrtRule());
		rules.add(new BetriebsstellenfolgeRule());
		rules.add(new BetriebsstelleRegionalbereichRule());
		rules.add(new CPhaseVorlaufRule());
		rules.add(new MassnahmeZeitraumRule());
		rules.add(new StatusBbzrRule());
		rules.add(new VerkehrstageregelungRule());
	}

	public SAPMassnahmeValidator(List<SAPMassnahmeValidationRule> rules) {
		this.rules = rules;
	}

	public void setRules(List<SAPMassnahmeValidationRule> rules) {
		this.rules = rules;
	}

	private List<ValidationResult> validate(SAPMassnahme massnahme, MassnahmeForm form) {
		ValidationResultCollector collector = new ValidationResultCollector();

		try {
			for (SAPMassnahmeValidationRule rule : rules) {
				rule.validate(massnahme, form, collector);
			}
		} catch (ValidationAbortedException ex) {
			log.error("Fehler bei der Validierung der Maßnahme.", ex);
		}

		return collector.getResults();
	}

	public static ActionErrors validate(MassnahmeForm form) {
		SAPMassnahmeValidator validator = new SAPMassnahmeValidator();

		List<ValidationResult> results = validator.validate(EasyServiceFactory.getInstance()
		    .createSAPMassnahmeService().findById(form.getMassnahmeId()), form);

		ActionErrors actionErrors = new ActionErrors();
		for (ValidationResult item : results) {
			actionErrors.add(item.getProperty(), item.getActionError());
		}

		return actionErrors;
	}
}
