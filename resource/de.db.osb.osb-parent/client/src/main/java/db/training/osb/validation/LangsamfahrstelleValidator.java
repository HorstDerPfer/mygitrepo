package db.training.osb.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.validation.rules.BetriebsstellenfolgeRule;
import db.training.osb.validation.rules.LaGeschwindigkeitRule;
import db.training.osb.validation.rules.MassnahmeZeitraumRule;
import db.training.osb.validation.rules.RegelungZeitraumRule;
import db.training.osb.validation.rules.VerkehrstageregelungRule;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

public class LangsamfahrstelleValidator {
	private static final Logger log = LoggerFactory.getLogger(LangsamfahrstelleValidator.class);

	private List<LangsamfahrstelleValidationRule> rules;

	public LangsamfahrstelleValidator() {
		this(new ArrayList<LangsamfahrstelleValidationRule>());
		rules.add(new VerkehrstageregelungRule());
		rules.add(new RegelungZeitraumRule());
		rules.add(new MassnahmeZeitraumRule());
		// rules.add(new BetriebsstelleRegionalbereichRule());
		rules.add(new BetriebsstellenfolgeRule());
		rules.add(new LaGeschwindigkeitRule());
	}

	public LangsamfahrstelleValidator(List<LangsamfahrstelleValidationRule> rules) {
		this.rules = rules;
	}

	public void setRules(List<LangsamfahrstelleValidationRule> rules) {
		this.rules = rules;
	}

	private List<ValidationResult> validate(Langsamfahrstelle langsamfahrstelle,
	    MassnahmeRegelungForm form) {
		ValidationResultCollector collector = new ValidationResultCollector();

		try {
			for (LangsamfahrstelleValidationRule rule : rules) {
				rule.validate(langsamfahrstelle, form, collector);
			}
		} catch (ValidationAbortedException ex) {
			log.error("Fehler bei der Validierung der Langsamfahrstelle.", ex);
		}

		return collector.getResults();
	}

	public static ActionErrors validate(MassnahmeRegelungForm form) {
		LangsamfahrstelleValidator validator = new LangsamfahrstelleValidator();

		List<ValidationResult> results = validator.validate(EasyServiceFactory.getInstance()
		    .createLangsamfahrstelleService().findById(form.getRegelungId()), form);

		ActionErrors actionErrors = new ActionErrors();
		for (ValidationResult item : results) {
			actionErrors.add(item.getProperty(), item.getActionError());
		}

		return actionErrors;
	}
}
