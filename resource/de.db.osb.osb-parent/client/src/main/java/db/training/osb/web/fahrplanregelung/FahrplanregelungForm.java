package db.training.osb.web.fahrplanregelung;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseValidatorForm;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.service.BetriebsstelleService;

@SuppressWarnings("serial")
public class FahrplanregelungForm extends BaseValidatorForm {

	private Integer fahrplanregelungId;

	private Integer regionalbereichId;

	private String name;

	private Integer betriebsweiseId;

	private String aufnahmeNfpVorschlag;

	private String aufnahmeNfp;

	private boolean behandlungKS;

	private boolean nachtsperrpause;

	private boolean verkehrstag_mo;

	private boolean verkehrstag_di;

	private boolean verkehrstag_mi;

	private boolean verkehrstag_do;

	private boolean verkehrstag_fr;

	private boolean verkehrstag_sa;

	private boolean verkehrstag_so;

	private String planStart;

	private String planEnde;

	private String start;

	private String ende;

	private String betriebsstelleVon;

	private String betriebsstelleBis;

	private Integer buendelId;

	private Integer sevSpfv;

	private Integer sevSpnv;

	private boolean relevanzBzu;

	private Boolean delete;

	private boolean fixiert;

	private Integer fahrplanjahr;

	private boolean copy;

	private boolean importiert;

	public FahrplanregelungForm() {
		reset();
	}

	public void reset() {
		fahrplanregelungId = null;
		regionalbereichId = null;
		name = null;
		betriebsweiseId = null;
		aufnahmeNfpVorschlag = null;
		aufnahmeNfp = null;
		planStart = null;
		planEnde = null;
		start = null;
		ende = null;
		betriebsstelleVon = null;
		betriebsstelleBis = null;
		buendelId = null;
		sevSpfv = null;
		sevSpnv = null;

		verkehrstag_mo = true;
		verkehrstag_di = true;
		verkehrstag_mi = true;
		verkehrstag_do = true;
		verkehrstag_fr = true;
		verkehrstag_sa = true;
		verkehrstag_so = true;

		behandlungKS = false;
		nachtsperrpause = false;
		relevanzBzu = false;
		fixiert = false;

		delete = null;
		copy = false;
		importiert = false;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// super.reset(arg0, arg1);
		fixiert = false;
		behandlungKS = false;
		nachtsperrpause = false;
		relevanzBzu = false;

		verkehrstag_mo = false;
		verkehrstag_di = false;
		verkehrstag_mi = false;
		verkehrstag_do = false;
		verkehrstag_fr = false;
		verkehrstag_sa = false;
		verkehrstag_so = false;

		copy = false;
		importiert = false;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(mapping, request);

		BetriebsstelleService bsService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();
		MessageResources msgRes = MessageResources.getMessageResources("MessageResources");

		// Fahrplanjahr pruefen, normalerweise nicht notwendig, da nicht aenderbar
		if (getFahrplanjahr() == null || getFahrplanjahr() == 0) {
			actionErrors.add("fahrplanjahr", new ActionMessage("error.required", msgRes
			    .getMessage("fahrplanregelung.fahrplanjahr")));
		} else {
			// BetriebsstelleVon pruefen
			if (getBetriebsstelleVon() != null && getBetriebsstelleVon().length() > 0
			    && bsService.castCaptionToKuerzel(getBetriebsstelleVon()) != null) {
				Betriebsstelle vonOrt = bsService.findByKuerzelAndFahrplanjahr(bsService
				    .castCaptionToKuerzel(getBetriebsstelleVon()), getFahrplanjahr());
				if (vonOrt == null) {
					actionErrors.add("betriebsstelleVon", new ActionMessage("error.invalid", msgRes
					    .getMessage("fahrplanregelung.betriebsstelleVon")));
				}
			}
			// BetriebsstelleBis pruefen
			if (getBetriebsstelleBis() != null && getBetriebsstelleBis().length() > 0
			    && bsService.castCaptionToKuerzel(getBetriebsstelleBis()) != null) {
				Betriebsstelle bisOrt = bsService.findByKuerzelAndFahrplanjahr(bsService
				    .castCaptionToKuerzel(getBetriebsstelleBis()), getFahrplanjahr());
				if (bisOrt == null) {
					actionErrors.add("betriebsstelleBis", new ActionMessage("error.invalid", msgRes
					    .getMessage("fahrplanregelung.betriebsstelleBis")));
				}
			}
		}

		// Datumswerte pruefen
		Date start = FrontendHelper.castStringToDate(getPlanStart());
		Date ende = FrontendHelper.castStringToDate(getPlanEnde());
		Date startKo = FrontendHelper.castStringToDate(getStart());
		Date endeKo = FrontendHelper.castStringToDate(getEnde());

		if (start != null && ende != null && ende.before(start)) {
			actionErrors.add("planStart", new ActionMessage("error.date.sequence", msgRes
			    .getMessage("fahrplanregelung.planStart"), msgRes
			    .getMessage("fahrplanregelung.planEnde")));
		}
		if (startKo != null && endeKo != null && endeKo.before(startKo)) {
			actionErrors.add("start", new ActionMessage("error.date.sequence", msgRes
			    .getMessage("fahrplanregelung.start"), msgRes.getMessage("fahrplanregelung.ende")));
		}

		return actionErrors;
	}

	public Integer getFahrplanregelungId() {
		return fahrplanregelungId;
	}

	public void setFahrplanregelungId(Integer fahrplanregelungId) {
		this.fahrplanregelungId = fahrplanregelungId;
	}

	public String getAufnahmeNfpVorschlag() {
		return aufnahmeNfpVorschlag;
	}

	public void setAufnahmeNfpVorschlag(String aufnahmeNfpVorschlag) {
		this.aufnahmeNfpVorschlag = aufnahmeNfpVorschlag;
	}

	public String getAufnahmeNfp() {
		return aufnahmeNfp;
	}

	public void setAufnahmeNfp(String aufnahmeNfp) {
		this.aufnahmeNfp = aufnahmeNfp;
	}

	public void setBehandlungKS(boolean behandlungKS) {
		this.behandlungKS = behandlungKS;
	}

	public void setNachtsperrpause(boolean nachtsperrpause) {
		this.nachtsperrpause = nachtsperrpause;
	}

	public Boolean getBehandlungKS() {
		return behandlungKS;
	}

	public void setBehandlungKS(Boolean behandlungKS) {
		this.behandlungKS = behandlungKS;
	}

	public String getPlanStart() {
		return planStart;
	}

	public void setPlanStart(String planStart) {
		this.planStart = planStart;
	}

	public String getPlanEnde() {
		return planEnde;
	}

	public void setPlanEnde(String planEnde) {
		this.planEnde = planEnde;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnde() {
		return ende;
	}

	public void setEnde(String ende) {
		this.ende = ende;
	}

	public String getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(String betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public String getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(String betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
	}

	public Integer getBuendelId() {
		return buendelId;
	}

	public void setBuendelId(Integer buendelId) {
		this.buendelId = buendelId;
	}

	public Integer getBetriebsweiseId() {
		return betriebsweiseId;
	}

	public void setBetriebsweiseId(Integer betriebsweiseId) {
		this.betriebsweiseId = betriebsweiseId;
	}

	public Boolean getNachtsperrpause() {
		return nachtsperrpause;
	}

	public void setNachtsperrpause(Boolean nachtsperrpause) {
		this.nachtsperrpause = nachtsperrpause;
	}

	public boolean isVerkehrstag_mo() {
		return verkehrstag_mo;
	}

	public void setVerkehrstag_mo(boolean verkehrstagMo) {
		verkehrstag_mo = verkehrstagMo;
	}

	public boolean isVerkehrstag_di() {
		return verkehrstag_di;
	}

	public void setVerkehrstag_di(boolean verkehrstagDi) {
		verkehrstag_di = verkehrstagDi;
	}

	public boolean isVerkehrstag_mi() {
		return verkehrstag_mi;
	}

	public void setVerkehrstag_mi(boolean verkehrstagMi) {
		verkehrstag_mi = verkehrstagMi;
	}

	public boolean isVerkehrstag_do() {
		return verkehrstag_do;
	}

	public void setVerkehrstag_do(boolean verkehrstagDo) {
		verkehrstag_do = verkehrstagDo;
	}

	public boolean isVerkehrstag_fr() {
		return verkehrstag_fr;
	}

	public void setVerkehrstag_fr(boolean verkehrstagFr) {
		verkehrstag_fr = verkehrstagFr;
	}

	public boolean isVerkehrstag_sa() {
		return verkehrstag_sa;
	}

	public void setVerkehrstag_sa(boolean verkehrstagSa) {
		verkehrstag_sa = verkehrstagSa;
	}

	public boolean isVerkehrstag_so() {
		return verkehrstag_so;
	}

	public void setVerkehrstag_so(boolean verkehrstagSo) {
		verkehrstag_so = verkehrstagSo;
	}

	public Integer getSevSpfv() {
		return sevSpfv;
	}

	public void setSevSpfv(Integer sevSpfv) {
		this.sevSpfv = sevSpfv;
	}

	public Integer getSevSpnv() {
		return sevSpnv;
	}

	public void setSevSpnv(Integer sevSpnv) {
		this.sevSpnv = sevSpnv;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public boolean isRelevanzBzu() {
		return relevanzBzu;
	}

	public void setRelevanzBzu(boolean relevanzBzu) {
		this.relevanzBzu = relevanzBzu;
	}

	public boolean isFixiert() {
		return fixiert;
	}

	public void setFixiert(boolean fixiert) {
		this.fixiert = fixiert;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCopy() {
		return copy;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public boolean isImportiert() {
		return importiert;
	}

	public void setImportiert(boolean importiert) {
		this.importiert = importiert;
	}

}
