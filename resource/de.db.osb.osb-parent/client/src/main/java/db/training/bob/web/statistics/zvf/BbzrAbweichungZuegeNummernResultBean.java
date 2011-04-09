package db.training.bob.web.statistics.zvf;

public class BbzrAbweichungZuegeNummernResultBean {

	private String betreiber;

	private String zugNr;

	private boolean festgelegtSgv;

	private boolean festgelegtSpfv;

	private boolean festgelegtSpnv;

	private boolean verspaetung;

	private boolean umleitung;

	private boolean ausfall;

	private boolean vorplan;

	private boolean gesperrt;

	private boolean ersatzhalte;

	private boolean regelung;

	public boolean isFestgelegtSgv() {
		return festgelegtSgv;
	}

	public void setFestgelegtSgv(boolean festgelegtSgv) {
		this.festgelegtSgv = festgelegtSgv;
	}

	public boolean isFestgelegtSpfv() {
		return festgelegtSpfv;
	}

	public void setFestgelegtSpfv(boolean festgelegtSpfv) {
		this.festgelegtSpfv = festgelegtSpfv;
	}

	public boolean isFestgelegtSpnv() {
		return festgelegtSpnv;
	}

	public void setFestgelegtSpnv(boolean festgelegtSpnv) {
		this.festgelegtSpnv = festgelegtSpnv;
	}

	public String getBetreiber() {
		return betreiber;
	}

	public void setBetreiber(String betreiber) {
		this.betreiber = betreiber;
	}

	public String getZugNr() {
		return zugNr;
	}

	public void setZugNr(String zugNr) {
		this.zugNr = zugNr;
	}

	public boolean isVerspaetung() {
		return verspaetung;
	}

	public void setVerspaetung(boolean verspaetung) {
		this.verspaetung = verspaetung;
	}

	public boolean isUmleitung() {
		return umleitung;
	}

	public void setUmleitung(boolean umleitung) {
		this.umleitung = umleitung;
	}

	public boolean isAusfall() {
		return ausfall;
	}

	public void setAusfall(boolean ausfall) {
		this.ausfall = ausfall;
	}

	public boolean isVorplan() {
		return vorplan;
	}

	public void setVorplan(boolean vorplan) {
		this.vorplan = vorplan;
	}

	public boolean isGesperrt() {
		return gesperrt;
	}

	public void setGesperrt(boolean gesperrt) {
		this.gesperrt = gesperrt;
	}

	public boolean isErsatzhalte() {
		return ersatzhalte;
	}

	public void setErsatzhalte(boolean ersatzhalte) {
		this.ersatzhalte = ersatzhalte;
	}

	public boolean isRegelung() {
		return regelung;
	}

	public void setRegelung(boolean regelung) {
		this.regelung = regelung;
	}
}
