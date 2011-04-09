package db.training.osb.web.massnahme;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseValidatorForm;
import db.training.osb.validation.GleissperrungValidator;
import db.training.osb.validation.LangsamfahrstelleValidator;
import db.training.osb.validation.OberleitungValidator;

@SuppressWarnings("serial")
public class MassnahmeRegelungForm extends BaseValidatorForm {

	public static final String GLEISSPERRUNG_METHOD = "gleissperrung";

	public static final String OBERLEITUNG_METHOD = "oberleitung";

	public static final String LANGSAMFAHRSTELLE_METHOD = "langsamfahrstelle";

	/* Zur Steuerung der DispatchAction */
	private String method;

	private Integer massnahmeId;

	private Integer regelungId;

	private Boolean ungueltig;

	private String strecke;

	private Integer betriebsstelleVonId;

	private Integer betriebsstelleBisId;

	private Integer richtungskennzahl;

	private String kmVon;

	private String kmBis;

	private String beginn;

	private String ende;

	private Boolean durchgehend;

	private Boolean schichtweise;

	private boolean wtsMo;

	private boolean wtsDi;

	private boolean wtsMi;

	private boolean wtsDo;

	private boolean wtsFr;

	private boolean wtsSa;

	private boolean wtsSo;

	private Boolean betroffenSpfv;

	private Boolean betroffenSpnv;

	private Boolean betroffenSgv;

	private String kommentar;

	private String schaltgruppen;

	private String sigWeicheVon;

	private String sigWeicheBis;

	String fzvMusterzug;

	String geschwindigkeitLa;

	String geschwindigkeitVzg;

	String laTyp;

	Integer betriebsweiseId;

	String ergaenzungBetriebsweise;

	Boolean vorschlagLisba;

	public MassnahmeRegelungForm() {
		reset();
	}

	public void reset() {
		method = null;
		massnahmeId = null;
		regelungId = null;
		strecke = null;
		betriebsstelleVonId = null;
		betriebsstelleBisId = null;
		richtungskennzahl = null;
		kmVon = null;
		kmBis = null;
		beginn = null;
		ende = null;
		ungueltig = false;
		durchgehend = false;
		schichtweise = false;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
		betroffenSpfv = false;
		betroffenSpnv = false;
		betroffenSgv = false;
		kommentar = null;
		schaltgruppen = null;
		sigWeicheVon = null;
		sigWeicheBis = null;
		fzvMusterzug = null;
		geschwindigkeitLa = null;
		geschwindigkeitVzg = null;
		laTyp = null;
		betriebsweiseId = null;
		ergaenzungBetriebsweise = null;
		vorschlagLisba = false;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		ungueltig = false;
		durchgehend = false;
		schichtweise = false;
		wtsMo = false;
		wtsDi = false;
		wtsMi = false;
		wtsDo = false;
		wtsFr = false;
		wtsSa = false;
		wtsSo = false;
		betroffenSpfv = false;
		betroffenSpnv = false;
		betroffenSgv = false;
		vorschlagLisba = false;
	}

	public void enableAllWtsDays() {
		wtsMo = true;
		wtsDi = true;
		wtsMi = true;
		wtsDo = true;
		wtsFr = true;
		wtsSa = true;
		wtsSo = true;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors actionErrors = super.validate(arg0, arg1);

		if (actionErrors.size() == 0 && getMethod() != null) {
			// Plausibilitätsprüfung nur durchführen, wenn alle Pflichtfelder mit Werten belegt
			// sind
			if (getMethod().equals(GLEISSPERRUNG_METHOD))
				actionErrors.add(GleissperrungValidator.validate(this));
			else if (getMethod().equals(OBERLEITUNG_METHOD))
				actionErrors.add(OberleitungValidator.validate(this));
			else if (getMethod().equals(LANGSAMFAHRSTELLE_METHOD))
				actionErrors.add(LangsamfahrstelleValidator.validate(this));
		}

		return actionErrors;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getMassnahmeId() {
		return massnahmeId;
	}

	public void setMassnahmeId(Integer massnahmeId) {
		this.massnahmeId = massnahmeId;
	}

	public Integer getRegelungId() {
		return regelungId;
	}

	public void setRegelungId(Integer regelungId) {
		this.regelungId = regelungId;
	}

	public Boolean getUngueltig() {
		return ungueltig;
	}

	public void setUngueltig(Boolean ungueltig) {
		this.ungueltig = ungueltig;
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

	public Boolean getBetroffenSpfv() {
		return betroffenSpfv;
	}

	public void setBetroffenSpfv(Boolean betroffenSpfv) {
		this.betroffenSpfv = betroffenSpfv;
	}

	public Boolean getBetroffenSpnv() {
		return betroffenSpnv;
	}

	public void setBetroffenSpnv(Boolean betroffenSpnv) {
		this.betroffenSpnv = betroffenSpnv;
	}

	public Boolean getBetroffenSgv() {
		return betroffenSgv;
	}

	public void setBetroffenSgv(Boolean betroffenSgv) {
		this.betroffenSgv = betroffenSgv;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
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

	public String getSchaltgruppen() {
		return schaltgruppen;
	}

	public void setSchaltgruppen(String schaltgruppen) {
		this.schaltgruppen = schaltgruppen;
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

	public String getFzvMusterzug() {
		return fzvMusterzug;
	}

	public void setFzvMusterzug(String fzvMusterzug) {
		this.fzvMusterzug = fzvMusterzug;
	}

	public String getGeschwindigkeitLa() {
		return geschwindigkeitLa;
	}

	public void setGeschwindigkeitLa(String geschwindigkeitLa) {
		this.geschwindigkeitLa = geschwindigkeitLa;
	}

	public String getGeschwindigkeitVzg() {
		return geschwindigkeitVzg;
	}

	public void setGeschwindigkeitVzg(String geschwindigkeitVzg) {
		this.geschwindigkeitVzg = geschwindigkeitVzg;
	}

	public String getLaTyp() {
		return laTyp;
	}

	public void setLaTyp(String laTyp) {
		this.laTyp = laTyp;
	}

	public Integer getBetriebsweiseId() {
		return betriebsweiseId;
	}

	public void setBetriebsweiseId(Integer betriebsweiseId) {
		this.betriebsweiseId = betriebsweiseId;
	}

	public String getErgaenzungBetriebsweise() {
		return ergaenzungBetriebsweise;
	}

	public void setErgaenzungBetriebsweise(String ergaenzungBetriebsweise) {
		this.ergaenzungBetriebsweise = ergaenzungBetriebsweise;
	}

	public Boolean getVorschlagLisba() {
		return vorschlagLisba;
	}

	public void setVorschlagLisba(Boolean vorschlagLisba) {
		this.vorschlagLisba = vorschlagLisba;
	}

}
