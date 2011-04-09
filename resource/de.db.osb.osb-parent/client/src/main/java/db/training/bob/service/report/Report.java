package db.training.bob.service.report;

import java.util.ArrayList;
import java.util.List;

import db.training.bob.web.statistics.zvf.SimpleReportBean;

public class Report {
	private List<SimpleReportBean> resultEVU = new ArrayList<SimpleReportBean>();

	private List<SimpleReportBean> resultGesamt = new ArrayList<SimpleReportBean>();

	public List<SimpleReportBean> getResultEVU() {
		return resultEVU;
	}

	public List<SimpleReportBean> getResultGesamt() {
		return resultGesamt;
	}
}

