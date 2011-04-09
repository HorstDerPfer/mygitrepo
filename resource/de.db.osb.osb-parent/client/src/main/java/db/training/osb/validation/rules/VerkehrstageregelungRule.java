package db.training.osb.validation.rules;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts.action.ActionMessage;

import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.SAPMassnahme;
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
 * <ol start="10">
 * <li>Bei eriner Maßnahme- oder Regelungsdauer <= 48 Stunden darf keine VTS angegeben sein.</li>
 * <li>Beginn und Ender Maßnahme oder Reglung müssen mit der eingegebenen VTS übereinstimmen.</li>
 * </ol>
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.3.7 U-0801007 Regel 10, 11
 */
public class VerkehrstageregelungRule implements GleissperrungValidationRule,
    OberleitungValidationRule, SAPMassnahmeValidationRule, LangsamfahrstelleValidationRule {

	private class Verkehrstageregelung {
		private boolean wtsMo;

		private boolean wtsDi;

		private boolean wtsMi;

		private boolean wtsDo;

		private boolean wtsFr;

		private boolean wtsSa;

		private boolean wtsSo;

		Verkehrstageregelung(boolean wtsMo, boolean wtsDi, boolean wtsMi, boolean wtsDo,
		    boolean wtsFr, boolean wtsSa, boolean wtsSo) {
			this.setWtsMo(wtsMo);
			this.setWtsDi(wtsDi);
			this.setWtsMi(wtsMi);
			this.setWtsDo(wtsDo);
			this.setWtsFr(wtsFr);
			this.setWtsSa(wtsSa);
			this.setWtsSo(wtsSo);
		}

		public void setWtsMo(boolean wtsMo) {
			this.wtsMo = wtsMo;
		}

		public boolean isWtsMo() {
			return wtsMo;
		}

		public void setWtsDi(boolean wtsDi) {
			this.wtsDi = wtsDi;
		}

		public boolean isWtsDi() {
			return wtsDi;
		}

		public void setWtsMi(boolean wtsMi) {
			this.wtsMi = wtsMi;
		}

		public boolean isWtsMi() {
			return wtsMi;
		}

		public void setWtsDo(boolean wtsDo) {
			this.wtsDo = wtsDo;
		}

		public boolean isWtsDo() {
			return wtsDo;
		}

		public void setWtsFr(boolean wtsFr) {
			this.wtsFr = wtsFr;
		}

		public boolean isWtsFr() {
			return wtsFr;
		}

		public void setWtsSa(boolean wtsSa) {
			this.wtsSa = wtsSa;
		}

		public boolean isWtsSa() {
			return wtsSa;
		}

		public void setWtsSo(boolean wtsSo) {
			this.wtsSo = wtsSo;
		}

		public boolean isWtsSo() {
			return wtsSo;
		}

		public boolean isSet(Calendar day) {
			switch (day.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				return isWtsMo();
			case Calendar.TUESDAY:
				return isWtsDi();
			case Calendar.WEDNESDAY:
				return isWtsMi();
			case Calendar.THURSDAY:
				return isWtsDo();
			case Calendar.FRIDAY:
				return isWtsFr();
			case Calendar.SATURDAY:
				return isWtsSa();
			case Calendar.SUNDAY:
				return isWtsSo();
			}
			return false;
		}
	}

	private void validateIntern(Verkehrstageregelung vtr, Date beginnDate, Date endeDate,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Calendar beginn = Calendar.getInstance();
		Calendar ende = Calendar.getInstance();

		beginn.setTime(beginnDate);
		ende.setTime(endeDate);

		// Dauer < 48 Stunden prüfen (2881 Minuten!)
		Calendar minEnde = (Calendar) beginn.clone();
		minEnde.roll(Calendar.MINUTE, 2881);

		if (minEnde.after(ende)) {
			// es darf kein VTS gesetzt sein
			if (vtr.isWtsMo() || vtr.isWtsDi() || vtr.isWtsMi() || vtr.isWtsDo() || vtr.isWtsFr()
			    || vtr.isWtsSa() || vtr.isWtsSo()) {
				collector.handleResult(new ValidationResult("beginn", new ActionMessage(
				    "validation.rule.verkehrstageregelungrule.48stunden")));
			}
		} else {
			// prüfen, ob Beginn und Ende der Maßnahme/Regelung mit der VTS übereinstimmen
			if (!vtr.isSet(beginn) && !vtr.isSet(ende)) {
				collector.handleResult(new ValidationResult("beginn", new ActionMessage(
				    "validation.rule.verkehrstageregelungrule.vtr")));
			}
		}
	}

	@Override
	public void validate(Gleissperrung gleissperrung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Verkehrstageregelung vtr = new Verkehrstageregelung(form.isWtsMo(), form.isWtsDi(), form
		    .isWtsMi(), form.isWtsDo(), form.isWtsFr(), form.isWtsSa(), form.isWtsSo());

		validateIntern(vtr, FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(SAPMassnahme massnahme, MassnahmeForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Verkehrstageregelung vtr = new Verkehrstageregelung(form.isWtsMo(), form.isWtsDi(), form
		    .isWtsMi(), form.isWtsDo(), form.isWtsFr(), form.isWtsSa(), form.isWtsSo());

		validateIntern(vtr, FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(Oberleitung oberleitung, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Verkehrstageregelung vtr = new Verkehrstageregelung(form.isWtsMo(), form.isWtsDi(), form
		    .isWtsMi(), form.isWtsDo(), form.isWtsFr(), form.isWtsSa(), form.isWtsSo());

		validateIntern(vtr, FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

	@Override
	public void validate(Langsamfahrstelle langsamfahrstelle, MassnahmeRegelungForm form,
	    ResultHandler<ValidationResult> collector) throws ValidationAbortedException {

		Verkehrstageregelung vtr = new Verkehrstageregelung(form.isWtsMo(), form.isWtsDi(), form
		    .isWtsMi(), form.isWtsDo(), form.isWtsFr(), form.isWtsSa(), form.isWtsSo());

		validateIntern(vtr, FrontendHelper.castStringToDate(form.getBeginn()), FrontendHelper
		    .castStringToDate(form.getEnde()), collector);
	}

}
