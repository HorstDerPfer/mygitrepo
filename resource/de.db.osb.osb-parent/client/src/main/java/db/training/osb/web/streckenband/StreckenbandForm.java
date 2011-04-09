/**
 * 
 */
package db.training.osb.web.streckenband;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseValidatorForm;

/**
 * Formular für das Streckenband
 * 
 * @author michels
 * 
 */
@SuppressWarnings("serial")
public class StreckenbandForm extends BaseValidatorForm {

	private Integer[] gleissperrungenIds;

	private Integer gleissperrungId;

	private Integer buendelId;

	private Integer vzgStreckeId;

	private Integer moveRowId;

	private String[] gewerke = null;

	public StreckenbandForm() {
		super();
		reset();
	}

	public void reset() {
		gleissperrungenIds = null;
		gleissperrungId = null;
		buendelId = null;
		vzgStreckeId = null;
		moveRowId = null;
		gewerke = null;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// resetProperty(arg1, this, "gleissperrungenIds", null);
		// resetProperty(arg1, this, "gewerke", null);
	}

	public Integer[] getGleissperrungenIds() {
		return gleissperrungenIds;
	}

	public void setGleissperrungenIds(Integer[] gleissperrungenIds) {
		this.gleissperrungenIds = gleissperrungenIds;
	}

	public Integer getGleissperrungId() {
		return gleissperrungId;
	}

	public void setGleissperrungId(Integer gleissperrungId) {
		this.gleissperrungId = gleissperrungId;
	}

	public Integer getBuendelId() {
		return buendelId;
	}

	public void setBuendelId(Integer buendelId) {
		this.buendelId = buendelId;
	}

	public Integer getVzgStreckeId() {
		return vzgStreckeId;
	}

	public void setVzgStreckeId(Integer vzgStreckeId) {
		this.vzgStreckeId = vzgStreckeId;
	}

	public Integer getMoveRowId() {
		return moveRowId;
	}

	public void setMoveRowId(Integer moveRowId) {
		this.moveRowId = moveRowId;
	}

	public String[] getGewerke() {
		return gewerke;
	}

	public void setGewerke(String[] gewerke) {
		this.gewerke = gewerke;
	}
}
