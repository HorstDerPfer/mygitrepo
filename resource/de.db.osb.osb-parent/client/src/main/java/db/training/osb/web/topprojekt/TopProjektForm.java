package db.training.osb.web.topprojekt;

import db.training.easy.web.BaseValidatorForm;

@SuppressWarnings("serial")
public class TopProjektForm extends BaseValidatorForm {

	private Integer topProjektId;

	private Integer anmelderId;

	private String name;

	private String baukosten;

	private String sapProjektNummer;

	private Integer regionalbereichId;

	private Boolean delete;

	public TopProjektForm() {
		reset();
	}

	public void reset() {
		setTopProjektId(null);
		setAnmelderId(null);
		setName(null);
		setBaukosten(null);
		setSapProjektNummer(null);
		setRegionalbereichId(null);
		setDelete(null);
	}

	public Integer getTopProjektId() {
		return topProjektId;
	}

	public void setTopProjektId(Integer topProjektId) {
		this.topProjektId = topProjektId;
	}

	public Integer getAnmelderId() {
		return anmelderId;
	}

	public void setAnmelderId(Integer anmelderId) {
		this.anmelderId = anmelderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaukosten() {
		return baukosten;
	}

	public void setBaukosten(String baukosten) {
		this.baukosten = baukosten;
	}

	public String getSapProjektNummer() {
		return sapProjektNummer;
	}

	public void setSapProjektNummer(String sapProjektNummer) {
		this.sapProjektNummer = sapProjektNummer;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

}
