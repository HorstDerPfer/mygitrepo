package db.training.bob.web.baumassnahme;

import java.io.InputStream;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.zvf.Uebergabeblatt;

public class XmlImportForm extends ValidatorForm {

	private static final long serialVersionUID = -7450836283816588366L;

	private FormFile xmlFile;

	private Set<BBPMassnahme> bbpMassnahmen;

	private String fileContent;

	private Integer baumassnahmeId;

	private String type;

	private Integer zvfId;

	private Uebergabeblatt zvfFile;

	private static final JAXBContext context = initContext();

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		xmlFile = null;
		baumassnahmeId = null;
		zvfId = null;
		bbpMassnahmen = null;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		if (bbpMassnahmen == null) { // ZVF-Import
			Uebergabeblatt zvfFile = null;
			try {
				Unmarshaller u = context.createUnmarshaller();
				InputStream input = this.getXmlFile().getInputStream();
				zvfFile = (Uebergabeblatt) u.unmarshal(input);
			} catch (Exception e) {
				actionErrors.add("error.import.invalid.file", new ActionMessage(
				    "error.import.invalid.file"));
			}

			try {
				String fileType = zvfFile.getMassnahmen().iterator().next().getVersion()
				    .getFormular().toString();

				if (type.equals("UEB") & !fileType.startsWith("ÜB"))
					actionErrors.add("error.import.invalid.kennung", new ActionMessage(
					    "error.import.invalid.kennung"));
				if (type.equals("ZVF") & !fileType.startsWith("ZVF"))
					actionErrors.add("error.import.invalid.kennung", new ActionMessage(
					    "error.import.invalid.kennung"));
			} catch (NullPointerException e) {
				actionErrors.add("error.import.invalid.file", new ActionMessage(
				    "error.import.invalid.file"));
			}
		}

		return actionErrors;
	}

	public Set<BBPMassnahme> getBbpMassnahmen() {
		return bbpMassnahmen;
	}

	public void setBbpMassnahmen(Set<BBPMassnahme> bbpMassnahmen) {
		this.bbpMassnahmen = bbpMassnahmen;
	}

	public FormFile getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(FormFile xmlFile) {
		this.xmlFile = xmlFile;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public Integer getBaumassnahmeId() {
		return baumassnahmeId;
	}

	public void setBaumassnahmeId(Integer baumassnahmeId) {
		this.baumassnahmeId = baumassnahmeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getZvfId() {
		return zvfId;
	}

	public void setZvfId(Integer zvfId) {
		this.zvfId = zvfId;
	}

	public Uebergabeblatt getZvfFile() {
		return zvfFile;
	}

	public void setZvfFile(Uebergabeblatt zvfFile) {
		this.zvfFile = zvfFile;
	}

	private static JAXBContext initContext() {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance("db.training.bob.model.zvf");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return jaxbContext;
	}

}
