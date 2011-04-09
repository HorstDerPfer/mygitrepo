package db.training.osb.web.imports;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class ImportDataForm extends ValidatorForm {

	private FormFile file;

	private String importTable;

	public void reset() {
		file = null;
		importTable = null;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getImportTable() {
		return importTable;
	}

	public void setImportTable(String importTable) {
		this.importTable = importTable;
	}
}