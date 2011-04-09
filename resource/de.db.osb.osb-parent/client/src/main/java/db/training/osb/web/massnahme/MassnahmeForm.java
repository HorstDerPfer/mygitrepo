package db.training.osb.web.massnahme;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseValidatorForm;
import db.training.osb.validation.SAPMassnahmeValidator;

@SuppressWarnings("serial")
public class MassnahmeForm extends BaseValidatorForm {

	private Integer massnahmeId;

	private Integer regionalbereichId;

	private Boolean entwurf;

	private Boolean studie;

	private Integer finanztypId;

	private Boolean geaendert;

	private Boolean gravierendeAenderung;

	private String strecke;

	private Integer betriebsstelleVonId;

	private Integer betriebsstelleBisId;

	private Integer richtungskennzahl;

	private String kmVon;

	private String kmBis;

	private Integer anmelderId;

	private String datumAnmeldung;

	private String kommentarAnmelder;

	private String ergaenzungAnmelder;

	private String beginn;

	private String ende;

	private String fahrplanjahr;

	private Boolean durchgehend;

	private Boolean schichtweise;

	private Integer topprojektId;

	private String technischerPlatz;

	private Integer arbeitenId;

	private Integer arbeitenOrtId;

	private Integer vtrId;

	private boolean wtsMo;

	private boolean wtsDi;

	private boolean wtsMi;

	private boolean wtsDo;

	private boolean wtsFr;

	private boolean wtsSa;

	private boolean wtsSo;

	private String arbeitenKommentar;

	private Integer phaseId;

	private Boolean genehmigungsanforderung;

	private Boolean bbei;

	private Boolean laEintrag406;

	private Boolean lueHinweis;

	private Integer folgeNichtausfuehrungId;

	private String folgeNichtausfuehrungBeginn;

	private String folgeNichtausfuehrungFzv;

	private String folgeNichtausfuehrungGeschwindigkeitLa;

	private Integer grossmaschineId;

	private String statusBbzr;

	private String hervorhebFarbe;

	private Integer vorbedingungId;

	private Integer unterdeckungId;

	private Integer ankermassnahmeId;

	public MassnahmeForm() {
		reset();
	}

	public void reset() {
		massnahmeId = null;
		regionalbereichId = null;
		entwurf = false;
		studie = false;
		finanztypId = null;
		geaendert = false;
		gravierendeAenderung = false;
		strecke = null;
		betriebsstelleVonId = null;
		betriebsstelleBisId = null;
		richtungskennzahl = null;
		kmVon = null;
		kmBis = null;
		anmelderId = null;
		datumAnmeldung = null;
		kommentarAnmelder = null;
		ergaenzungAnmelder = null;
		beginn = null;
		ende = null;
		fahrplanjahr = null;
		durchgehend = false;
		schichtweise = false;
		topprojektId = null;
		technischerPlatz = null;
		arbeitenId = null;
		arbeitenOrtId = null;
		vtrId = null;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
		arbeitenKommentar = null;
		phaseId = null;
		genehmigungsanforderung = false;
		bbei = false;
		laEintrag406 = false;
		lueHinweis = false;
		folgeNichtausfuehrungId = null;
		folgeNichtausfuehrungBeginn = null;
		folgeNichtausfuehrungFzv = null;
		folgeNichtausfuehrungGeschwindigkeitLa = null;
		grossmaschineId = null;
		statusBbzr = null;
		hervorhebFarbe = null;
		vorbedingungId = null;
		unterdeckungId = null;
		ankermassnahmeId = null;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors actionErrors = super.validate(arg0, arg1);

		if (actionErrors.size() == 0)
			// Plausibilitätsprüfung nur durchführen, wenn alle Pflichtfelder mit Werten belegt sind
			actionErrors.add(SAPMassnahmeValidator.validate(this));

		return actionErrors;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

		entwurf = false;
		studie = false;
		geaendert = false;
		gravierendeAenderung = false;
		durchgehend = false;
		schichtweise = false;
		genehmigungsanforderung = false;
		bbei = false;
		laEintrag406 = false;
		lueHinweis = false;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
	}

	public Integer getMassnahmeId() {
		return massnahmeId;
	}

	public void setMassnahmeId(Integer massnahmeId) {
		this.massnahmeId = massnahmeId;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public Boolean getEntwurf() {
		return entwurf;
	}

	public void setEntwurf(Boolean entwurf) {
		this.entwurf = entwurf;
	}

	public Boolean getStudie() {
		return studie;
	}

	public void setStudie(Boolean studie) {
		this.studie = studie;
	}

	public Integer getFinanztypId() {
		return finanztypId;
	}

	public void setFinanztypId(Integer finanztypId) {
		this.finanztypId = finanztypId;
	}

	public Boolean getGeaendert() {
		return geaendert;
	}

	public void setGeaendert(Boolean geaendert) {
		this.geaendert = geaendert;
	}

	public Boolean getGravierendeAenderung() {
		return gravierendeAenderung;
	}

	public void setGravierendeAenderung(Boolean gravierendeAenderung) {
		this.gravierendeAenderung = gravierendeAenderung;
	}

	public String getStrecke() {
		return strecke;
	}

	public void setStrecke(String strecke) {
		this.strecke = strecke;
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

	public Integer getRichtungskennzahl() {
		return richtungskennzahl;
	}

	public void setRichtungskennzahl(Integer richtungskennzahl) {
		this.richtungskennzahl = richtungskennzahl;
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

	public Integer getAnmelderId() {
		return anmelderId;
	}

	public void setAnmelderId(Integer anmelderId) {
		this.anmelderId = anmelderId;
	}

	public String getDatumAnmeldung() {
		return datumAnmeldung;
	}

	public void setDatumAnmeldung(String datumAnmeldung) {
		this.datumAnmeldung = datumAnmeldung;
	}

	public String getKommentarAnmelder() {
		return kommentarAnmelder;
	}

	public void setKommentarAnmelder(String kommentarAnmelder) {
		this.kommentarAnmelder = kommentarAnmelder;
	}

	public String getErgaenzungAnmelder() {
		return ergaenzungAnmelder;
	}

	public void setErgaenzungAnmelder(String ergaenzungAnmelder) {
		this.ergaenzungAnmelder = ergaenzungAnmelder;
	}

	public String getBeginn() {
		return beginn;
	}

	public void setBeginn(String beginn) {
		this.beginn = beginn;
	}

	public String getEnde() {
		return ende;
	}

	public void setEnde(String ende) {
		this.ende = ende;
	}

	public String getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(String fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
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

	public Integer getTopprojektId() {
		return topprojektId;
	}

	public void setTopprojektId(Integer topprojektId) {
		this.topprojektId = topprojektId;
	}

	public String getTechnischerPlatz() {
		return technischerPlatz;
	}

	public void setTechnischerPlatz(String technischerPlatz) {
		this.technischerPlatz = technischerPlatz;
	}

	public Integer getArbeitenId() {
		return arbeitenId;
	}

	public void setArbeitenId(Integer arbeitenId) {
		this.arbeitenId = arbeitenId;
	}

	public Integer getArbeitenOrtId() {
		return arbeitenOrtId;
	}

	public void setArbeitenOrtId(Integer arbeitenOrtId) {
		this.arbeitenOrtId = arbeitenOrtId;
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

	public String getArbeitenKommentar() {
		return arbeitenKommentar;
	}

	public void setArbeitenKommentar(String arbeitenKommentar) {
		this.arbeitenKommentar = arbeitenKommentar;
	}

	public Integer getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Integer phaseId) {
		this.phaseId = phaseId;
	}

	public Boolean getGenehmigungsanforderung() {
		return genehmigungsanforderung;
	}

	public void setGenehmigungsanforderung(Boolean genehmigungsanforderung) {
		this.genehmigungsanforderung = genehmigungsanforderung;
	}

	public Boolean getBbei() {
		return bbei;
	}

	public void setBbei(Boolean bbei) {
		this.bbei = bbei;
	}

	public Boolean getLaEintrag406() {
		return laEintrag406;
	}

	public void setLaEintrag406(Boolean laEintrag406) {
		this.laEintrag406 = laEintrag406;
	}

	public Boolean getLueHinweis() {
		return lueHinweis;
	}

	public void setLueHinweis(Boolean lueHinweis) {
		this.lueHinweis = lueHinweis;
	}

	public Integer getFolgeNichtausfuehrungId() {
		return folgeNichtausfuehrungId;
	}

	public void setFolgeNichtausfuehrungId(Integer folgeNichtausfuehrungId) {
		this.folgeNichtausfuehrungId = folgeNichtausfuehrungId;
	}

	public String getFolgeNichtausfuehrungBeginn() {
		return folgeNichtausfuehrungBeginn;
	}

	public void setFolgeNichtausfuehrungBeginn(String folgeNichtausfuehrungBeginn) {
		this.folgeNichtausfuehrungBeginn = folgeNichtausfuehrungBeginn;
	}

	public String getFolgeNichtausfuehrungFzv() {
		return folgeNichtausfuehrungFzv;
	}

	public void setFolgeNichtausfuehrungFzv(String folgeNichtausfuehrungFzv) {
		this.folgeNichtausfuehrungFzv = folgeNichtausfuehrungFzv;
	}

	public String getFolgeNichtausfuehrungGeschwindigkeitLa() {
		return folgeNichtausfuehrungGeschwindigkeitLa;
	}

	public void setFolgeNichtausfuehrungGeschwindigkeitLa(
	    String folgeNichtausfuehrungGeschwindigkeitLa) {
		this.folgeNichtausfuehrungGeschwindigkeitLa = folgeNichtausfuehrungGeschwindigkeitLa;
	}

	public Integer getGrossmaschineId() {
		return grossmaschineId;
	}

	public void setGrossmaschineId(Integer grossmaschineId) {
		this.grossmaschineId = grossmaschineId;
	}

	public String getStatusBbzr() {
		return statusBbzr;
	}

	public void setStatusBbzr(String statusBbzr) {
		this.statusBbzr = statusBbzr;
	}

	public String getHervorhebFarbe() {
		return hervorhebFarbe;
	}

	public void setHervorhebFarbe(String hervorhebFarbe) {
		this.hervorhebFarbe = hervorhebFarbe;
	}

	public Integer getVorbedingungId() {
		return vorbedingungId;
	}

	public void setVorbedingungId(Integer vorbedingungId) {
		this.vorbedingungId = vorbedingungId;
	}

	public Integer getUnterdeckungId() {
		return unterdeckungId;
	}

	public void setUnterdeckungId(Integer unterdeckungId) {
		this.unterdeckungId = unterdeckungId;
	}

	public Integer getAnkermassnahmeId() {
		return ankermassnahmeId;
	}

	public void setAnkermassnahmeId(Integer ankermassnahmeId) {
		this.ankermassnahmeId = ankermassnahmeId;
	}
}
