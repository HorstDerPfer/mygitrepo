package db.training.osb.web.gleissperrung;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseValidatorForm;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

public class GleissperrungForm extends BaseValidatorForm {

	private static final long serialVersionUID = -1434241022242372879L;

	private Integer gleissperrungId;

	private Integer lfdNr;

	private Integer massnahmeId;

	private String buendelIds;

	private String baustellenIds;

	private String vzgStrecke;

	private Integer bstVonId;

	private Integer bstBisId;

	private Integer bstVonKoordiniertId;

	private Integer bstBisKoordiniertId;

	private String kmVon;

	private String kmBis;

	private String sigWeicheVon;

	private String sigWeicheBis;

	private String artDerArbeiten;

	private String zeitVon;

	private String zeitBis;

	private String zeitVonKoordiniert;

	private String zeitBisKoordiniert;

	private Boolean durchgehend;

	private Boolean schichtweise;

	private Integer betriebsweiseId;

	private String betriebsweise;

	private String sperrpausenbedarf;

	private String sperrpausenbedarfArt;

	private String auswirkung;

	private Boolean bauLue;

	private Integer richtungsKennzahl;

	private String fahrplanjahr;

	private String kommentar;

	private String bezeichnung;

	private Integer vtrId;

	private Boolean delete;

	private boolean wtsMo;

	private boolean wtsDi;

	private boolean wtsMi;

	private boolean wtsDo;

	private boolean wtsFr;

	private boolean wtsSa;

	private boolean wtsSo;

	public GleissperrungForm() {
		reset();
	}

	public void reset() {
		setGleissperrungId(null);
		setLfdNr(null);
		setMassnahmeId(null);
		setBuendelIds(null);
		setBaustellenIds(null);
		setVzgStrecke(null);
		setBstVonId(null);
		setBstBisId(null);
		setBstVonKoordiniertId(null);
		setBstBisKoordiniertId(null);
		setKmVon(null);
		setKmBis(null);
		setSigWeicheVon(null);
		setSigWeicheBis(null);
		setArtDerArbeiten(null);
		setZeitVon(null);
		setZeitBis(null);
		setZeitVonKoordiniert(null);
		setZeitBisKoordiniert(null);
		setBetriebsweiseId(null);
		setBetriebsweise(null);
		setSperrpausenbedarf(null);
		setSperrpausenbedarfArt(null);
		setBauLue(null);
		setRichtungsKennzahl(null);
		setFahrplanjahr(null);
		setKommentar(null);
		setBezeichnung(null);
		setDelete(null);
		setDurchgehend(false);
		setSchichtweise(false);
		vtrId = null;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		MessageResources msgRes = MessageResources.getMessageResources("MessageResources");
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();

		VzgStrecke vzgStrecke = null;
		Integer fplj = FrontendHelper.castStringToInteger(getFahrplanjahr());

		/* Sperrpausenbedarfe */
		if (FrontendHelper.stringNotNullOrEmpty(getSperrpausenbedarf())) {
			if (FrontendHelper.stringNotNullOrEmpty(getSperrpausenbedarfArt())) {
				if (FrontendHelper.castStringToInteger(getSperrpausenbedarf()) == null) {
					actionErrors.add("sperrpausenbedarf", new ActionMessage("error.invalid", msgRes
					    .getMessage("gleissperrung.sperrbedarf")));
				}
			} else {
				actionErrors.add("sperrpausenbedarfArt", new ActionMessage("error.required", msgRes
				    .getMessage("sperrpausenbedarf.art")));
			}
		}

		// VZG-Strecke pruefen
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(getVzgStrecke());
		if (vzgStreckeNummer != null) {
			// Fahrplanjahr zur Sicherheit noch einmal pruefen
			if (fplj != null) {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    fplj, null, false, new Preload[] { new Preload(VzgStrecke.class,
				        "betriebsstellen") }));
				// VZG-Strecke nicht gefunden
				if (vzgStrecke == null) {
					actionErrors.add("vzgStrecke", new ActionMessage("error.invalid", msgRes
					    .getMessage("regelung.vzgStrecke")));
				}
			}
			// Fahrplanjahr nicht gefuellt
			else {
				actionErrors.add("fahrplanjahr", new ActionMessage("error.required", msgRes
				    .getMessage("regelung.fahrplanjahr")));
			}
		}
		// VZG-Strecke nicht gefuellt
		else {
			actionErrors.add("vzgStrecke", new ActionMessage("error.required", msgRes
			    .getMessage("regelung.vzgStrecke")));
		}

		// Betriebsstellen pruefen, ob auf Strecke
		if (vzgStrecke != null) {
			List<Integer> bstIds = new ArrayList<Integer>();
			for (BetriebsstelleVzgStreckeLink bvsl : vzgStrecke.getBetriebsstellen()) {
				bstIds.add(bvsl.getBetriebsstelle().getId());
			}
			// Betriebsstelle VON
			if (getBstVonId() != null && getBstVonId() != 0 && !bstIds.contains(getBstVonId())) {
				actionErrors.add("bstVonId", new ActionMessage("error.invalid", msgRes
				    .getMessage("regelung.bstVon")));
			}
			// Betriebsstelle BIS
			if (getBstBisId() != null && getBstBisId() != 0 && !bstIds.contains(getBstBisId())) {
				actionErrors.add("bstBisId", new ActionMessage("error.invalid", msgRes
				    .getMessage("regelung.bstBis")));
			}
			// Betriebsstelle VON Koordiniert
			if (getBstVonKoordiniertId() != null && getBstVonKoordiniertId() != 0
			    && !bstIds.contains(getBstVonKoordiniertId())) {
				actionErrors.add("bstVonKoordiniertId", new ActionMessage("error.invalid", msgRes
				    .getMessage("regelung.bstVonKoordiniert")));
			}
			// Betriebsstelle BIS Koordiniert
			if (getBstBisKoordiniertId() != null && getBstBisKoordiniertId() != 0
			    && !bstIds.contains(getBstBisKoordiniertId())) {
				actionErrors.add("bstBisKoordiniertId", new ActionMessage("error.invalid", msgRes
				    .getMessage("regelung.bstBisKoordiniert")));
			}
		} else {
			actionErrors.add("bstVonId", new ActionMessage("error.required", msgRes
			    .getMessage("regelung.bstVon")));
			actionErrors.add("bstBisId", new ActionMessage("error.required", msgRes
			    .getMessage("regelung.bstBis")));
		}

		return actionErrors;
	}

	public Integer getGleissperrungId() {
		return gleissperrungId;
	}

	public void setGleissperrungId(Integer gleissperrungId) {
		this.gleissperrungId = gleissperrungId;
	}

	public Integer getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Integer lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Integer getMassnahmeId() {
		return massnahmeId;
	}

	public void setMassnahmeId(Integer massnahmeId) {
		this.massnahmeId = massnahmeId;
	}

	public String getBuendelIds() {
		return buendelIds;
	}

	public void setBuendelIds(String buendelIds) {
		this.buendelIds = buendelIds;
	}

	public String getBaustellenIds() {
		return baustellenIds;
	}

	public void setBaustellenIds(String baustellenIds) {
		this.baustellenIds = baustellenIds;
	}

	public String getVzgStrecke() {
		return vzgStrecke;
	}

	public void setVzgStrecke(String vzgStrecke) {
		this.vzgStrecke = vzgStrecke;
	}

	public Integer getBstVonId() {
		return bstVonId;
	}

	public void setBstVonId(Integer bstVonId) {
		this.bstVonId = bstVonId;
	}

	public Integer getBstBisId() {
		return bstBisId;
	}

	public void setBstBisId(Integer bstBisId) {
		this.bstBisId = bstBisId;
	}

	public Integer getBstVonKoordiniertId() {
		return bstVonKoordiniertId;
	}

	public void setBstVonKoordiniertId(Integer bstVonKoordiniertId) {
		this.bstVonKoordiniertId = bstVonKoordiniertId;
	}

	public Integer getBstBisKoordiniertId() {
		return bstBisKoordiniertId;
	}

	public void setBstBisKoordiniertId(Integer bstBisKoordiniertId) {
		this.bstBisKoordiniertId = bstBisKoordiniertId;
	}

	public String getKmVon() {
		return kmVon;
	}

	public void setKmVon(String kmVon) {
		this.kmVon = kmVon;
	}

	public String getKmBis() {
		return kmBis;
	}

	public void setKmBis(String kmBis) {
		this.kmBis = kmBis;
	}

	public String getSigWeicheVon() {
		return sigWeicheVon;
	}

	public void setSigWeicheVon(String sigWeicheVon) {
		this.sigWeicheVon = sigWeicheVon;
	}

	public String getSigWeicheBis() {
		return sigWeicheBis;
	}

	public void setSigWeicheBis(String sigWeicheBis) {
		this.sigWeicheBis = sigWeicheBis;
	}

	public String getArtDerArbeiten() {
		return artDerArbeiten;
	}

	public void setArtDerArbeiten(String artDerArbeiten) {
		this.artDerArbeiten = artDerArbeiten;
	}

	public String getZeitVon() {
		return zeitVon;
	}

	public void setZeitVon(String zeitVon) {
		this.zeitVon = zeitVon;
	}

	public String getZeitBis() {
		return zeitBis;
	}

	public void setZeitBis(String zeitBis) {
		this.zeitBis = zeitBis;
	}

	public String getZeitVonKoordiniert() {
		return zeitVonKoordiniert;
	}

	public void setZeitVonKoordiniert(String zeitVonKoordiniert) {
		this.zeitVonKoordiniert = zeitVonKoordiniert;
	}

	public String getZeitBisKoordiniert() {
		return zeitBisKoordiniert;
	}

	public void setZeitBisKoordiniert(String zeitBisKoordiniert) {
		this.zeitBisKoordiniert = zeitBisKoordiniert;
	}

	public Boolean getDurchgehend() {
		return durchgehend;
	}

	public void setDurchgehend(Boolean durchgehend) {
		this.durchgehend = durchgehend;
	}

	public Boolean getSchichtweise() {
		return schichtweise;
	}

	public void setSchichtweise(Boolean schichtweise) {
		this.schichtweise = schichtweise;
	}

	public Integer getBetriebsweiseId() {
		return betriebsweiseId;
	}

	public void setBetriebsweiseId(Integer betriebsweise) {
		this.betriebsweiseId = betriebsweise;
	}

	public String getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(String betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public String getSperrpausenbedarf() {
		return sperrpausenbedarf;
	}

	public void setSperrpausenbedarf(String sperrpausenbedarf) {
		this.sperrpausenbedarf = sperrpausenbedarf;
	}

	public String getSperrpausenbedarfArt() {
		return sperrpausenbedarfArt;
	}

	public void setSperrpausenbedarfArt(String sperrpausenbedarfArt) {
		this.sperrpausenbedarfArt = sperrpausenbedarfArt;
	}

	public String getAuswirkung() {
		return auswirkung;
	}

	public void setAuswirkung(String auswirkung) {
		this.auswirkung = auswirkung;
	}

	public Boolean getBauLue() {
		return bauLue;
	}

	public void setBauLue(Boolean bauLue) {
		this.bauLue = bauLue;
	}

	public Integer getRichtungsKennzahl() {
		return richtungsKennzahl;
	}

	public void setRichtungsKennzahl(Integer richtungsKennzahl) {
		this.richtungsKennzahl = richtungsKennzahl;
	}

	public String getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(String fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Integer getVtrId() {
		return vtrId;
	}

	public void setVtrId(Integer vtrId) {
		this.vtrId = vtrId;
	}

	public boolean isWtsMo() {
		return wtsMo;
	}

	public void setWtsMo(boolean wtsMo) {
		this.wtsMo = wtsMo;
	}

	public boolean isWtsDi() {
		return wtsDi;
	}

	public void setWtsDi(boolean wtsDi) {
		this.wtsDi = wtsDi;
	}

	public boolean isWtsMi() {
		return wtsMi;
	}

	public void setWtsMi(boolean wtsMi) {
		this.wtsMi = wtsMi;
	}

	public boolean isWtsDo() {
		return wtsDo;
	}

	public void setWtsDo(boolean wtsDo) {
		this.wtsDo = wtsDo;
	}

	public boolean isWtsFr() {
		return wtsFr;
	}

	public void setWtsFr(boolean wtsFr) {
		this.wtsFr = wtsFr;
	}

	public boolean isWtsSa() {
		return wtsSa;
	}

	public void setWtsSa(boolean wtsSa) {
		this.wtsSa = wtsSa;
	}

	public boolean isWtsSo() {
		return wtsSo;
	}

	public void setWtsSo(boolean wtsSo) {
		this.wtsSo = wtsSo;
	}

}
