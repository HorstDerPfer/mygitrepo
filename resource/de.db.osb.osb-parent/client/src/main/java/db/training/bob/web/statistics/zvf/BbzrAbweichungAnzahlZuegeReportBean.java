package db.training.bob.web.statistics.zvf;

import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.web.statistics.AbstractReportBean;

public class BbzrAbweichungAnzahlZuegeReportBean extends AbstractReportBean {

	private int umgeleitet = 0;

	private int verspaetet = 0;

	private int vorPlan = 0;

	private int ausfall = 0;

	private int ausfallVerkehrshalt = 0;

	private int bedarfsplanGesperrt = 0;

	private int regelung = 0;

	private int gesamt = 0;

	public BbzrAbweichungAnzahlZuegeReportBean(String key) {
		super(key);
	}

	public void increment(Abweichungsart art) {
		if (art == Abweichungsart.UMLEITUNG)
			umgeleitet++;
		if (art == Abweichungsart.VERSPAETUNG)
			verspaetet++;
		if (art == Abweichungsart.VORPLAN)
			vorPlan++;
		if (art == Abweichungsart.AUSFALL)
			ausfall++;
		if (art == Abweichungsart.ERSATZHALTE)
			ausfallVerkehrshalt++;
		if (art == Abweichungsart.GESPERRT)
			bedarfsplanGesperrt++;
		if (art == Abweichungsart.REGELUNG)
			regelung++;
		gesamt++;
	}

	public int getUmgeleitet() {
		return umgeleitet;
	}

	public void setUmgeleitet(int umgeleitet) {
		this.umgeleitet = umgeleitet;
	}

	public int getVerspaetet() {
		return verspaetet;
	}

	public void setVerspaetet(int verspaetet) {
		this.verspaetet = verspaetet;
	}

	public int getVorPlan() {
		return vorPlan;
	}

	public void setVorPlan(int vorPlan) {
		this.vorPlan = vorPlan;
	}

	public int getAusfall() {
		return ausfall;
	}

	public void setAusfall(int ausfall) {
		this.ausfall = ausfall;
	}

	public int getAusfallVerkehrshalt() {
		return ausfallVerkehrshalt;
	}

	public void setAusfallVerkehrshalt(int ausfallVerkehrshalt) {
		this.ausfallVerkehrshalt = ausfallVerkehrshalt;
	}

	public int getBedarfsplanGesperrt() {
		return bedarfsplanGesperrt;
	}

	public void setBedarfsplanGesperrt(int bedarfsplanGesperrt) {
		this.bedarfsplanGesperrt = bedarfsplanGesperrt;
	}

	public int getRegelung() {
		return regelung;
	}

	public void setRegelung(int regelung) {
		this.regelung = regelung;
	}

	public int getGesamt() {
		return gesamt;
	}

	public void setGesamt(int gesamt) {
		this.gesamt = gesamt;
	}

}
