package db.training.osb.web.sperrpausenbedarf;

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
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

@SuppressWarnings("serial")
public class SperrpausenbedarfForm extends ValidatorForm {

	private Integer sperrpausenbedarfId;

	private Integer auftraggeberId;

	private Integer regionalbereichId;

	private Integer finanztypId;

	private Integer arbeitenId;

	private String arbeitenKommentar;

	private String hauptStrecke;

	private String richtungsKennzahl;

	private String kmVon;

	private String kmBis;

	private Integer betriebsstelleVonId;

	private Integer betriebsstelleBisId;

	private Integer betriebsstelleVonKoordiniertId;

	private Integer betriebsstelleBisKoordiniertId;

	private String bauterminStart;

	private String bauterminEnde;

	private String bauterminStartKoordiniert;

	private String bauterminEndeKoordiniert;

	private Integer paketId;

	private String technischerPlatz;

	private String gewerk;

	private String untergewerk;

	private String bauverfahren;

	private String pspElement;

	private String weichenbauform;

	private String weichenGleisnummerBfGleisen;

	private String kommentar;

	private String kommentarKoordination;

	private Boolean durchgehend;

	private Boolean schichtweise;

	private boolean wtsMo;

	private boolean wtsDi;

	private boolean wtsMi;

	private boolean wtsDo;

	private boolean wtsFr;

	private boolean wtsSa;

	private boolean wtsSo;

	private Boolean bahnsteige;

	private Boolean einbauPss;

	private Boolean kabelkanal;

	private Boolean oberleitungsAnpassung;

	private Boolean lst;

	private Boolean tiefenentwaesserungNoetig;

	private String tiefentwaesserungLage;

	private String geplanteNennleistung;

	private String umbaulaenge;

	private String notwendigeLaengePss;

	private Boolean fixiert;

	private Integer ankermassnahmeArtId;

	private Integer fahrplanjahr;

	public void reset() {
		sperrpausenbedarfId = null;
		auftraggeberId = null;
		regionalbereichId = null;
		finanztypId = null;
		arbeitenId = null;
		arbeitenKommentar = null;
		hauptStrecke = null;
		richtungsKennzahl = null;
		kmVon = null;
		kmBis = null;
		betriebsstelleVonId = null;
		betriebsstelleBisId = null;
		betriebsstelleVonKoordiniertId = null;
		betriebsstelleBisKoordiniertId = null;
		bauterminStart = null;
		bauterminEnde = null;
		bauterminStartKoordiniert = null;
		bauterminEndeKoordiniert = null;
		paketId = null;
		technischerPlatz = null;
		gewerk = null;
		untergewerk = null;
		bauverfahren = null;
		pspElement = null;
		weichenbauform = null;
		weichenGleisnummerBfGleisen = null;
		kommentar = null;
		kommentarKoordination = null;
		durchgehend = false;
		schichtweise = false;
		bahnsteige = false;
		einbauPss = false;
		kabelkanal = false;
		oberleitungsAnpassung = false;
		lst = false;
		tiefenentwaesserungNoetig = false;
		tiefentwaesserungLage = null;
		geplanteNennleistung = null;
		umbaulaenge = null;
		notwendigeLaengePss = null;
		fixiert = false;
		ankermassnahmeArtId = null;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
		fahrplanjahr = null;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		durchgehend = false;
		schichtweise = false;
		bahnsteige = false;
		einbauPss = false;
		kabelkanal = false;
		oberleitungsAnpassung = false;
		lst = false;
		tiefenentwaesserungNoetig = false;
		fixiert = false;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
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

		// Betriebsstellen pruefen, ob auf Strecke
		if (vzgStrecke != null) {
			List<Integer> bstIds = new ArrayList<Integer>();
			for (BetriebsstelleVzgStreckeLink bvsl : vzgStrecke.getBetriebsstellen()) {
				bstIds.add(bvsl.getBetriebsstelle().getId());
			}
			// Betriebsstelle VON
			if (getBetriebsstelleVonId() != null && getBetriebsstelleVonId() != 0
			    && !bstIds.contains(getBetriebsstelleVonId())) {
				actionErrors.add("betriebsstelleVonId", new ActionMessage("error.invalid", msgRes
				    .getMessage("sperrpausenbedarf.betriebsstelleVon")));
			}
			// Betriebsstelle BIS
			if (getBetriebsstelleBisId() != null && getBetriebsstelleBisId() != 0
			    && !bstIds.contains(getBetriebsstelleBisId())) {
				actionErrors.add("betriebsstelleBisId", new ActionMessage("error.invalid", msgRes
				    .getMessage("sperrpausenbedarf.betriebsstelleBis")));
			}
			// Betriebsstelle VON Koordiniert
			if (getBetriebsstelleVonKoordiniertId() != null
			    && getBetriebsstelleVonKoordiniertId() != 0
			    && !bstIds.contains(getBetriebsstelleVonKoordiniertId())) {
				actionErrors.add("betriebsstelleVonKoordiniertId", new ActionMessage(
				    "error.invalid", msgRes
				        .getMessage("sperrpausenbedarf.betriebsstelleVonKoordiniert")));
			}
			// Betriebsstelle BIS Koordiniert
			if (getBetriebsstelleBisKoordiniertId() != null
			    && getBetriebsstelleBisKoordiniertId() != 0
			    && !bstIds.contains(getBetriebsstelleBisKoordiniertId())) {
				actionErrors.add("betriebsstelleBisKoordiniertId", new ActionMessage(
				    "error.invalid", msgRes
				        .getMessage("sperrpausenbedarf.betriebsstelleBisKoordiniert")));
			}

			// Datumswerte pruefen
			Date start = FrontendHelper.castStringToDate(getBauterminStart());
			Date ende = FrontendHelper.castStringToDate(getBauterminEnde());
			Date startKo = FrontendHelper.castStringToDate(getBauterminStartKoordiniert());
			Date endeKo = FrontendHelper.castStringToDate(getBauterminEndeKoordiniert());

			if (start != null && ende != null && ende.before(start)) {
				actionErrors.add("bauterminStart", new ActionMessage("error.date.sequence", msgRes
				    .getMessage("sperrpausenbedarf.bauterminStart"), msgRes
				    .getMessage("sperrpausenbedarf.bauterminEnde")));
			}
			if (startKo != null && endeKo != null && endeKo.before(startKo)) {
				actionErrors.add("bauterminStartKoordiniert", new ActionMessage(
				    "error.date.sequence", msgRes
				        .getMessage("sperrpausenbedarf.bauterminStartKoordiniert"), msgRes
				        .getMessage("sperrpausenbedarf.bauterminEndeKoordiniert")));
			}
		}

		// notwendigeLaengePss
		if (getEinbauPss() == true
		    && FrontendHelper.castStringToInteger(getNotwendigeLaengePss()) == null) {
			actionErrors.add("notwendigeLaengePss", new ActionMessage("error.required", msgRes
			    .getMessage("sperrpausenbedarf.notwendigeLaengePss")));
		}

		return actionErrors;
	}

	public Integer getSperrpausenbedarfId() {
		return sperrpausenbedarfId;
	}

	public void setSperrpausenbedarfId(Integer sperrpausenbedarfId) {
		this.sperrpausenbedarfId = sperrpausenbedarfId;
	}

	public Integer getAuftraggeberId() {
		return auftraggeberId;
	}

	public void setAuftraggeberId(Integer auftraggeberId) {
		this.auftraggeberId = auftraggeberId;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public Integer getFinanztypId() {
		return finanztypId;
	}

	public void setFinanztypId(Integer finanztypId) {
		this.finanztypId = finanztypId;
	}

	public Integer getArbeitenId() {
		return arbeitenId;
	}

	public void setArbeitenId(Integer arbeitenId) {
		this.arbeitenId = arbeitenId;
	}

	public String getArbeitenKommentar() {
		return arbeitenKommentar;
	}

	public void setArbeitenKommentar(String arbeitenKommentar) {
		this.arbeitenKommentar = arbeitenKommentar;
	}

	public String getHauptStrecke() {
		return hauptStrecke;
	}

	public void setHauptStrecke(String hauptStrecke) {
		this.hauptStrecke = hauptStrecke;
	}

	public String getRichtungsKennzahl() {
		return richtungsKennzahl;
	}

	public void setRichtungsKennzahl(String richtungsKennzahl) {
		this.richtungsKennzahl = richtungsKennzahl;
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

	public Integer getBetriebsstelleVonId() {
		return betriebsstelleVonId;
	}

	public void setBetriebsstelleVonId(Integer betriebsstelleVonId) {
		this.betriebsstelleVonId = betriebsstelleVonId;
	}

	public Integer getBetriebsstelleBisId() {
		return betriebsstelleBisId;
	}

	public void setBetriebsstelleBisId(Integer betriebsstelleBisId) {
		this.betriebsstelleBisId = betriebsstelleBisId;
	}

	public Integer getBetriebsstelleVonKoordiniertId() {
		return betriebsstelleVonKoordiniertId;
	}

	public void setBetriebsstelleVonKoordiniertId(Integer betriebsstelleVonKoordiniertId) {
		this.betriebsstelleVonKoordiniertId = betriebsstelleVonKoordiniertId;
	}

	public Integer getBetriebsstelleBisKoordiniertId() {
		return betriebsstelleBisKoordiniertId;
	}

	public void setBetriebsstelleBisKoordiniertId(Integer betriebsstelleBisKoordiniertId) {
		this.betriebsstelleBisKoordiniertId = betriebsstelleBisKoordiniertId;
	}

	public String getBauterminStart() {
		return bauterminStart;
	}

	public void setBauterminStart(String bauterminStart) {
		this.bauterminStart = bauterminStart;
	}

	public String getBauterminEnde() {
		return bauterminEnde;
	}

	public void setBauterminEnde(String bauterminEnde) {
		this.bauterminEnde = bauterminEnde;
	}

	public String getBauterminStartKoordiniert() {
		return bauterminStartKoordiniert;
	}

	public void setBauterminStartKoordiniert(String bauterminStartKoordiniert) {
		this.bauterminStartKoordiniert = bauterminStartKoordiniert;
	}

	public String getBauterminEndeKoordiniert() {
		return bauterminEndeKoordiniert;
	}

	public void setBauterminEndeKoordiniert(String bauterminEndeKoordiniert) {
		this.bauterminEndeKoordiniert = bauterminEndeKoordiniert;
	}

	public Integer getPaketId() {
		return paketId;
	}

	public void setPaketId(Integer paketId) {
		this.paketId = paketId;
	}

	public String getTechnischerPlatz() {
		return technischerPlatz;
	}

	public void setTechnischerPlatz(String technischerPlatz) {
		this.technischerPlatz = technischerPlatz;
	}

	public String getGewerk() {
		return gewerk;
	}

	public void setGewerk(String gewerk) {
		this.gewerk = gewerk;
	}

	public String getUntergewerk() {
		return untergewerk;
	}

	public void setUntergewerk(String untergewerk) {
		this.untergewerk = untergewerk;
	}

	public String getBauverfahren() {
		return bauverfahren;
	}

	public void setBauverfahren(String bauverfahren) {
		this.bauverfahren = bauverfahren;
	}

	public String getPspElement() {
		return pspElement;
	}

	public void setPspElement(String pspElement) {
		this.pspElement = pspElement;
	}

	public String getWeichenbauform() {
		return weichenbauform;
	}

	public void setWeichenbauform(String weichenbauform) {
		this.weichenbauform = weichenbauform;
	}

	public String getWeichenGleisnummerBfGleisen() {
		return weichenGleisnummerBfGleisen;
	}

	public void setWeichenGleisnummerBfGleisen(String weichenGleisnummerBfGleisen) {
		this.weichenGleisnummerBfGleisen = weichenGleisnummerBfGleisen;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getKommentarKoordination() {
		return kommentarKoordination;
	}

	public void setKommentarKoordination(String kommentarKoordination) {
		this.kommentarKoordination = kommentarKoordination;
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

	public Boolean getBahnsteige() {
		return bahnsteige;
	}

	public void setBahnsteige(Boolean bahnsteige) {
		this.bahnsteige = bahnsteige;
	}

	public Boolean getEinbauPss() {
		return einbauPss;
	}

	public void setEinbauPss(Boolean einbauPss) {
		this.einbauPss = einbauPss;
	}

	public Boolean getKabelkanal() {
		return kabelkanal;
	}

	public void setKabelkanal(Boolean kabelkanal) {
		this.kabelkanal = kabelkanal;
	}

	public Boolean getOberleitungsAnpassung() {
		return oberleitungsAnpassung;
	}

	public void setOberleitungsAnpassung(Boolean oberleitungsAnpassung) {
		this.oberleitungsAnpassung = oberleitungsAnpassung;
	}

	public Boolean getLst() {
		return lst;
	}

	public void setLst(Boolean lst) {
		this.lst = lst;
	}

	public Boolean getTiefenentwaesserungNoetig() {
		return tiefenentwaesserungNoetig;
	}

	public void setTiefenentwaesserungNoetig(Boolean tiefenentwaesserungNoetig) {
		this.tiefenentwaesserungNoetig = tiefenentwaesserungNoetig;
	}

	public String getTiefentwaesserungLage() {
		return tiefentwaesserungLage;
	}

	public void setTiefentwaesserungLage(String tiefentwaesserungLage) {
		this.tiefentwaesserungLage = tiefentwaesserungLage;
	}

	public String getGeplanteNennleistung() {
		return geplanteNennleistung;
	}

	public void setGeplanteNennleistung(String geplanteNennleistung) {
		this.geplanteNennleistung = geplanteNennleistung;
	}

	public String getUmbaulaenge() {
		return umbaulaenge;
	}

	public void setUmbaulaenge(String umbaulaenge) {
		this.umbaulaenge = umbaulaenge;
	}

	public String getNotwendigeLaengePss() {
		return notwendigeLaengePss;
	}

	public void setNotwendigeLaengePss(String notwendigeLaengePss) {
		this.notwendigeLaengePss = notwendigeLaengePss;
	}

	public Boolean getFixiert() {
		return fixiert;
	}

	public void setFixiert(Boolean fixiert) {
		this.fixiert = fixiert;
	}

	public Integer getAnkermassnahmeArtId() {
		return ankermassnahmeArtId;
	}

	public void setAnkermassnahmeArtId(Integer ankermassnahmeArtId) {
		this.ankermassnahmeArtId = ankermassnahmeArtId;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

}
