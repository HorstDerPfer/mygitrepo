package db.training.osb.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.Gleissperrung;
import db.training.osb.validation.rules.BetriebsstellenfolgeRule;
import db.training.osb.validation.rules.BetriebsweiseRule;
import db.training.osb.validation.rules.GleissperrungZugpausenRule;
import db.training.osb.validation.rules.MassnahmeZeitraumRule;
import db.training.osb.validation.rules.RegelungZeitraumRule;
import db.training.osb.validation.rules.VerkehrstageregelungRule;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public class GleissperrungValidator {
	private static final Logger log = LoggerFactory.getLogger(GleissperrungValidator.class);

	private List<GleissperrungValidationRule> rules;

	public GleissperrungValidator() {
		this(new ArrayList<GleissperrungValidationRule>());
		rules.add(new VerkehrstageregelungRule());
		rules.add(new RegelungZeitraumRule());
		rules.add(new MassnahmeZeitraumRule());
		rules.add(new BetriebsweiseRule());
		// rules.add(new BetriebsstelleRegionalbereichRule());
		rules.add(new BetriebsstellenfolgeRule());
		rules.add(new GleissperrungZugpausenRule());
	}

	public GleissperrungValidator(List<GleissperrungValidationRule> rules) {
		this.rules = rules;
	}

	public void setRules(List<GleissperrungValidationRule> rules) {
		this.rules = rules;
	}

	private List<ValidationResult> validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form) {
		ValidationResultCollector collector = new ValidationResultCollector();

		try {
			for (GleissperrungValidationRule rule : rules) {
				rule.validate(gleissperrung, form, collector);
			}
		} catch (ValidationAbortedException ex) {
			log.error("Fehler bei der Validierung der Gleissperrung.", ex);
		}

		return collector.getResults();
	}

	public static ActionErrors validate(MassnahmeRegelungForm form) {
		GleissperrungValidator validator = new GleissperrungValidator();

		List<ValidationResult> results = validator.validate(EasyServiceFactory.getInstance()
		    .createGleissperrungService().findById(form.getRegelungId()), form);

		ActionErrors actionErrors = new ActionErrors();
		for (ValidationResult item : results) {
			actionErrors.add(item.getProperty(), item.getActionError());
		}

		return actionErrors;
	}
}
