package db.training.osb.web.topprojekt;

import org.apache.struts.action.ActionForm;

public class TopProjektFilterForm extends ActionForm {

	private static final long serialVersionUID = -2812638517466817996L;

	private String sapProjektNummer;
	
	private String datumVon;

	private String datumBis;

	public TopProjektFilterForm() {
		reset();
	}
	
	public void reset() {
		setSapProjektNummer(null);
		setDatumVon(null);
		setDatumBis(null);
	}

	public String getSapProjektNummer() {
		return sapProjektNummer;
	}
	
	public void setSapProjektNummer(String sapProjektNummer) {
		this.sapProjektNummer = sapProjektNummer;
	}

	public String getDatumVon() {
    	return datumVon;
    }

	public void setDatumVon(String datumVon) {
    	this.datumVon = datumVon;
    }

	public String getDatumBis() {
    	return datumBis;
    }

	public void setDatumBis(String datumBis) {
    	this.datumBis = datumBis;
    }
}
