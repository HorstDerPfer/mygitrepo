package db.training.osb.validation.rules;

import java.util.Date;

import org.apache.struts.action.ActionMessage;

import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserServiceImpl;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.BetriebsstelleRegionalbereichLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.validation.GleissperrungValidationRule;
import db.training.osb.validation.LangsamfahrstelleValidationRule;
import db.training.osb.validation.OberleitungValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * Mindestens eine Betriebsstelle der Angaben muss im Regionalbereich des eingebenden Benutzers
 * liegen. Diese Regel greift auf den angemeldeten Benutzer zu. Dieser muss zuvor von
 * <code>UserServiceImpl</code> gespeichert werden. Dem Regionalbereich Zentrale (ID:10) sind keine
 * Betriebsstellen zugeordnet. Diese Regel ist für Benutzer aus dem RB Zentrale immer erfolgreich.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 3
 * @see UserServiceImpl.setCurrentApplicationUser
 */
public class BetriebsstelleRegionalbereichRule implements SAPMassnahmeValidationRule,
    GleissperrungValidationRule, OberleitungValidationRule, LangsamfahrstelleValidationRule {

	private final int REGIONALBEREICH_ZENTRALE_ID = 10;

	private void validateIntern(Integer betriebsstelleVonId, Integer regionalbereichId,
	    Integer betriebsstelleBisId, int fahrplanjahr, ResultHandler<ValidationResult> collector)
	    throws ValidationAbortedException {

		Date firstDayOfYear = EasyDateFormat.getFirstDayOfFahrplanjahr(fahrplanjahr);
		Date lastDayOfYear = EasyDateFormat.getLastDayOfFahrplanjahr(fahrplanjahr);

		validateIntern(betriebsstelleVonId, betriebsstelleBisId, regionalbereichId, firstDayOfYear,
		    lastDayOfYear, collector);
	}

	private void validateIntern(Integer betriebsstelleVonId, Integer betriebsstelleBisId,
	    Integer regionalbereichId, Date gueltigVon, Date gueltigBis,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		User applicationUser = UserServiceImpl.getCurrentApplicationUser();

		if (applicationUser == null)
			throw new ValidationAbortedException("applicationUser == null");

		if (applicationUser.getRegionalbereich().getId() == REGIONALBEREICH_ZENTRALE_ID)
			// Es gehören keine Betriebsstellen zur Zentrale.
			return;

		BetriebsstelleService bstService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		BetriebsstelleRegionalbereichLink betriebsstelleVon = bstService.findByRegionalbereich(
		    betriebsstelleVonId, regionalbereichId, gueltigVon, gueltigBis);
		BetriebsstelleRegionalbereichLink betriebsstelleBis = bstService.findByRegionalbereich(
		    betriebsstelleBisId, regionalbereichId, gueltigVon, gueltigBis);

		if (betriebsstelleVon == null
		    || betriebsstelleBis == null
		    || (!betriebsstelleVon.getRegionalbereich()
		        .equals(applicationUser.getRegionalbereich()) && !betriebsstelleBis
		        .getRegionalbereich().equals(applicationUser.getRegionalbereich()))) {

			collector.handleResult(new ValidationResult("betriebsstelleVon", new ActionMessage(
			    "validation.rule.betriebsstelleregionalbereich")));
		}
	}

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || FrontendHelper.stringNotNullOrEmpty(form.getEnde())) {
			validateIntern(form.getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), form
			    .getRegionalbereichId(), FrontendHelper.castStringToDate(form.getBeginn()),
			    FrontendHelper.castStringToDate(form.getEnde()), collector);
		} else {
			validateIntern(form.getBetriebsstelleVonId(), form.getBetriebsstelleBisId(), form
			    .getRegionalbereichId(), Integer.parseInt(form.getFahrplanjahr()), collector);
		}
	}

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {
		// TODO Auto-generated method stub

	}

}
