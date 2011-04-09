package db.training.osb.validation.rules;

import java.util.Date;

import org.apache.struts.action.ActionMessage;

import db.training.easy.common.PersistenceException;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Betriebsstellentyp;
import db.training.osb.model.Betriebsweise;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.validation.GleissperrungValidationRule;
import db.training.osb.validation.ResultHandler;
import db.training.osb.validation.ValidationAbortedException;
import db.training.osb.validation.ValidationResult;
import db.training.osb.web.massnahme.MassnahmeRegelungForm;

/**
 * Ist bei einer Betriebsweise angegeben, dass sie nur beim Vorhandensein freier Strecke angewendet
 * werden darf, dann müssen <code>Betriebsstelle von</code> und <code>Betriebsstelle bis</code>
 * unterschiedlich sein. Bahnhofsteile (Bft) müssen zusätzlich unterschiedlichen Mutterbahnhöfen
 * angehören. Ist bei Bft der Mutterbahnhof gleich, ist zu prüfen, ob zwischen diesen Bft
 * Betriebsstellen eines anderen Mutterbahnhofs vorhanden sind.
 * 
 * <ol>
 * <li>prüfen, ob es sich um eine Betriebsweise der freien Strecke handelt. Wenn
 * <code>2Bst != true</code> dann abbrechen</li>
 * <li>Wenn <code>gleissperrung.bstvon_ID == gleissperrung.bstbis_ID</code> dann abbrechen</li>
 * <li>Wenn
 * <code>BstVon ist Bft UND BstBis ist Bft UND BstVon.Mutterbahnhof <> BstBis.Mutterbahnhof<code> dann abbrechen
 * </li>
 * <li>Beide Betriebsstellen gehören zum selben Bahnhof: Streckenverlauf auf Betriebsstellen eines
 * einen Mutterbahnhofs dazwischen prüfen, sonst FEHLER</li>
 * </ol>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 13
 */
public class BetriebsweiseRule implements GleissperrungValidationRule {

	/**
	 * Name des Betriebsstellentyps, der Bahnhofsteile kennzeichnet (ID=25)
	 */
	public static final String BETRIEBSSTELLENTYP_BFT = "Bft";

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		EasyServiceFactory serviceFactory = EasyServiceFactory.getInstance();

		Betriebsweise betriebsweise = serviceFactory.createBetriebsweiseService().findById(
		    form.getBetriebsweiseId());

		if (betriebsweise == null)
			throw new ValidationAbortedException("Es wurde eine ungültige Betriebsweise angegeben.");

		// STEP 1: Prüfung nur erforderlich, wenn 2Bst = true
		if (betriebsweise.isZweiBst() == false)
			return;

		// STEP 2: zwischen identischen Betriebsstellen existiert keine freie Strecke
		if (form.getBetriebsstelleVonId().equals(form.getBetriebsstelleBisId())) {
			collector.handleResult(new ValidationResult("betriebsweiseId", new ActionMessage(
			    "validation.rule.betriebsweiserule.betriebsstellenidentisch")));
			return;
		}

		// Betriebsstellen laden
		BetriebsstelleService bstService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();
		Betriebsstelle bstVon = bstService.findById(form.getBetriebsstelleVonId());
		Betriebsstelle bstBis = bstService.findById(form.getBetriebsstelleBisId());

		Date gleissperrungVon = FrontendHelper.castStringToDate(form.getBeginn());
		Date gleissperrungBis = FrontendHelper.castStringToDate(form.getEnde());

		Betriebsstellentyp typBstVon = bstVon.getBetriebsstellentyp(gleissperrungVon,
		    gleissperrungBis);
		Betriebsstellentyp typBstBis = bstBis.getBetriebsstellentyp(gleissperrungVon,
		    gleissperrungBis);

		if (typBstVon == null)
			throw new ValidationAbortedException(
			    "Betriebsstelle (von) hat keinen gültigen Betriebsstellentyp.");

		if (typBstBis == null)
			throw new ValidationAbortedException(
			    "Betriebsstelle (bis) hat keinen gültigen Betriebsstellentyp.");

		// STEP 3.1
		if (!typBstVon.getName().equalsIgnoreCase(BETRIEBSSTELLENTYP_BFT)
		    || !typBstBis.getName().equalsIgnoreCase(BETRIEBSSTELLENTYP_BFT)) {
			// mind. eine Betriebsstelle ist kein Bahnhofsteil,
			// gültige Eingaben, keine Prüfung erforderlich
			return;
		}

		// STEP 3.2
		if (!bstVon.getMutterBst().equals(bstBis.getMutterBst())) {
			// die Bft gehören zu verschiedenen Bahnhöfen
			// gültige Eingaben, kein Fehler
			return;
		}

		// STEP 4: prüfen, ob auf der Strecke zwischen beiden Betriebsstellen eine Betriebstelle
		// eines
		// anderen Mutterbahnhofs liegt
		try {
			Integer vzgStreckeNummer = VzgStrecke.getId(form.getStrecke());

			if (vzgStreckeNummer == null)
				throw new ValidationAbortedException(String.format(
				    "Streckennummer konnte nicht erkannt werden: %s", form.getStrecke()));

			float kmVon = FrontendHelper.castStringToFloat(form.getKmVon());
			float kmBis = FrontendHelper.castStringToFloat(form.getKmBis());
			if (bstService.hasMoreThanOne_MutterBst(vzgStreckeNummer, kmVon, kmBis)) {
				collector.handleResult(new ValidationResult("betriebsweiseId", new ActionMessage(
				    "validation.rule.betriebsweiserule.betriebsstellenidentisch")));
			}
		} catch (PersistenceException ex) {
			throw new ValidationAbortedException(ex);
		}
	}
}
