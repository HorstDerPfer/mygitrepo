package db.training.bob.web.statistics;

/**
 * beschreibt eine Zeile des Berichts 'Termineinhaltung Meilensteine für mehrere Maßnahmen'
 * 
 * @author michels
 * 
 */
public class DelaysReportBean {

	private String label = null;

	private int zvFEntwurf = 0;

	private int stellungNameEVU = 0;

	private int zvF = 0;

	private int masterUebergabeblattPV = 0;

	private int masterUebergabeblattGV = 0;

	private int uebergabeblattPV = 0;

	private int uebergabeblattGV = 0;

	private int fplo = 0;

	private int eingabeGFDZ = 0;

	public DelaysReportBean(String label) {
		this.setLabel(label);
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/*
	 * public void addDate(Date soll, Date ist, Status status) { if(ist == null) ist = new Date();
	 * 
	 * if(status == Status.GREEN) { totalCount += 1; }
	 * 
	 * if(status == Status.RED) { final int MILLSECS_PER_DAY = 1000 * 60 *60 * 24; long deltaDays =
	 * 0; deltaDays = ( ist.getTime() - soll.getTime() ) / MILLSECS_PER_DAY;
	 * 
	 * if(deltaDays>=20) { delayed20DaysCount++; } else if(deltaDays>=10) { delayed10DaysCount++; }
	 * else if(deltaDays>=5) { delayed5DaysCount++; } else { delayedLess5DaysCount++; }
	 * 
	 * totalCount++; delayedCount++; } }
	 */

	/**
	 * versucht die übergebene Zeichenfolge in einen ganzzahligen Wert zu konvertieren. Wenn die
	 * Zeichenfolge nicht konvertiert werden kann wird 0 zurückgegeben.
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unused")
	private int toInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * @param zvFEntwurf
	 *            the zvFEntwurf to set
	 */
	public void setZvFEntwurf(int zvFEntwurf) {
		this.zvFEntwurf = zvFEntwurf;
	}

	/**
	 * @return the zvFEntwurf
	 */
	public int getZvFEntwurf() {
		return zvFEntwurf;
	}

	/**
	 * @param stellungNameEVU
	 *            the stellungNameEVU to set
	 */
	public void setStellungNameEVU(int stellungNameEVU) {
		this.stellungNameEVU = stellungNameEVU;
	}

	/**
	 * @return the stellungNameEVU
	 */
	public int getStellungNameEVU() {
		return stellungNameEVU;
	}

	/**
	 * @param zvF
	 *            the zvF to set
	 */
	public void setZvF(int zvF) {
		this.zvF = zvF;
	}

	/**
	 * @return the zvF
	 */
	public int getZvF() {
		return zvF;
	}

	/**
	 * @param masterUebergabeblattPV
	 *            the masterUebergabeblattPV to set
	 */
	public void setMasterUebergabeblattPV(int masterUebergabeblattPV) {
		this.masterUebergabeblattPV = masterUebergabeblattPV;
	}

	/**
	 * @return the masterUebergabeblattPV
	 */
	public int getMasterUebergabeblattPV() {
		return masterUebergabeblattPV;
	}

	/**
	 * @param masterUebergabeblattGV
	 *            the masterUebergabeblattGV to set
	 */
	public void setMasterUebergabeblattGV(int masterUebergabeblattGV) {
		this.masterUebergabeblattGV = masterUebergabeblattGV;
	}

	/**
	 * @return the masterUebergabeblattGV
	 */
	public int getMasterUebergabeblattGV() {
		return masterUebergabeblattGV;
	}

	/**
	 * @param uebergabeblattPV
	 *            the uebergabeblattPV to set
	 */
	public void setUebergabeblattPV(int uebergabeblattPV) {
		this.uebergabeblattPV = uebergabeblattPV;
	}

	/**
	 * @return the uebergabeblattPV
	 */
	public int getUebergabeblattPV() {
		return uebergabeblattPV;
	}

	/**
	 * @param uebergabeblattGV
	 *            the uebergabeblattGV to set
	 */
	public void setUebergabeblattGV(int uebergabeblattGV) {
		this.uebergabeblattGV = uebergabeblattGV;
	}

	/**
	 * @return the uebergabeblattGV
	 */
	public int getUebergabeblattGV() {
		return uebergabeblattGV;
	}

	/**
	 * @param fplo
	 *            the fplo to set
	 */
	public void setFplo(int fplo) {
		this.fplo = fplo;
	}

	/**
	 * @return the fplo
	 */
	public int getFplo() {
		return fplo;
	}

	/**
	 * @param eingabeGFDZ
	 *            the eingabeGFDZ to set
	 */
	public void setEingabeGFDZ(int eingabeGFDZ) {
		this.eingabeGFDZ = eingabeGFDZ;
	}

	/**
	 * @return the eingabeGFDZ
	 */
	public int getEingabeGFDZ() {
		return eingabeGFDZ;
	}

}
