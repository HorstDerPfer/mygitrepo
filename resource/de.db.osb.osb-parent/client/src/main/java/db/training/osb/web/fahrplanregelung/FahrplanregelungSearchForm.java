package db.training.osb.web.fahrplanregelung;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class FahrplanregelungSearchForm extends ValidatorForm {

	public final static int DEFAULTPAGESIZE = 20;

	private Integer buendelId;

	private Integer fahrplanregelungId;

	private String name;

	private String nummer;

	private Integer regionalbereichId;

	private Integer pageSize = DEFAULTPAGESIZE;

	private List<Integer> ids;
	
	private String datumVon;

	private String datumBis;

	public void reset() {
		pageSize = DEFAULTPAGESIZE;
		buendelId = null;
		fahrplanregelungId = null;
		name = null;
		nummer = null;
		regionalbereichId = null;
		datumVon = null;
		datumBis = null;
		setIds(new ArrayList<Integer>());
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		return errors;
	}

	public Integer getBuendelId() {
		return buendelId;
	}

	public void setBuendelId(Integer buendelId) {
		this.buendelId = buendelId;
	}

	public Integer getFahrplanregelungId() {
		return fahrplanregelungId;
	}

	public void setFahrplanregelungId(Integer fahrplanregelungId) {
		this.fahrplanregelungId = fahrplanregelungId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public static int getDefaultpagesize() {
		return DEFAULTPAGESIZE;
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
