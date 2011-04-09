package db.training.osb.web.buendel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class BuendelSearchForm extends ValidatorForm {

	public final static int DEFAULTPAGESIZE = 20;

	private String hauptStreckeNummer;

	private Integer pageSize = DEFAULTPAGESIZE;

	private Integer regionalbereichId;

	private String searchBuendelName;

	private String buendelKennung;

	private List<Integer> ids;

	private String betriebsstelleVon;

	private String betriebsstelleBis;
	
	private String datumVon;

	private String datumBis;

	public void reset() {

		pageSize = DEFAULTPAGESIZE;

		regionalbereichId = null;
		searchBuendelName = null;
		hauptStreckeNummer = null;
		buendelKennung = null;
		betriebsstelleVon = null;
		betriebsstelleBis = null;
		datumVon = null;
		datumBis = null;

		setIds(new ArrayList<Integer>());

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
	 * @return the buendelName
	 */
	public String getSearchBuendelName() {
		return searchBuendelName;
	}

	/**
	 * @param buendelName
	 *            the buendelName to set
	 */
	public void setSearchBuendelName(String buendelName) {
		this.searchBuendelName = buendelName;
	}

	/**
	 * @return the hauptStreckeNummer
	 */
	public String getHauptStreckeNummer() {
		return hauptStreckeNummer;
	}

	/**
	 * @param hauptStreckeNummer
	 *            the hauptStreckeNummer to set
	 */
	public void setHauptStreckeNummer(String hauptStreckeNummer) {
		this.hauptStreckeNummer = hauptStreckeNummer;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getBuendelKennung() {
		return buendelKennung;
	}

	public void setBuendelKennung(String buendelKennung) {
		this.buendelKennung = buendelKennung;
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
