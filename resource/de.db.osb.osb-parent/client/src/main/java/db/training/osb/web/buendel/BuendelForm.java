package db.training.osb.web.buendel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

@SuppressWarnings("serial")
public class BuendelForm extends ValidatorForm {

	private Integer buendelId;

	private String buendelName;

	private Integer regionalbereichId;

	private String hauptStrecke;

	private Integer ankermassnahmeArtId;

	private Integer startBahnhofId;

	private Integer endeBahnhofId;

	private String durchfuehrungsZeitraumStartGeplant;

	private String durchfuehrungsZeitraumEndeGeplant;

	private String durchfuehrungsZeitraumStartKoordiniert;

	private String durchfuehrungsZeitraumEndeKoordiniert;

	private String eiuVks;

	private String sperrzeitbedarfBuendel;

	private String baukostenVorBuendelung;

	private String baukostenNachBuendelung;

	private String sperrzeitErsparnis;

	private String baukostenErsparnis;

	private List<Gleissperrung> gleissperrungList;

	private String gleissperrungIds;

	private Boolean delete;

	private Boolean fixiert;

	private Integer fahrplanjahr;

	public BuendelForm() {
		reset();
	}

	public void reset() {
		buendelId = null;
		buendelName = null;
		regionalbereichId = null;
		hauptStrecke = null;
		ankermassnahmeArtId = null;
		startBahnhofId = null;
		endeBahnhofId = null;
		durchfuehrungsZeitraumStartGeplant = null;
		durchfuehrungsZeitraumEndeGeplant = null;
		durchfuehrungsZeitraumStartKoordiniert = null;
		durchfuehrungsZeitraumEndeKoordiniert = null;
		eiuVks = null;
		sperrzeitbedarfBuendel = null;
		baukostenVorBuendelung = null;
		baukostenNachBuendelung = null;
		sperrzeitErsparnis = null;
		baukostenErsparnis = null;
		gleissperrungList = null;
		gleissperrungIds = null;
		fahrplanjahr = null;

		delete = false;
		fixiert = false;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		delete = false;
		fixiert = false;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(mapping, request);

		MessageResources msgRes = MessageResources.getMessageResources("MessageResources");
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();

		VzgStrecke vzgStrecke = null;
		Integer fplj = getFahrplanjahr();

		// VZG-Strecke pruefen
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(getHauptStrecke());
		if (vzgStreckeNummer != null) {
			// Fahrplanjahr zur Sicherheit noch einmal pruefen
			if (fplj != null) {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    fplj, null, true, new Preload[] { new Preload(VzgStrecke.class,
				        "betriebsstellen") }));
				// VZG-Strecke nicht gefunden
				if (vzgStrecke == null) {
					actionErrors.add("hauptStrecke", new ActionMessage("error.invalid", msgRes
					    .getMessage("buendel.hauptStrecke")));
				}
			}
			// Fahrplanjahr nicht gefuellt
			else {
				actionErrors.add("fahrplanjahr", new ActionMessage("error.required", msgRes
				    .getMessage("buendel.fahrplanjahr")));
			}
		}
		// VZG-Strecke nicht gefuellt
		else {
			actionErrors.add("hauptStrecke", new ActionMessage("error.required", msgRes
			    .getMessage("buendel.hauptStrecke")));
		}

		// Betriebsstellen pruefen, ob auf Strecke
		if (vzgStrecke != null) {
			List<Integer> bstIds = new ArrayList<Integer>();
			for (BetriebsstelleVzgStreckeLink bvsl : vzgStrecke.getBetriebsstellen()) {
				bstIds.add(bvsl.getBetriebsstelle().getId());
			}
			// Betriebsstelle VON
			if (getStartBahnhofId() != null && getStartBahnhofId() != 0
			    && !bstIds.contains(getStartBahnhofId())) {
				actionErrors.add("startBahnhofId", new ActionMessage("error.invalid", msgRes
				    .getMessage("buendel.startBahnhof")));
			}
			// Betriebsstelle BIS
			if (getEndeBahnhofId() != null && getEndeBahnhofId() != 0
			    && !bstIds.contains(getEndeBahnhofId())) {
				actionErrors.add("endeBahnhofId", new ActionMessage("error.invalid", msgRes
				    .getMessage("buendel.endeBahnhof")));
			}
		}

		// Start- vor Enddatum
		// Datumswerte pruefen
		Date startKo = FrontendHelper.castStringToDate(getDurchfuehrungsZeitraumStartKoordiniert());
		Date endeKo = FrontendHelper.castStringToDate(getDurchfuehrungsZeitraumEndeKoordiniert());

		if (startKo != null && endeKo != null && endeKo.before(startKo)) {
			actionErrors.add("durchfuehrungszeitraumStartKoordiniert", new ActionMessage(
			    "error.date.sequence", msgRes
			        .getMessage("buendel.durchfuehrungszeitraumStartKoordiniert"), msgRes
			        .getMessage("buendel.durchfuehrungszeitraumEndeKoordiniert")));
		}

		return actionErrors;
	}

	public Integer getBuendelId() {
		return buendelId;
	}

	public void setBuendelId(Integer buendelId) {
		this.buendelId = buendelId;
	}

	public String getBuendelName() {
		return buendelName;
	}

	public void setBuendelName(String buendelName) {
		this.buendelName = buendelName;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public String getHauptStrecke() {
		return hauptStrecke;
	}

	public void setHauptStrecke(String hauptStrecke) {
		this.hauptStrecke = hauptStrecke;
	}

	public Integer getAnkermassnahmeArtId() {
		return ankermassnahmeArtId;
	}

	public void setAnkermassnahmeArtId(Integer ankermassnahmeArtId) {
		this.ankermassnahmeArtId = ankermassnahmeArtId;
	}

	public Integer getStartBahnhofId() {
		return startBahnhofId;
	}

	public void setStartBahnhofId(Integer startBahnhofId) {
		this.startBahnhofId = startBahnhofId;
	}

	public Integer getEndeBahnhofId() {
		return endeBahnhofId;
	}

	public void setEndeBahnhofId(Integer endeBahnhofId) {
		this.endeBahnhofId = endeBahnhofId;
	}

	public String getDurchfuehrungsZeitraumStartGeplant() {
		return durchfuehrungsZeitraumStartGeplant;
	}

	public void setDurchfuehrungsZeitraumStartGeplant(String durchfuehrungsZeitraumStartGeplant) {
		this.durchfuehrungsZeitraumStartGeplant = durchfuehrungsZeitraumStartGeplant;
	}

	public String getDurchfuehrungsZeitraumEndeGeplant() {
		return durchfuehrungsZeitraumEndeGeplant;
	}

	public void setDurchfuehrungsZeitraumEndeGeplant(String durchfuehrungsZeitraumEndeGeplant) {
		this.durchfuehrungsZeitraumEndeGeplant = durchfuehrungsZeitraumEndeGeplant;
	}

	public String getDurchfuehrungsZeitraumStartKoordiniert() {
		return durchfuehrungsZeitraumStartKoordiniert;
	}

	public void setDurchfuehrungsZeitraumStartKoordiniert(
	    String durchfuehrungsZeitraumStartKoordiniert) {
		this.durchfuehrungsZeitraumStartKoordiniert = durchfuehrungsZeitraumStartKoordiniert;
	}

	public String getDurchfuehrungsZeitraumEndeKoordiniert() {
		return durchfuehrungsZeitraumEndeKoordiniert;
	}

	public void setDurchfuehrungsZeitraumEndeKoordiniert(
	    String durchfuehrungsZeitraumEndeKoordiniert) {
		this.durchfuehrungsZeitraumEndeKoordiniert = durchfuehrungsZeitraumEndeKoordiniert;
	}

	public String getEiuVks() {
		return eiuVks;
	}

	public void setEiuVks(String eiuVks) {
		this.eiuVks = eiuVks;
	}

	public String getBaukostenVorBuendelung() {
		return baukostenVorBuendelung;
	}

	public void setBaukostenVorBuendelung(String baukostenVorBuendelung) {
		this.baukostenVorBuendelung = baukostenVorBuendelung;
	}

	public String getBaukostenNachBuendelung() {
		return baukostenNachBuendelung;
	}

	public void setBaukostenNachBuendelung(String baukostenNachBuendelung) {
		this.baukostenNachBuendelung = baukostenNachBuendelung;
	}

	public String getSperrzeitbedarfBuendel() {
		return sperrzeitbedarfBuendel;
	}

	public void setSperrzeitbedarfBuendel(String sperrzeitbedarfBuendel) {
		this.sperrzeitbedarfBuendel = sperrzeitbedarfBuendel;
	}

	public String getSperrzeitErsparnis() {
		return sperrzeitErsparnis;
	}

	public void setSperrzeitErsparnis(String sperrzeitErsparnis) {
		this.sperrzeitErsparnis = sperrzeitErsparnis;
	}

	public String getBaukostenErsparnis() {
		return baukostenErsparnis;
	}

	public void setBaukostenErsparnis(String baukostenErsparnis) {
		this.baukostenErsparnis = baukostenErsparnis;
	}

	public List<Gleissperrung> getGleissperrungList() {
		return gleissperrungList;
	}

	public void setGleissperrungList(List<Gleissperrung> gleissperrungList) {
		this.gleissperrungList = gleissperrungList;
	}

	public String getGleissperrungIds() {
		return gleissperrungIds;
	}

	public void setGleissperrungIds(String gleissperrungIds) {
		this.gleissperrungIds = gleissperrungIds;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getFixiert() {
		return fixiert;
	}

	public void setFixiert(Boolean fixiert) {
		this.fixiert = fixiert;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

}
