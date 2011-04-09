package db.training.osb.service;

import db.training.bob.model.BaubetriebsplanReport;

import java.util.Date;

/**
 * @author Sebastian Hennebrueder
 *         Date: 24.02.2010
 */
public interface BaubetriebsplanService {
	/**
	 * Lädt die Daten fuer einen Baubetriebsplanreport für eine Baumassnahme
	 * @param massnahmeId
	 * @return
	 */
	BaubetriebsplanReport createReportData(Integer massnahmeId, Date start, Date end);
}
