package db.training.osb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import db.training.bob.model.BaubetriebsplanReport;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.Finanztyp;
import db.training.osb.model.babett.StatusBbzr;

/**
 * @author Sebastian Hennebrueder Date: 24.02.2010
 */
public class BaubetriebsplanServiceImpl implements BaubetriebsplanService {

	@SuppressWarnings("deprecation")
	public BaubetriebsplanReport createReportData(Integer massnahmeId, Date start, Date end) {

		// Create Demo Daten

		List<SAPMassnahme> sapMassnahmeList = new ArrayList<SAPMassnahme>();
		SAPMassnahme m1 = new SAPMassnahme();
		m1.setLueHinweis(true);
		m1.setFinanztyp(new Finanztyp("Invest", "Investition"));
		m1.setStatusBbzr(StatusBbzr.ERFORDERLICH);
		m1.setBetriebsstelleVon(new Betriebsstelle("Bln Rummelsbg", "Bln Rummelsbg", "xyz"));
		m1.setRichtungsKennzahl(1);
		m1.setHauptStrecke(new VzgStrecke(123));
		sapMassnahmeList.add(m1);
		Gleissperrung gleissperrung = new Gleissperrung(new Date(2010, 3, 10),
		    new Date(2010, 3, 12));
		gleissperrung.setBetroffenSgv(true);
		final List<Gleissperrung> list = Arrays.asList(gleissperrung);
		m1.setGleissperrungen(new HashSet<Gleissperrung>(list));

		SAPMassnahme m2 = new SAPMassnahme();
		m2.setStatusBbzr(StatusBbzr.ENTWURF_GELIEFERT);
		sapMassnahmeList.add(m2);
		m2.setGleissperrungen(new HashSet<Gleissperrung>(Arrays.asList(new Gleissperrung(new Date(
		    2010, 3, 10), new Date(2010, 3, 12)), new Gleissperrung(new Date(2010, 3, 16),
		    new Date(2010, 3, 19)))));
		m2.setLangsamfahrstellen(new HashSet<Langsamfahrstelle>(Arrays
		    .asList(new Langsamfahrstelle(new Date(2010, 3, 2), new Date(2010, 3, 5)))));
		m2.setOberleitungen(new HashSet<Oberleitung>(Arrays.asList(new Oberleitung(new Date(2010,
		    3, 10), new Date(2010, 3, 12)), new Oberleitung(new Date(2010, 3, 20), new Date(2010,
		    3, 22)))));

		BaubetriebsplanReport baubetriebsplanReport = new BaubetriebsplanReport(start, end);
		baubetriebsplanReport.build(sapMassnahmeList);
		return baubetriebsplanReport;
	}
}
