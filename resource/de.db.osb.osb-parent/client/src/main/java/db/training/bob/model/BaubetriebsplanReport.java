package db.training.bob.model;

import static db.training.bob.util.StringUtils.coalesc;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import db.training.bob.util.StringUtils;
import db.training.logwrapper.Logger;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.babett.StatusBbzr;

/**
 * Reporting model fuer die Baubetriebsplanuebersicht
 * 
 * @author Sebastian Hennebrueder Date: 24.02.2010
 */
public class BaubetriebsplanReport implements Serializable {

	private static final long serialVersionUID = 1242249387251013311L;

	private List<MassnahmeReport> massnahmeReportList = new ArrayList<MassnahmeReport>();

	private Date start;

	private Date end;

	private TaskSeriesCollection dataLegend;

	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yy,HH:mm");

	public BaubetriebsplanReport(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public List<MassnahmeReport> getMassnahmeReportList() {
		return massnahmeReportList;
	}

	public void build(List<SAPMassnahme> sapMassnahmeList) {
		for (SAPMassnahme massnahme : sapMassnahmeList) {
			massnahmeReportList.add(new MassnahmeReport(massnahme));
		}

		dataLegend = new TaskSeriesCollection();
		TaskSeries taskSeries = new TaskSeries("Datum");
		dataLegend.add(taskSeries);

	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public static class MassnahmeReport implements Serializable {

		private static final long serialVersionUID = 4919617519661482661L;

		Logger logger = Logger.getLogger(BaubetriebsplanReport.class);

		private StatusBbzr statusBbzr;

		private boolean schichtweise;

		private boolean durchgehend;

		private String phase;

		private List<RegelungReport> regelungReportList = new ArrayList<RegelungReport>();

		// Baubeschraenkte Einschraenkung der Infrastruktur
		private boolean ei;

		// Lisba
		private boolean lisba;

		private String betraNummer;

		private String fploKennzeichen = null;

		private String bildlicheUebersicht = null;

		/**
		 * Kuerzel aus {@link db.training.osb.model.babett.FolgeNichtausfuehrung}
		 */
		private String folgeNichtausfuehrung;

		/**
		 * Geschwindigkeit, wenn eine Folgeausfuehrung eine Langsamfahrstelle ist, aus
		 * {@link db.training.osb.model.SAPMassnahme}
		 */
		private Integer folgeNichtausfuehrungGeschwindigkeit;

		/**
		 * Name der Grossmaschine aus {@link db.training.osb.model.babett.Grossmaschine}
		 */
		private String grossmaschine;

		/**
		 * Ist wahr, wenn das Feld {@code genehmigungsanforderung} in
		 * {@link db.training.osb.model.SAPMassnahme} wahr ist, aber das Feld {@code
		 * genehmigungsDatum} null ist
		 */
		private boolean genehmigungOffen;

		/**
		 * Aussteller der Genehmigung, Ermittelt aus dem Attribut {@code genehmiger bzws.
		 * genehmigerRegion} in {@link db.training.osb.model.SAPMassnahme}
		 */
		private GenehmigungsAussteller genehmigungsAussteller;

		/**
		 * Formatierte id der Massnahme
		 */
		private String massnahmeId;

		/**
		 * Lü finanzhinweis der Massnahme
		 */
		private boolean lueHinweis;

		/**
		 * La Eintrag der Massnahme
		 */
		private boolean laEintrag;

		private String finanzTyp;

		/**
		 * Nummer der vzg Strecke der Massnahme
		 */
		private Integer vzgNummer;

		/**
		 * richtungskennzahl aus der Massnahme
		 */
		private Integer richtungsKennzahl;

		/**
		 * Von / In Bestriebsstelle der Massnahme
		 */
		private String betriebsstelleVon;

		/**
		 * Bis Betriebsstelle der Massnahme
		 */
		private String betriebsstelleBis;

		/**
		 * Nummer aus TopProjekt der Massnahme
		 */
		private String sapProjektnummer;

		private static final String NBSP = "&nbsp;";

		public MassnahmeReport(SAPMassnahme sapMassnahme) {
			if (sapMassnahme.getPhase() != null)
				this.phase = sapMassnahme.getPhase().getName();
			// TODO richtiges Feld fuer ei und lisba
			this.ei = sapMassnahme.getBbEi() == Boolean.TRUE;
			this.lisba = true;

			this.betraNummer = sapMassnahme.getBetraNr();
			// TODO fix me
			fploKennzeichen = "abc";
			bildlicheUebersicht = null;

			if (sapMassnahme.getFolgeNichtausfuehrung() != null) {
				this.folgeNichtausfuehrung = sapMassnahme.getFolgeNichtausfuehrung().getKuerzel();
				this.folgeNichtausfuehrungGeschwindigkeit = sapMassnahme
				    .getFolgeNichtausfuehrungLaGeschwindigkeit();
			}

			if (sapMassnahme.getGrossmaschine() != null)
				this.grossmaschine = sapMassnahme.getGrossmaschine().getName();

			genehmigungOffen = sapMassnahme.getGenehmigungsanforderung() == Boolean.TRUE
			    && sapMassnahme.getGenehmigungsDatum() == null;
			// TODO festlegen
			genehmigungsAussteller = GenehmigungsAussteller.REGION;
			statusBbzr = sapMassnahme.getStatusBbzr();
			schichtweise = (sapMassnahme.getSchichtweise() != null && sapMassnahme
			    .getSchichtweise());
			durchgehend = (sapMassnahme.getDurchgehend() != null && sapMassnahme.getDurchgehend());

			regelungReportList.add(new RegelungReport(sapMassnahme.getGleissperrungen(),
			    "Gleissperrung"));
			regelungReportList.add(new RegelungReport(sapMassnahme.getOberleitungen(),
			    "Oberleitung"));
			regelungReportList.add(new RegelungReport(sapMassnahme.getLangsamfahrstellen(),
			    "Langsamfahrstelle"));

			massnahmeId = sapMassnahme.getMassnahmeId();
			lueHinweis = Boolean.TRUE == sapMassnahme.getLueHinweis();
			laEintrag = Boolean.TRUE == sapMassnahme.getLaEintragR406();
			finanzTyp = sapMassnahme.getFinanztyp() == null ? "" : sapMassnahme.getFinanztyp()
			    .getKuerzel();
			vzgNummer = sapMassnahme.getHauptStrecke() != null ? sapMassnahme.getHauptStrecke()
			    .getNummer() : null;
			richtungsKennzahl = sapMassnahme.getRichtungsKennzahl();
			betriebsstelleVon = sapMassnahme.getBetriebsstelleVon() != null ? sapMassnahme
			    .getBetriebsstelleVon().getName() : null;
			betriebsstelleBis = sapMassnahme.getBetriebsstelleBis() != null ? sapMassnahme
			    .getBetriebsstelleBis().getName() : null;

			// TODO welches Projekt nehmen
			if (sapMassnahme.getTopProjekte() != null && sapMassnahme.getTopProjekte().size() > 0)
				sapProjektnummer = sapMassnahme.getTopProjekte().iterator().next()
				    .getSapProjektNummer();
			else
				sapProjektnummer = "";
			// TODO
			if (Boolean.TRUE == sapMassnahme.getSchichtweise()) {
				String begin = sapMassnahme.getBauterminStart() == null ? dateTimeFormat
				    .format(sapMassnahme.getBauterminStart()) : "";
				String end = sapMassnahme.getBauterminEnde() == null ? dateTimeFormat
				    .format(sapMassnahme.getBauterminEnde()) : "";
				// zeitraumBaumassnahme = begin + end + coalesc(sapMassnahme.getVtr().getVts())
			} else {

			}
		}

		public String getLabelA() {
			return String.format("%s(%s)", phase != null ? phase : "", StringUtils.join(lisba ? "L"
			    : null, ei ? "EI" : null));
		}

		/**
		 * Gibt ein * zurueck, wenn es eine Betra-Nummer gibt, sonst ein Leerzeichen
		 * 
		 * @return entweder * oder ' '
		 */
		public String getBetraNummer() {
			return betraNummer != null ? "*" : " ";
		}

		/**
		 * Gibt ein * zurueck, wenn es eine Fplo Kennzeichnung gibt, sonst ein Leerzeichen
		 * 
		 * @return entweder * oder ' '
		 */
		public String getFploKennzeichnung() {
			return fploKennzeichen != null ? "*" : " ";
		}

		/**
		 * Gibt ein * zurueck, wenn es eine bildliche Übersicht gibt, sonst ein Leerzeichen
		 * 
		 * @return entweder * oder ' '
		 */
		public String getBildlicheUebersicht() {
			return bildlicheUebersicht != null ? "*" : " ";
		}

		public String getLabelB() {
			return String.format("%s %s<br>%s", coalesc(folgeNichtausfuehrung),
			    coalesc(folgeNichtausfuehrungGeschwindigkeit), coalesc(grossmaschine));
		}

		public String getLabelC() {
			return String.format("%s%s",
			    genehmigungsAussteller == GenehmigungsAussteller.ZENTRALE ? "GZ" : "GR",
			    genehmigungOffen ? "!" : "");
		}

		public boolean isSchichtweise() {
			return schichtweise;
		}

		public boolean isDurchgehend() {
			return durchgehend;
		}

		public StatusBbzr getStatusBbzr() {
			return statusBbzr;
		}

		public List<RegelungReport> getRegelungReportList() {
			return regelungReportList;
		}

		public String getMassnahmeId() {
			return massnahmeId;
		}

		public String getFinanzInfo() {
			String info = String.format("%2s %2s %6s", lueHinweis ? "Lü" : "", laEintrag ? "La"
			    : "", finanzTyp);

			return info.replace(" ", NBSP);
		}

		public String getStrecke() {
			String strecke;
			if (betriebsstelleVon == null || betriebsstelleVon.equals(betriebsstelleBis))
				strecke = String.format("%4s.%1s %s", coalesc(vzgNummer),
				    coalesc(richtungsKennzahl), coalesc(betriebsstelleVon));
			else
				strecke = String.format("%4s.%1s %s - %s", coalesc(vzgNummer),
				    coalesc(richtungsKennzahl), coalesc(betriebsstelleVon),
				    coalesc(betriebsstelleBis));
			if (strecke.length() > "0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS".length())
				strecke = strecke.substring(0, "0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS".length());
			return strecke.replace(" ", NBSP);
		}
	}

	public static class RegelungReport implements Serializable {

		private static final long serialVersionUID = -2049846550789104961L;

		final Logger logger = Logger.getLogger(RegelungReport.class);

		private TaskSeriesCollection reportData = new TaskSeriesCollection();

		private Color color;

		private boolean betroffeneSpnv;

		private boolean betroffeneSpfv;

		private boolean betroffeneSgv;

		private boolean schichtweise;

		private boolean durchgehend;

		private Integer vzgNummer;

		/**
		 * Betriebsstelle von / in einer Regelung
		 */
		private String betriebsstelleVon;

		/**
		 * Betriebsstelle bis einer Regelung
		 */
		private String betriebsstelleBis;

		/**
		 * Richtungskennzeichen einer Regelung
		 */
		private Integer richtungsKennzahl;

		public RegelungReport(Set<?> regelungsList, String title) {

			TaskSeries taskSeries = new TaskSeries(title);

			reportData.add(taskSeries);

			color = Color.GRAY;
			if (regelungsList == null || regelungsList.isEmpty())
				return;

			final Regelung firstEntry = (Regelung) regelungsList.iterator().next();

			schichtweise = firstEntry.getSchichtweise() == Boolean.TRUE;
			durchgehend = firstEntry.getDurchgehend() == Boolean.TRUE;

			if (firstEntry instanceof Gleissperrung)
				color = Color.YELLOW;
			else if (firstEntry instanceof Oberleitung)
				color = Color.BLUE;
			else if (firstEntry instanceof Langsamfahrstelle)
				color = Color.green;

			Date first = null, last = null;

			for (Object entryObject : regelungsList) {
				Regelung entry = (Regelung) entryObject;
				vzgNummer = entry.getVzgStrecke() != null ? entry.getVzgStrecke().getNummer()
				    : null;
				richtungsKennzahl = 1;
				betriebsstelleVon = entry.getBstVon() != null ? entry.getBstVon().getName() : null;
				betriebsstelleBis = entry.getBstBis() != null ? entry.getBstBis().getName() : null;

				betroffeneSpfv = Boolean.TRUE == entry.getBetroffenSpfv();
				betroffeneSpnv = Boolean.TRUE == entry.getBetroffenSpnv();
				betroffeneSgv = Boolean.TRUE == entry.getBetroffenSgv();

				if (first == null || first.after(entry.getZeitVon()))
					first = entry.getZeitVon();
				if (last == null || last.before(entry.getZeitBis()))
					last = entry.getZeitBis();
			}
			// TODO Wie stellen wir Regelungen ohne Zeit da
			if (first != null && last != null) {
				if (first.after(last)) {
					Date temp = last;
					last = first;
					first = temp;
					logger.warn("Startzeit ", first, " vor endzeit ", last);
				}

				logger.debug("Create task ", title, ", ", first, ", ", last);
				Task task = new Task(title, first, last);
				for (Object entryObject : regelungsList) {
					Regelung entry = (Regelung) entryObject;
					task.addSubtask(new Task(title, entry.getZeitVon(), entry.getZeitBis()));
				}
				taskSeries.add(task);
			} else
				logger.warn("gleispsperrung ohne Datum");
		}

		/**
		 * True, wenn Arbeiten der OSB Regelung schichtweise erfolgt
		 * 
		 * @return
		 */
		public boolean isSchicht() {
			return schichtweise;
		}

		/**
		 * True, wenn Arbeiten der OSB Regelung unterbrochen werden
		 * 
		 * @return
		 */
		public boolean isDurchgehend() {
			return durchgehend;
		}

		/**
		 * Gibt Kennzeichen zurueck, wenn SPNV, SPFV oder SPGV betroffen ist
		 * 
		 * @return einen String, der die entsprechenden Zeichen enthält
		 */
		public String getBetroffen() {
			return StringUtils.join(betroffeneSpfv ? "F" : null, betroffeneSpnv ? "N" : null,
			    betroffeneSgv ? "G" : null);
		}

		public TaskSeriesCollection getTaskSeries() {
			return reportData;
		}

		public Paint getColor() {
			return color;
		}

		public String getStrecke() {
			String strecke;
			if (betriebsstelleVon == null || betriebsstelleVon.equals(betriebsstelleBis))
				strecke = String.format("%4s.%1s %s", coalesc(vzgNummer),
				    coalesc(richtungsKennzahl), coalesc(betriebsstelleVon));
			else
				strecke = String.format("%4s.%1s %s - %s", coalesc(vzgNummer),
				    coalesc(richtungsKennzahl), coalesc(betriebsstelleVon),
				    coalesc(betriebsstelleBis));
			if (strecke.length() > "0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS".length())
				strecke = strecke.substring(0, "0000.0SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS".length());
			return strecke.replace(" ", "&nbsp;");
		}

	}

	enum GenehmigungsAussteller {
		ZENTRALE, REGION
	}
}
