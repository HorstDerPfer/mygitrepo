package db.training.bob.web.baumassnahme;

import org.apache.struts.validator.ValidatorForm;

import db.training.bob.model.Baumassnahme;

@SuppressWarnings("serial")
public class RegelungForm extends ValidatorForm {

	private Integer regelungId;
	private Integer bbpMassnahmeId;
	private Integer baumassnahmeId;
	private boolean beginnFuerTerminberechnung;
	private String beginn;
	private String ende;
	
	public Integer getBaumassnahmeId() {
    	return baumassnahmeId;
    }

	public void setBaumassnahmeId(Integer baumassnahmeId) {
    	this.baumassnahmeId = baumassnahmeId;
    }
	
	public String getBeginn() {
    	return beginn;
    }

	public String getEnde() {
    	return ende;
    }

	public void setBeginn(String beginn) {
    	this.beginn = beginn;
    }

	public void setEnde(String ende) {
    	this.ende = ende;
    }

	public Integer getRegelungId() {
    	return regelungId;
    }

	public void setRegelungId(Integer regelungId) {
    	this.regelungId = regelungId;
    }

	public boolean isBeginnFuerTerminberechnung() {
    	return beginnFuerTerminberechnung;
    }
	public void setBeginnFuerTerminberechnung(boolean beginnFuerTerminberechnung) {
    	this.beginnFuerTerminberechnung = beginnFuerTerminberechnung;
    }

	public Integer getBbpMassnahmeId() {
    	return bbpMassnahmeId;
    }

	public void setBbpMassnahmeId(Integer bbpMassnahmeId) {
    	this.bbpMassnahmeId = bbpMassnahmeId;
    }

}
