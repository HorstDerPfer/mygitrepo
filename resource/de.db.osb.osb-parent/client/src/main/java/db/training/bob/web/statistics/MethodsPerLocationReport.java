package db.training.bob.web.statistics;

import java.util.HashMap;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;

/**
 * 
 * @author michels
 * 
 */
public class MethodsPerLocationReport {

	private HashMap<Integer, Integer> report = null;

	public void init() {
		report = new HashMap<Integer, Integer>();
		RegionalbereichService rbService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();

		for (Regionalbereich regionalbereich : rbService.findAll()) {
			// die ID des Regionalbereichs ist Schlüssel in HashMap
			if (!report.containsKey(regionalbereich.getId())) {
				// Regionalbereich ist nicht enhalten
				report.put(regionalbereich.getId(), 0);
			}
		}
	}

	public HashMap<Integer, Integer> getReport() {
		if (report == null)
			init();
		return report;
	}
}
