package db.training.osb.web.fahrplanregelung;

import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseValidatorForm;

public class FahrplanregelungGleissperrungFilterForm extends BaseValidatorForm {

	private static final long serialVersionUID = 4319825681930357602L;

	public Integer fahrplanregelungId;
	
	public Integer[] gleissperrungenIds;

	public final static int DEFAULTPAGESIZE = PaginatedList.LIST_OBJECTS_PER_PAGE;

	private Integer pageSize;

	private String betriebsstelleVon;

	private String betriebsstelleBis;

	private Integer regionalbereichId;

	private String streckeVZG;
	
	public FahrplanregelungGleissperrungFilterForm() {
		reset();
	}

	public void reset() {
		fahrplanregelungId = null;
		pageSize = null;
		betriebsstelleVon = null;
		betriebsstelleBis = null;
		regionalbereichId = null;
		streckeVZG = null;
	}

	public Integer getFahrplanregelungId() {
		return fahrplanregelungId;
	}

	public void setFahrplanregelungId(Integer fahrplanregelungId) {
		this.fahrplanregelungId = fahrplanregelungId;
	}
	
	public Integer[] getGleissperrungenIds() {
		return gleissperrungenIds;
	}

	public void setGleissperrungenIds(Integer[] gleissperrungId) {
		this.gleissperrungenIds = gleissperrungId;
	}
	
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
}
