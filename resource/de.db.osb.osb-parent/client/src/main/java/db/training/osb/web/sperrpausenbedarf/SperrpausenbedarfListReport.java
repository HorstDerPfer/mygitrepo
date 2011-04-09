package db.training.osb.web.sperrpausenbedarf;

import db.training.osb.model.SAPMassnahme;

/**
 * Author: Sebastian Hennebrueder, http://www.laliluna.de Date: 03.02.2010
 */
public class SperrpausenbedarfListReport extends SAPMassnahme {

	private static final long serialVersionUID = 6303951469675982231L;

	private Integer regionalBereichId;

	private String regionalBereichName;

	private Integer hauptstreckeNummer;

	public SperrpausenbedarfListReport() {
	}

	public Integer getRegionalBereichId() {
		return regionalBereichId;
	}

	public void setRegionalBereichId(Integer regionalBereichId) {
		this.regionalBereichId = regionalBereichId;
	}

	public String getRegionalBereichName() {
		return regionalBereichName;
	}

	public void setRegionalBereichName(String regionalBereichName) {
		this.regionalBereichName = regionalBereichName;
	}

	public Integer getHauptstreckeNummer() {
		return hauptstreckeNummer;
	}

	public void setHauptstreckeNummer(Integer hauptstreckeNummer) {
		this.hauptstreckeNummer = hauptstreckeNummer;
	}

	/* Methoden ueberschreiben, damit der ResultTransformer funktioniert */

	@Override
	public Integer getLfdNr() {
		return super.getLfdNr();
	}

	@Override
	public void setLfdNr(Integer lfdNr) {
		super.setLfdNr(lfdNr);
	}

	@Override
	public String getUntergewerk() {
		return super.getUntergewerk();
	}

	@Override
	public void setUntergewerk(String untergewerk) {
		super.setUntergewerk(untergewerk);
	}

	@Override
	public Integer getId() {
		return super.getId();
	}

	@Override
	public void setId(Integer id) {
		super.setId(id);
	}

	@Override
	public String getProjektDefinitionDbBez() {
		return super.getProjektDefinitionDbBez();
	}

	@Override
	public void setProjektDefinitionDbBez(String projektDefinitionDbBez) {
		super.setProjektDefinitionDbBez(projektDefinitionDbBez);
	}

	/**
	 * Dieser Code ist dupliziert in der Klasse {@link db.training.osb.model.SAPMassnahme}
	 * 
	 * @return
	 */
	public String getMassnahmeId() {
		return String.format("%06d.%04d.%02d.%02d", getLfdNr() != null ? getLfdNr() : 0,
		    hauptstreckeNummer != null ? hauptstreckeNummer : 0,
		    regionalBereichId != null ? regionalBereichId : 0,
		    getUrspruenglichesPlanungsjahr() != null ? getUrspruenglichesPlanungsjahr() - 2000 : 0);
	}

}
