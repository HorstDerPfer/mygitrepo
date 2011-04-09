package db.training.osb.web.sperrpausenbedarf;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.displaytag.pagination.PaginatedList;

@SuppressWarnings("serial")
public class SperrpausenbedarfFilterForm extends ValidatorForm {

	public final static int DEFAULTPAGESIZE = PaginatedList.LIST_OBJECTS_PER_PAGE;

	private Integer pageSize;

	private String anmelder;

	private String gewerk;

	private String untergewerk;

	private Integer regionalbereichId;

	private String streckeVZG;

	private String paket;

	private String sapProjektnummer;
	
	private String datumVon;

	private String datumBis;

	public void reset() {
		pageSize = null;
		gewerk = null;
		anmelder = null;
		untergewerk = null;
		regionalbereichId = null;
		paket = null;
		streckeVZG = null;
		sapProjektnummer = null;
		datumVon = null;
		datumBis = null;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getAnmelder() {
		return anmelder;
	}

	public void setAnmelder(String anmelder) {
		this.anmelder = anmelder;
	}

	public String getGewerk() {
		return gewerk;
	}

	public void setGewerk(String gewerk) {
		this.gewerk = gewerk;
	}

	public String getUntergewerk() {
		return untergewerk;
	}

	public void setUntergewerk(String untergewerk) {
		this.untergewerk = untergewerk;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public String getStreckeVZG() {
		return streckeVZG;
	}

	public void setStreckeVZG(String streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public String getPaket() {
		return paket;
	}

	public void setPaket(String paket) {
		this.paket = paket;
	}

	public String getSapProjektnummer() {
		return sapProjektnummer;
	}

	public void setSapProjektnummer(String sapProjektnummer) {
		this.sapProjektnummer = sapProjektnummer;
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
