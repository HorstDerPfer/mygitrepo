package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class UmleitungenToFahrplanregelungForm extends ValidatorForm {

	private Integer fahrplanregelungId;

	private Integer umleitungId;

	private Integer anzahlSGV;

	private Integer anzahlSPNV;

	private Integer anzahlSPFV;

	private Integer anzahlSGVGegenRich;

	private Integer anzahlSPNVGegenRich;

	private Integer anzahlSPFVGegenRich;

	public UmleitungenToFahrplanregelungForm() {
		reset();
	}

	public void reset() {
		fahrplanregelungId = null;
		anzahlSGV = null;
		anzahlSPNV = null;
		anzahlSPFV = null;

		anzahlSGVGegenRich = null;
		anzahlSPNVGegenRich = null;
		anzahlSPFVGegenRich = null;
		umleitungId = null;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		return errors;
	}

	public Integer getFahrplanregelungId() {
		return fahrplanregelungId;
	}

	public void setFahrplanregelungId(Integer fahrplanregelungId) {
		this.fahrplanregelungId = fahrplanregelungId;
	}

	public Integer getUmleitungId() {
		return umleitungId;
	}

	public void setUmleitungId(Integer umleitungId) {
		this.umleitungId = umleitungId;
	}

	public Integer getAnzahlSGV() {
		return anzahlSGV;
	}

	public void setAnzahlSGV(Integer anzahlSGV) {
		this.anzahlSGV = anzahlSGV;
	}

	public Integer getAnzahlSPNV() {
		return anzahlSPNV;
	}

	public void setAnzahlSPNV(Integer anzahlSPNV) {
		this.anzahlSPNV = anzahlSPNV;
	}

	public Integer getAnzahlSPFV() {
		return anzahlSPFV;
	}

	public void setAnzahlSPFV(Integer anzahlSPFV) {
		this.anzahlSPFV = anzahlSPFV;
	}

	public Integer getAnzahlSGVGegenRich() {
		return anzahlSGVGegenRich;
	}

	public void setAnzahlSGVGegenRich(Integer anzahlSGVGegenRich) {
		this.anzahlSGVGegenRich = anzahlSGVGegenRich;
	}

	public Integer getAnzahlSPNVGegenRich() {
		return anzahlSPNVGegenRich;
	}

	public void setAnzahlSPNVGegenRich(Integer anzahlSPNVGegenRich) {
		this.anzahlSPNVGegenRich = anzahlSPNVGegenRich;
	}

	public Integer getAnzahlSPFVGegenRich() {
		return anzahlSPFVGegenRich;
	}

	public void setAnzahlSPFVGegenRich(Integer anzahlSPFVGegenRich) {
		this.anzahlSPFVGegenRich = anzahlSPFVGegenRich;
	}

}
