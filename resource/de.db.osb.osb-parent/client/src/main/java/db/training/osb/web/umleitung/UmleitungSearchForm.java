package db.training.osb.web.umleitung;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class UmleitungSearchForm extends ValidatorForm {

	public final static int DEFAULTPAGESIZE = 20;

	private String name;

	private Integer pageSize = DEFAULTPAGESIZE;

	private String betriebsStelle;

	private String vzgStreckenNummer;
	
	private Integer regionalbereichId;

	private List<Integer> ids;

	public void reset() {
		name = null;
		vzgStreckenNummer = null;
		regionalbereichId = null;
		betriebsStelle = null;
		pageSize = DEFAULTPAGESIZE;
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

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the betriebstelleVon
	 */
	public String getBetriebsStelle() {
		return betriebsStelle;
	}

	/**
	 * @param betriebstelleVon
	 *            the betriebstelleVon to set
	 */
	public void setBetriebsStelle(String betriebsStelle) {
		this.betriebsStelle = betriebsStelle;
	}

	public String getVzgStreckenNummer() {
    	return vzgStreckenNummer;
    }

	public void setVzgStreckenNummer(String vzgStreckenNummer) {
    	this.vzgStreckenNummer = vzgStreckenNummer;
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
}
