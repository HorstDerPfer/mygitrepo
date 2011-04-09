package db.training.web.administration;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class EditableMeilensteinListItemForm extends ValidatorForm {

	private Integer id;

	private Integer art;

	private Integer schnittstelle;

	private Integer meilensteinBezeichnung;

	private String updateWochenVorBaubeginn;

	private Integer updateWochentag;

	private Boolean updateMindestfrist;

	private String updateGueltigAb;

	private Integer insertArt;

	private Integer insertSchnittstelle;

	private Integer insertMeilensteinBezeichnung;

	private String insertWochenVorBaubeginn;

	private Integer insertWochentag;

	private Boolean insertMindestfrist;

	private String insertGueltigAb;

	public EditableMeilensteinListItemForm() {
		reset();
	}

	public void reset() {
		setId(null);
		setArt(null);
		setSchnittstelle(null);
		setMeilensteinBezeichnung(null);
		setUpdateWochenVorBaubeginn(null);
		setUpdateWochentag(null);
		setUpdateMindestfrist(null);
		setUpdateGueltigAb(null);
		setInsertArt(null);
		setInsertSchnittstelle(null);
		setInsertMeilensteinBezeichnung(null);
		setInsertWochenVorBaubeginn(null);
		setInsertWochentag(2);
		setInsertMindestfrist(false);
		setInsertGueltigAb(null);
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		String method = request.getParameter("method");
		MessageResources messageResources = MessageResources
		    .getMessageResources("MessageResources");

		if (FrontendHelper.stringNotNullOrEmpty(method)) {
			if (method.equalsIgnoreCase("insert")) {
				if (!FrontendHelper.stringNotNullOrEmpty(insertWochenVorBaubeginn)) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    messageResources.getMessage("error.wochenvorbaubeginn")));
				} else {
					Integer value = FrontendHelper.castStringToInteger(insertWochenVorBaubeginn);
					if (value == null)
						actionErrors.add("error.invalid", new ActionMessage("error.invalid",
						    messageResources.getMessage("error.wochenvorbaubeginn")));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(insertGueltigAb)) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    messageResources.getMessage("error.gueltigabbaubeginn")));
				} else {
					Date date = FrontendHelper.castStringToDate(insertGueltigAb);
					if (date == null)
						actionErrors.add("error.invalid", new ActionMessage("error.invalid",
						    messageResources.getMessage("error.gueltigabbaubeginn")));
				}
			}
			if (method.equalsIgnoreCase("update")) {
				if (!FrontendHelper.stringNotNullOrEmpty(updateWochenVorBaubeginn)) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    messageResources.getMessage("error.wochenvorbaubeginn")));
				} else {
					Integer value = FrontendHelper.castStringToInteger(updateWochenVorBaubeginn);
					if (value == null)
						actionErrors.add("error.invalid", new ActionMessage("error.invalid",
						    messageResources.getMessage("error.wochenvorbaubeginn")));
				}
				if (new String().equals(updateGueltigAb)) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    messageResources.getMessage("error.gueltigabbaubeginn")));
				} else if (updateGueltigAb != null) {
					Date date = FrontendHelper.castStringToDate(updateGueltigAb);
					if (date == null)
						actionErrors.add("error.invalid", new ActionMessage("error.invalid",
						    messageResources.getMessage("error.gueltigabbaubeginn")));
				}
			}
		}
		return actionErrors;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Integer getArt() {
		return art;
	}

	public void setArt(Integer art) {
		this.art = art;
	}

	public Integer getSchnittstelle() {
		return schnittstelle;
	}

	public void setSchnittstelle(Integer schnittstelle) {
		this.schnittstelle = schnittstelle;
	}

	public Integer getMeilensteinBezeichnung() {
		return meilensteinBezeichnung;
	}

	public void setMeilensteinBezeichnung(Integer meilensteinBezeichnung) {
		this.meilensteinBezeichnung = meilensteinBezeichnung;
	}

	public String getUpdateWochenVorBaubeginn() {
		return updateWochenVorBaubeginn;
	}

	public void setUpdateWochenVorBaubeginn(String updateWochenVorBaubeginn) {
		this.updateWochenVorBaubeginn = updateWochenVorBaubeginn;
	}

	public Integer getUpdateWochentag() {
		return updateWochentag;
	}

	public void setUpdateWochentag(Integer updateWochentag) {
		this.updateWochentag = updateWochentag;
	}

	public Boolean getUpdateMindestfrist() {
		return updateMindestfrist;
	}

	public void setUpdateMindestfrist(Boolean updateMindestfrist) {
		this.updateMindestfrist = updateMindestfrist;
	}

	public String getUpdateGueltigAb() {
		return updateGueltigAb;
	}

	public void setUpdateGueltigAb(String updateGueltigAb) {
		this.updateGueltigAb = updateGueltigAb;
	}

	public Integer getInsertArt() {
		return insertArt;
	}

	public void setInsertArt(Integer insertArt) {
		this.insertArt = insertArt;
	}

	public Integer getInsertSchnittstelle() {
		return insertSchnittstelle;
	}

	public void setInsertSchnittstelle(Integer insertSchnittstelle) {
		this.insertSchnittstelle = insertSchnittstelle;
	}

	public Integer getInsertMeilensteinBezeichnung() {
		return insertMeilensteinBezeichnung;
	}

	public void setInsertMeilensteinBezeichnung(Integer insertMeilensteinBezeichnung) {
		this.insertMeilensteinBezeichnung = insertMeilensteinBezeichnung;
	}

	public String getInsertWochenVorBaubeginn() {
		return insertWochenVorBaubeginn;
	}

	public void setInsertWochenVorBaubeginn(String insertWochenVorBaubeginn) {
		this.insertWochenVorBaubeginn = insertWochenVorBaubeginn;
	}

	public Integer getInsertWochentag() {
		return insertWochentag;
	}

	public void setInsertWochentag(Integer insertWochentag) {
		this.insertWochentag = insertWochentag;
	}

	public Boolean getInsertMindestfrist() {
		return insertMindestfrist;
	}

	public void setInsertMindestfrist(Boolean insertMindestfrist) {
		this.insertMindestfrist = insertMindestfrist;
	}

	public String getInsertGueltigAb() {
		return insertGueltigAb;
	}

	public void setInsertGueltigAb(String insertGueltigAb) {
		this.insertGueltigAb = insertGueltigAb;
	}

}
