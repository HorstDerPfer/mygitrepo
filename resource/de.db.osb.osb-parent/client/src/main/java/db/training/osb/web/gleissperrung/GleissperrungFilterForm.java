package db.training.osb.web.gleissperrung;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.displaytag.pagination.PaginatedList;

@SuppressWarnings("serial")
public class GleissperrungFilterForm extends ValidatorForm {

	public final static int DEFAULTPAGESIZE = PaginatedList.LIST_OBJECTS_PER_PAGE;

	private Integer pageSize = DEFAULTPAGESIZE;

	private String betriebsstelleVon;

	private String betriebsstelleBis;

	private Integer regionalbereichId;

	private String streckeVZG;
	
	private String datumVon;

	private String datumBis;

	public void reset() {
		pageSize = DEFAULTPAGESIZE;
		betriebsstelleVon = null;
		betriebsstelleBis = null;
		regionalbereichId = null;
		streckeVZG = null;
		datumVon = null;
		datumBis = null;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the regionalbereich
	 */
	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	/**
	 * @param regionalbereich
	 *            the regionalbereich to set
	 */
	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	/**
	 * @return the streckeVZG
	 */
	public String getStreckeVZG() {
		return streckeVZG;
	}

	/**
	 * @param streckeVZG
	 *            the streckeVZG to set
	 */
	public void setStreckeVZG(String streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public String getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(String betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public String getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(String betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
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
