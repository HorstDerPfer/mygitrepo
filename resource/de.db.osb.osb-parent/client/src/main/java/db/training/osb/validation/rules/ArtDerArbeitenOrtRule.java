/**
 * 
 */
package db.training.osb.validation.rules;

import java.util.Date;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.Arbeitstyp;
import db.training.osb.service.ArbeitstypService;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.SAPMassnahmeValidationRule;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeForm;

/**
 * Ist bei der ausgewählten Art der Arbeiten eine zusätzliche Ortsinformation gefordert, muss der
 * angegebene Ort im Bauabschnitt liegen. Es gilt:
 * <code>BetriebsstelleVon.Strecke.km <= ArbeitenBst.Strecke.km <= BetriebsstelleBis.Strecke.km</code>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 5
 */
public class ArtDerArbeitenOrtRule implements SAPMassnahmeValidationRule {

	private MessageResources msgRes = MessageResources.getMessageResources("MessageResources");

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		if (form.getArbeitenId() == null)
			return;

		ArbeitstypService arbeitstypService = EasyServiceFactory.getInstance()
		    .createArbeitstypService();
		Arbeitstyp arbeiten = arbeitstypService.findById(form.getArbeitenId());

		if (arbeiten == null)
			throw new ValidationAbortedException("Arbeitstyp nicht gefunden. ID:"
			    + form.getArbeitenId());

		if (!arbeiten.isOrtErforderlich())
			// Wenn die Ortsangabe nicht erforderlich ist, wird keine weitere Prüfung durchgeführt.
			return;

		if (form.getBetriebsstelleVonId() == null || form.getBetriebsstelleBisId() == null)
			throw new ValidationAbortedException("Es wurden nicht alle Betriebsstellen angegeben");

		if (form.getStrecke() == null)
			throw new ValidationAbortedException("Streckennummer nicht angegeben.");

		if (form.getArbeitenOrtId() == null)
			collector.handleResult(new ValidationResult("arbeitenOrtId", new ActionMessage(
			    "error.required", msgRes.getMessage("sperrpausenbedarf.arbeitenOrt"))));

		if (!FrontendHelper.stringNotNullOrEmpty(form.getBeginn())
		    || !FrontendHelper.stringNotNullOrEmpty(form.getEnde()))
			throw new ValidationAbortedException("Kein Bauzeitraum angegeben.");

		Date von = FrontendHelper.castStringToDate(form.getBeginn());
		Date bis = FrontendHelper.castStringToDate(form.getEnde());

		Integer vzgStreckeNummer = VzgStrecke.getId(form.getStrecke());

		BetriebsstelleService betriebsstelleService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();
		BetriebsstelleVzgStreckeLink bstStreckeVon = betriebsstelleService.findByVzgStrecke(form
		    .getBetriebsstelleVonId(), vzgStreckeNummer, von, bis);
		BetriebsstelleVzgStreckeLink bstStreckeBis = betriebsstelleService.findByVzgStrecke(form
		    .getBetriebsstelleBisId(), vzgStreckeNummer, von, bis);
		BetriebsstelleVzgStreckeLink bstStreckeOrt = betriebsstelleService.findByVzgStrecke(form
		    .getArbeitenOrtId(), vzgStreckeNummer, von, bis);

		if (!(bstStreckeVon.getKm() <= bstStreckeOrt.getKm() && bstStreckeOrt.getKm() <= bstStreckeBis
		    .getKm())) {
			collector.handleResult(new ValidationResult("arbeitenOrtId", new ActionMessage(
			    "validation.rule.artderarbeitenort")));
		}
	}

}
