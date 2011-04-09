package db.training.bob.web.statistics.zvf;

import java.util.ArrayList;
import java.util.List;

import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.web.statistics.AbstractReportBean;

public class BbzrAbweichungZuegeNummernReportBean extends AbstractReportBean {

	private List<String> zuegeUmgeleitet = new ArrayList<String>();

	private List<String> zuegeVerspaetet = new ArrayList<String>();

	private List<String> zuegeVorPlan = new ArrayList<String>();

	private List<String> zuegeAusfall = new ArrayList<String>();

	private List<String> zuegeAusfallVerkehrshalt = new ArrayList<String>();

	private List<String> zuegeBedarfsplanGesperrt = new ArrayList<String>();

	private List<String> zuegeRegelung = new ArrayList<String>();

	public BbzrAbweichungZuegeNummernReportBean(String key) {
		super(key);
	}

	public void addZug(Abweichungsart art, Zug zug) {
		if (zug != null)
			addZug(art, zug.getZugnr().toString());
	}

	public void addZug(Abweichungsart art, String zugNr) {
		if (art == Abweichungsart.UMLEITUNG)
			zuegeUmgeleitet.add(zugNr);
		if (art == Abweichungsart.VERSPAETUNG)
			zuegeVerspaetet.add(zugNr);
		if (art == Abweichungsart.VORPLAN)
			zuegeVorPlan.add(zugNr);
		if (art == Abweichungsart.AUSFALL)
			zuegeAusfall.add(zugNr);
		if (art == Abweichungsart.ERSATZHALTE)
			zuegeAusfallVerkehrshalt.add(zugNr);
		if (art == Abweichungsart.GESPERRT)
			zuegeBedarfsplanGesperrt.add(zugNr);
		if (art == Abweichungsart.REGELUNG)
			zuegeRegelung.add(zugNr);
	}

	public List<String> getZuegeUmgeleitet() {
		return zuegeUmgeleitet;
	}

	public void setZuegeUmgeleitet(List<String> zuegeUmgeleitet) {
		this.zuegeUmgeleitet = zuegeUmgeleitet;
	}

	public List<String> getZuegeVerspaetet() {
		return zuegeVerspaetet;
	}

	public void setZuegeVerspaetet(List<String> zuegeVerspaetet) {
		this.zuegeVerspaetet = zuegeVerspaetet;
	}

	public List<String> getZuegeVorPlan() {
		return zuegeVorPlan;
	}

	public void setZuegeVorPlan(List<String> zuegeVorPlan) {
		this.zuegeVorPlan = zuegeVorPlan;
	}

	public List<String> getZuegeAusfall() {
		return zuegeAusfall;
	}

	public void setZuegeAusfall(List<String> zuegeAusfall) {
		this.zuegeAusfall = zuegeAusfall;
	}

	public List<String> getZuegeAusfallVerkehrshalt() {
		return zuegeAusfallVerkehrshalt;
	}

	public void setZuegeAusfallVerkehrshalt(List<String> zuegeAusfallVerkehrshalt) {
		this.zuegeAusfallVerkehrshalt = zuegeAusfallVerkehrshalt;
	}

	public List<String> getZuegeBedarfsplanGesperrt() {
		return zuegeBedarfsplanGesperrt;
	}

	public void setZuegeBedarfsplanGesperrt(List<String> zuegeBedarfsplanGesperrt) {
		this.zuegeBedarfsplanGesperrt = zuegeBedarfsplanGesperrt;
	}

	public List<String> getZuegeRegelung() {
		return zuegeRegelung;
	}

	public void setZuegeRegelung(List<String> zuegeRegelung) {
		this.zuegeRegelung = zuegeRegelung;
	}

}
