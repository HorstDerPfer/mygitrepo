package db.training.osb.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.Oberleitung;
import db.training.osb.validation.rules.BetriebsstellenfolgeRule;
import db.training.osb.validation.rules.MassnahmeZeitraumRule;
import db.training.osb.validation.rules.RegelungZeitraumRule;
import db.training.osb.validation.rules.VerkehrstageregelungRule;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public class OberleitungValidator {
	private static final Logger log = LoggerFactory.getLogger(OberleitungValidator.class);

	private List<OberleitungValidationRule> rules;

	public OberleitungValidator() {
		this(new ArrayList<OberleitungValidationRule>());
		rules.add(new VerkehrstageregelungRule());
		rules.add(new RegelungZeitraumRule());
		rules.add(new MassnahmeZeitraumRule());
		// rules.add(new BetriebsstelleRegionalbereichRule());
		rules.add(new BetriebsstellenfolgeRule());
	}

	public OberleitungValidator(List<OberleitungValidationRule> rules) {
		this.rules = rules;
	}

	public void setRules(List<OberleitungValidationRule> rules) {
		this.rules = rules;
	}

	private List<ValidationResult> validate(Oberleitung oberleitung, MassnahmeRegelungForm form) {
		ValidationResultCollector collector = new ValidationResultCollector();

		try {
			for (OberleitungValidationRule rule : rules) {
				rule.validate(oberleitung, form, collector);
			}
		} catch (ValidationAbortedException ex) {
			log.error("Fehler bei der Validierung der Oberleitung.", ex);
		}

		return collector.getResults();
	}

	public static ActionErrors validate(MassnahmeRegelungForm form) {
		OberleitungValidator validator = new OberleitungValidator();

		List<ValidationResult> results = validator.validate(EasyServiceFactory.getInstance()
		    .createOberleitungService().findById(form.getRegelungId()), form);

		ActionErrors actionErrors = new ActionErrors();
		for (ValidationResult item : results) {
			actionErrors.add(item.getProperty(), item.getActionError());
		}

		return actionErrors;
	}
}
