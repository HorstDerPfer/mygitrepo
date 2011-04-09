package db.training.bob.web.baumassnahme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class KnotenzeitenForm extends ValidatorForm {

	private Integer id;

	private List<Integer> zugIds;

	private boolean error = false;

	private Integer baumassnahmeId;

	private String ersterVerkehrstag = null;

	private String letzterVerkehrstag = null;

	private Map<String, String> bahnhof = new HashMap<String, String>();

	private Map<String, String> haltart = new HashMap<String, String>();

	private Map<String, String> an = new HashMap<String, String>();

	private Map<String, String> ab = new HashMap<String, String>();

	private Map<String, String> relativlage = new HashMap<String, String>();

	private Boolean add;

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		ActionMessage message = testTimeFormat(an.values(), "An");
		if (message != null)
			actionErrors.add("error.invalid", message);

		message = testTimeFormat(ab.values(), "Ab");
		if (message != null)
			actionErrors.add("error.invalid", message);

		for (String h : haltart.values()) {
			if (h != null) {
				if (h.length() > 1
				    | (!h.equals("B") & !h.equals("H") & !h.equals("X") & !h.equals("+") & !h
				        .equals("")))
					actionErrors
					    .add("error.invalid", new ActionMessage("error.invalid", "Haltart"));

			}
		}

		for (String r : relativlage.values()) {
			if (r != null && r.length() > 0) {
				Integer i = FrontendHelper.castStringToInteger(r);
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Relativlage"));
			}
		}
		error = true; // damit editaction feststellen kann, dass save fehlgeschlagen
		return actionErrors;

	}

	private ActionMessage testTimeFormat(Collection<String> times, String messagePart) {
		Iterator<String> it = times.iterator();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		while (it.hasNext()) {
			String time = it.next();
			if (time != null & !time.equals("")) {
				try {
					df.parse(time);
				} catch (ParseException e) {
					return new ActionMessage("error.invalid", messagePart);
				}
			}
		}
		return null;
	}

	public void reset() {
		bahnhof = new HashMap<String, String>();
		haltart = new HashMap<String, String>();
		an = new HashMap<String, String>();
		ab = new HashMap<String, String>();
		relativlage = new HashMap<String, String>();
	}

	@Override
	public void reset(ActionMapping mapping, ServletRequest request) {
		super.reset(mapping, request);
		bahnhof.clear();
		haltart.clear();
		an.clear();
		ab.clear();
		relativlage.clear();
		setId(null);
		setBaumassnahmeId(null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getZugIds() {
		return zugIds;
	}

	public void setZugIds(List<Integer> zugIds) {
		this.zugIds = zugIds;
	}

	public String getErsterVerkehrstag() {
		return ersterVerkehrstag;
	}

	public void setErsterVerkehrstag(String ersterVerkehrstag) {
		this.ersterVerkehrstag = ersterVerkehrstag;
	}

	public String getLetzterVerkehrstag() {
		return letzterVerkehrstag;
	}

	public void setLetzterVerkehrstag(String letzterVerkehrstag) {
		this.letzterVerkehrstag = letzterVerkehrstag;
	}

	public Integer getBaumassnahmeId() {
		return baumassnahmeId;
	}

	public void setBaumassnahmeId(Integer baumassnahmeId) {
		this.baumassnahmeId = baumassnahmeId;
	}

	public String getBahnhof(String i) {
		return bahnhof.get(i);
	}

	public void setBahnhof(String i, String bahnhof) {
		this.bahnhof.put(i, bahnhof);
	}

	public String getHaltart(String i) {
		return haltart.get(i);
	}

	public void setHaltart(String i, String haltart) {
		this.haltart.put(i, haltart);
	}

	public String getAn(String i) {
		return an.get(i);
	}

	public void setAn(String i, String an) {
		this.an.put(i, an);
	}

	public String getAb(String i) {
		return ab.get(i);
	}

	public void setAb(String i, String ab) {
		this.ab.put(i, ab);
	}

	public String getRelativlage(String i) {
		return relativlage.get(i);
	}

	public void setRelativlage(String i, String relativlage) {
		this.relativlage.put(i, relativlage);
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
