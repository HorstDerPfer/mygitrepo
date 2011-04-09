package db.training.osb.web.gleissperrung;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseValidatorForm;

@SuppressWarnings("serial")
public class GleissperrungMassnahmeLinkForm extends BaseValidatorForm {

	private Integer gleissperrungId;

	private Integer massnahmeId;

	private Integer[] gleissperrungLinkIds;

	private Integer[] massnahmenLinkIds;

	public GleissperrungMassnahmeLinkForm() {
		reset();
	}

	public void reset() {
		gleissperrungId = null;
		massnahmeId = null;
		gleissperrungLinkIds = null;
		massnahmenLinkIds = null;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		resetProperty(request, this, "gleissperrungLinkIds", null);
		resetProperty(request, this, "massnahmenLinkIds", null);
	}

	/**
	 * @return the gleissperrungId
	 */
	public Integer getGleissperrungId() {
		return gleissperrungId;
	}

	/**
	 * @param gleissperrungId
	 *            the gleissperrungId to set
	 */
	public void setGleissperrungId(Integer gleissperrungId) {
		this.gleissperrungId = gleissperrungId;
	}

	/**
	 * @return the massnahmeId
	 */
	public Integer getMassnahmeId() {
		return massnahmeId;
	}

	/**
	 * @param massnahmeId
	 *            the massnahmeId to set
	 */
	public void setMassnahmeId(Integer massnahmeId) {
		this.massnahmeId = massnahmeId;
	}

	/**
	 * @return the gleissperrungLinkIds
	 */
	public Integer[] getGleissperrungLinkIds() {
		return gleissperrungLinkIds;
	}

	/**
	 * @param gleissperrungLinkIds
	 *            the gleissperrungLinkIds to set
	 */
	public void setGleissperrungLinkIds(Integer[] gleissperrungLinkIds) {
		this.gleissperrungLinkIds = gleissperrungLinkIds;
	}

	/**
	 * @return the massnahmenLinkIds
	 */
	public Integer[] getMassnahmenLinkIds() {
		return massnahmenLinkIds;
	}

	/**
	 * @param massnahmenLinkIds
	 *            the massnahmenLinkIds to set
	 */
	public void setMassnahmenLinkIds(Integer[] massnahmenLinkIds) {
		this.massnahmenLinkIds = massnahmenLinkIds;
	}

}
