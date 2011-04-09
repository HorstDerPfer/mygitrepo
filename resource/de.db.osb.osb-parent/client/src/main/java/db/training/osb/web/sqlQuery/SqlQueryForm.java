package db.training.osb.web.sqlQuery;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class SqlQueryForm extends ValidatorForm {

	private Integer sqlQueryId;

	private String name;

	private String beschreibung;

	private String query;

	private String gueltigVon;

	private String gueltigBis;

	private String modul;

	private String cluster;

	public void reset() {
		sqlQueryId = null;
		name = null;
		beschreibung = null;
		query = null;
		gueltigVon = null;
		gueltigBis = null;
		modul = null;
		cluster = null;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		String sqlQuery = query.toUpperCase();
		if (sqlQuery.contains("INSERT") || sqlQuery.contains("UPDATE")
		    || sqlQuery.contains("DELETE") || sqlQuery.contains("CREATE")
		    || sqlQuery.contains("DROP") || sqlQuery.contains("TRUNCATE")
		    || sqlQuery.contains("GRANT") || sqlQuery.contains("ALTER")
		    || sqlQuery.contains("LOCK"))
			errors.add("query", new ActionMessage("error.sqlQuery.query.forbidden"));
		return errors;
	}

	public Integer getSqlQueryId() {
		return sqlQueryId;
	}

	public void setSqlQueryId(Integer sqlQueryId) {
		this.sqlQueryId = sqlQueryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getGueltigVon() {
		return gueltigVon;
	}

	public void setGueltigVon(String gueltigVon) {
		this.gueltigVon = gueltigVon;
	}

	public String getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(String gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	public String getModul() {
		return modul;
	}

	public void setModul(String modul) {
		this.modul = modul;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

}
