package db.training.bob.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.util.MessageResources;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.StatusType;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.easy.util.FrontendHelper;
import db.training.osb.util.ConfigResources;

public class ExcelExporter {

	private MessageResources msgRes;

	private HSSFCellStyle headerStyle;

	private HSSFCellStyle dateStyle;

	private HSSFCellStyle dateStyleBorder;

	private HSSFCellStyle defaultStyleBorder;

	public ExcelExporter() {
		msgRes = MessageResources.getMessageResources("MessageResources");
	}

	public HSSFWorkbook getXls(List<Baumassnahme> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		configureWorkbook(workbook);
		buildReportSheet(workbook, list);

		return workbook;
	}

	private void applyDocumentFooter(HSSFSheet sheet) {
		if (sheet == null)
			return;
		HSSFFooter footer = sheet.getFooter();
		footer.setCenter(msgRes.getMessage("print.createdwith") + ": "
		    + ConfigResources.getInstance().getApplicationTitle() + "\n"
		    + msgRes.getMessage("print.createdon") + ": "
		    + FrontendHelper.castDateToString(new Date(), "dd.MM.yyyy HH:mm"));
		footer.setRight(msgRes.getMessage("print.page") + " " + HeaderFooter.page() + "/"
		    + HeaderFooter.numPages());
	}

	/**
	 * stellt die Druckausgabe Querformat, DIN A4 ein.
	 * 
	 * @param sheet
	 * @return
	 */
	private HSSFPrintSetup applyPrintSetup(HSSFSheet sheet) {
		if (sheet == null)
			return null;
		HSSFPrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		return ps;
	}

	private HSSFSheet buildReportSheet(HSSFWorkbook workbook, List<Baumassnahme> list) {
		HSSFSheet sheet = workbook.createSheet("Bericht");

		applyDocumentFooter(sheet);
		applyPrintSetup(sheet);
		writeColumnHeader(sheet);
		writeBaumassnahmen(sheet, list);
		formatCells(sheet);
		return sheet;
	}

	@SuppressWarnings("unchecked")
	private void formatCells(HSSFSheet sheet) {
		final int FACTOR = 267;

		// Stammdaten
		sheet.setColumnWidth(0, 11 * FACTOR);
		sheet.setColumnWidth(1, 11 * FACTOR);
		sheet.setColumnWidth(2, 11 * FACTOR);
		sheet.setColumnWidth(3, 11 * FACTOR);
		sheet.setColumnWidth(4, 11 * FACTOR);
		sheet.setColumnWidth(5, 40 * FACTOR);
		sheet.setColumnWidth(6, 25 * FACTOR);
		sheet.setColumnWidth(7, 11 * FACTOR);
		sheet.setColumnWidth(8, 11 * FACTOR);
		sheet.setColumnWidth(9, 30 * FACTOR);
		sheet.setColumnWidth(10, 30 * FACTOR);
		sheet.setColumnWidth(11, 20 * FACTOR);
		sheet.setColumnWidth(12, 20 * FACTOR);
		sheet.setColumnWidth(13, 11 * FACTOR);
		sheet.setColumnWidth(14, 11 * FACTOR);
		sheet.setColumnWidth(15, 11 * FACTOR);
		sheet.setColumnWidth(16, 20 * FACTOR);
		sheet.setColumnWidth(17, 11 * FACTOR);
		sheet.setColumnWidth(18, 11 * FACTOR);
		sheet.setColumnWidth(19, 20 * FACTOR);
		sheet.setColumnWidth(20, 20 * FACTOR);
		sheet.setColumnWidth(21, 30 * FACTOR);
		sheet.setColumnWidth(22, 11 * FACTOR);

		// EVUs
		sheet.setColumnWidth(23, 11 * FACTOR);
		sheet.setColumnWidth(24, 30 * FACTOR);
		sheet.setColumnWidth(25, 11 * FACTOR);

		sheet.setColumnWidth(26, 11 * FACTOR);
		sheet.setColumnWidth(27, 11 * FACTOR);
		sheet.setColumnWidth(28, 20 * FACTOR);
		sheet.setColumnWidth(29, 11 * FACTOR);
		sheet.setColumnWidth(30, 11 * FACTOR);
		sheet.setColumnWidth(31, 20 * FACTOR);
		sheet.setColumnWidth(32, 11 * FACTOR);
		sheet.setColumnWidth(33, 11 * FACTOR);
		sheet.setColumnWidth(34, 20 * FACTOR);
		sheet.setColumnWidth(35, 11 * FACTOR);
		sheet.setColumnWidth(36, 11 * FACTOR);
		sheet.setColumnWidth(37, 20 * FACTOR);
		sheet.setColumnWidth(38, 11 * FACTOR);
		sheet.setColumnWidth(39, 11 * FACTOR);
		sheet.setColumnWidth(40, 20 * FACTOR);
		sheet.setColumnWidth(41, 11 * FACTOR);
		sheet.setColumnWidth(42, 11 * FACTOR);
		sheet.setColumnWidth(43, 20 * FACTOR);
		sheet.setColumnWidth(44, 20 * FACTOR);
		sheet.setColumnWidth(45, 20 * FACTOR);
		sheet.setColumnWidth(46, 11 * FACTOR);
		sheet.setColumnWidth(47, 11 * FACTOR);
		sheet.setColumnWidth(48, 20 * FACTOR);
		sheet.setColumnWidth(49, 11 * FACTOR);
		sheet.setColumnWidth(50, 11 * FACTOR);
		sheet.setColumnWidth(51, 20 * FACTOR);
		sheet.setColumnWidth(52, 11 * FACTOR);
		sheet.setColumnWidth(53, 11 * FACTOR);
		sheet.setColumnWidth(54, 20 * FACTOR);
		sheet.setColumnWidth(55, 11 * FACTOR);
		sheet.setColumnWidth(56, 11 * FACTOR);
		sheet.setColumnWidth(57, 20 * FACTOR);
		sheet.setColumnWidth(58, 11 * FACTOR);
		sheet.setColumnWidth(59, 11 * FACTOR);
		sheet.setColumnWidth(60, 20 * FACTOR);

		Iterator rowIt = sheet.rowIterator();

		final int[] dateColumns = new int[] { 6, 7, 8, 26, 27, 29, 30, 32, 33, 35, 36, 38, 39, 41,
		        42, 46, 47, 49, 50, 52, 53, 55, 56, 58, 59 };

		int lfdNrBefore = 0;
		int lfdNrCurrent = 0;
		while (rowIt.hasNext()) {
			HSSFRow r = (HSSFRow) rowIt.next();
			try {
				lfdNrCurrent = (int) r.getCell(0).getNumericCellValue();
			} catch (Exception e) {
				// do nothing
			}

			if (r.getRowNum() < 2) {// Header
				Iterator it = r.cellIterator();
				while (it.hasNext()) {
					((HSSFCell) it.next()).setCellStyle(headerStyle);
				}
			} else {// Baumassnahmen
				Iterator it = r.cellIterator();
				HSSFCell cell = null;
				while (it.hasNext()) {
					cell = (HSSFCell) it.next();
					if (Arrays.binarySearch(dateColumns, cell.getColumnIndex()) >= 0) {
						if (lfdNrBefore != lfdNrCurrent)
							cell.setCellStyle(dateStyleBorder);
						else
							cell.setCellStyle(dateStyle);
					} else {
						if (lfdNrBefore != lfdNrCurrent)
							cell.setCellStyle(defaultStyleBorder);
					}
				}
			}
			lfdNrBefore = lfdNrCurrent;
		}

	}

	private void writeBaumassnahmen(HSSFSheet sheet, List<Baumassnahme> list) {
		int rowIndex = 2;

		Iterator<Baumassnahme> it = list.iterator();
		Baumassnahme bm = null;
		int lfdNr = 0;
		while (it.hasNext()) {
			lfdNr++;
			bm = it.next();

			int rowsCreated = writeSchnittstelleBBP(rowIndex, sheet, bm, lfdNr);
			writeSchnittstellePEVU(rowIndex + rowsCreated, sheet, bm, lfdNr);
			rowIndex = rowIndex + rowsCreated + bm.getPevus().size();
			writeSchnittstelleGEVU(rowIndex, sheet, bm, lfdNr);
			rowIndex = rowIndex + bm.getGevus().size();
		}
	}

	private void writeSchnittstelleGEVU(int rowIndex, HSSFSheet sheet, Baumassnahme bm, int lfdNr) {

		HSSFRow row = null;
		HSSFCell cell = null;
		int columnIndex = 0;

		Set<TerminUebersichtGueterverkehrsEVU> gevus = bm.getGevus();
		Iterator<TerminUebersichtGueterverkehrsEVU> it = gevus.iterator();
		TerminUebersichtGueterverkehrsEVU gevu = null;
		while (it.hasNext()) {
			row = sheet.createRow(rowIndex);
			columnIndex = writeStammdaten(rowIndex, sheet, bm, lfdNr);
			rowIndex++;
			// Änderung/Ausfälle
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			gevu = it.next();
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes
			    .getMessage("baumassnahme.schnittstellen.gevu")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(gevu.getEvuGruppe().getName()));// Name
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(""));// Kd-Nr

			// Studie/Grobkonzept
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getStudieGrobkonzept() != null)
				cell.setCellValue(gevu.getStudieGrobkonzept());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (bm.getArt().equals(Art.KS))
				cell.setCellValue(new HSSFRichTextString(""));
			else
				cell.setCellValue(new HSSFRichTextString(castStatusToString(StatusType.NEUTRAL)));

			// Anf. BBZR
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Vorentwurf BiÜ/Zvf
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Zvf-entwurf
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getZvfEntwurfSoll() != null)
				cell.setCellValue(gevu.getZvfEntwurfSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getZvfEntwurf() != null)
				cell.setCellValue(gevu.getZvfEntwurf());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getZvfEntwurfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getZvfEntwurfStatus())));
			}

			// Stellungnahme EVU
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getStellungnahmeEVUSoll() != null)
				cell.setCellValue(gevu.getStellungnahmeEVUSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getStellungnahmeEVU() != null)
				cell.setCellValue(gevu.getStellungnahmeEVU());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getStellungnahmeEVUStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getStellungnahmeEVUStatus())));
			}

			// Gesamtkonzept BBZR
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Ausfälle/SEV
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// B-Konzept EVU
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// BiÜ/ZvF
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getZvFSoll() != null)
				cell.setCellValue(gevu.getZvFSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getZvF() != null)
				cell.setCellValue(gevu.getZvF());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getZvFStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu.getZvFStatus())));
			}

			// Master ÜB
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getMasterUebergabeblattGVSoll() != null)
				cell.setCellValue(gevu.getMasterUebergabeblattGVSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getMasterUebergabeblattGV() != null)
				cell.setCellValue(gevu.getMasterUebergabeblattGV());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getMasterUebergabeblattGVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getMasterUebergabeblattGVStatus())));
			}

			// ÜB
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getUebergabeblattGVSoll() != null)
				cell.setCellValue(gevu.getUebergabeblattGVSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getUebergabeblattGV() != null)
				cell.setCellValue(gevu.getUebergabeblattGV());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getUebergabeblattGVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getUebergabeblattGVStatus())));
			}

			// Fplo
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getFploSoll() != null)
				cell.setCellValue(gevu.getFploSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getFplo() != null)
				cell.setCellValue(gevu.getFplo());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getFploStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu.getFploStatus())));
			}

			// Eingabe GFDZ
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getEingabeGFD_ZSoll() != null)
				cell.setCellValue(gevu.getEingabeGFD_ZSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (gevu.getEingabeGFD_Z() != null)
				cell.setCellValue(gevu.getEingabeGFD_Z());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (gevu.getEingabeGFD_ZStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getEingabeGFD_ZStatus())));
			}
		}
	}

	private int writeStammdaten(int rowIndex, HSSFSheet sheet, Baumassnahme bm, int lfdNr) {
		int columnIndex = 0;
		HSSFCell cell = null;
		HSSFRow row = sheet.getRow(rowIndex);
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(lfdNr++);

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(bm.getVorgangsNr());

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getFploNr()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckeBBP()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckeVZG()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckenAbschnitt()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(bm.getBeginnFuerTerminberechnung());

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(bm.getBeginnDatum());

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(bm.getEndDatum());

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getArtDerMassnahme()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getBetriebsweise()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getRegionalbereichBM()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getRegionalBereichFpl().getName()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bm.getPrioritaet() != null) {
			cell.setCellValue(Integer.parseInt(bm.getPrioritaet().getValue()));
		} else {
			cell.setCellValue(new HSSFRichTextString(""));
		}

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(bm.getArt().name()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		String kigBauValue = msgRes.getMessage("baumassnahme.kigbau.false");
		if (bm.getKigBau() == true) {
			kigBauValue = msgRes.getMessage("baumassnahme.kigbau.true");
		}
		cell.setCellValue(new HSSFRichTextString(kigBauValue));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		StringBuilder sbKigBauNr = new StringBuilder();
		if (bm.getKigBauNr() != null)
			for (String nr : bm.getKigBauNr()) {
				if (sbKigBauNr.length() > 0)
					sbKigBauNr.append(", ");
				sbKigBauNr.append(nr);
			}
		cell.setCellValue(new HSSFRichTextString(sbKigBauNr.toString()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bm.getKorridorNr() != null) {
			cell.setCellValue(bm.getKorridorNr());
		}

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		StringBuilder sbKorridorZeitfenster = new StringBuilder();
		if (bm.getKorridorZeitfenster() != null)
			for (String zf : bm.getKorridorZeitfenster()) {
				if (sbKorridorZeitfenster.length() > 0)
					sbKorridorZeitfenster.append(", ");
				sbKorridorZeitfenster.append(zf);
			}
		cell.setCellValue(new HSSFRichTextString(sbKorridorZeitfenster.toString()));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		StringBuilder sbQsNummer = new StringBuilder();
		for (String nr : bm.getQsNr()) {
			if (sbQsNummer.length() > 0)
				sbQsNummer.append(", ");
			sbQsNummer.append(nr);
		}
		cell.setCellValue(new HSSFRichTextString(sbQsNummer.toString()));

		return columnIndex;
	}

	private void writeSchnittstellePEVU(int rowIndex, HSSFSheet sheet, Baumassnahme bm, int lfdNr) {

		HSSFRow row = null;
		HSSFCell cell = null;
		int columnIndex = 0;

		Set<TerminUebersichtPersonenverkehrsEVU> pevus = bm.getPevus();
		Iterator<TerminUebersichtPersonenverkehrsEVU> it = pevus.iterator();
		TerminUebersichtPersonenverkehrsEVU pevu = null;
		while (it.hasNext()) {
			row = sheet.createRow(rowIndex);
			columnIndex = writeStammdaten(rowIndex, sheet, bm, lfdNr);
			rowIndex++;
			// Änderung/Ausfälle
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			pevu = it.next();
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes
			    .getMessage("baumassnahme.schnittstellen.pevu")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(pevu.getEvuGruppe().getName()));// Name
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(""));// Kd-Nr

			// Studie/Grobkonzept
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getStudieGrobkonzept() != null)
				cell.setCellValue(pevu.getStudieGrobkonzept());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (bm.getArt().equals(Art.KS))
				cell.setCellValue(new HSSFRichTextString(""));
			else
				cell.setCellValue(new HSSFRichTextString(castStatusToString(StatusType.NEUTRAL)));

			// Anf. BBZR
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Vorentwurf BiÜ/Zvf
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Zvf-entwurf
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getZvfEntwurfSoll() != null)
				cell.setCellValue(pevu.getZvfEntwurfSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getZvfEntwurf() != null)
				cell.setCellValue(pevu.getZvfEntwurf());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getZvfEntwurfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getZvfEntwurfStatus())));
			}

			// Stellungnahme EVU
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getStellungnahmeEVUSoll() != null)
				cell.setCellValue(pevu.getStellungnahmeEVUSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getStellungnahmeEVU() != null)
				cell.setCellValue(pevu.getStellungnahmeEVU());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getStellungnahmeEVUStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getStellungnahmeEVUStatus())));
			}

			// Gesamtkonzept BBZR
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

			// Ausfälle/SEV
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.isAusfaelleSEV() == true) {
				cell.setCellValue(new HSSFRichTextString(msgRes
				    .getMessage("baumassnahme.ausfall.ja")));
			} else {
				cell.setCellValue(new HSSFRichTextString(msgRes
				    .getMessage("baumassnahme.ausfall.nein")));
			}

			// B-Konzept EVU
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getBKonzeptEVUStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getBKonzeptEVUStatus())));
			}

			// BiÜ/ZvF
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getZvFSoll() != null)
				cell.setCellValue(pevu.getZvFSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getZvF() != null)
				cell.setCellValue(pevu.getZvF());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getZvFStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu.getZvFStatus())));
			}

			// Master ÜB
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getMasterUebergabeblattPVSoll() != null)
				cell.setCellValue(pevu.getMasterUebergabeblattPVSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getMasterUebergabeblattPV() != null)
				cell.setCellValue(pevu.getMasterUebergabeblattPV());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getMasterUebergabeblattPVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getMasterUebergabeblattPVStatus())));
			}

			// ÜB
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getUebergabeblattPVSoll() != null)
				cell.setCellValue(pevu.getUebergabeblattPVSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getUebergabeblattPV() != null)
				cell.setCellValue(pevu.getUebergabeblattPV());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getUebergabeblattPVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getUebergabeblattPVStatus())));
			}

			// Fplo
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getFploSoll() != null)
				cell.setCellValue(pevu.getFploSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getFplo() != null)
				cell.setCellValue(pevu.getFplo());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getFploStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu.getFploStatus())));
			}

			// Eingabe GFDZ
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getEingabeGFD_ZSoll() != null)
				cell.setCellValue(pevu.getEingabeGFD_ZSoll());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
			if (pevu.getEingabeGFD_Z() != null)
				cell.setCellValue(pevu.getEingabeGFD_Z());
			else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			if (pevu.getEingabeGFD_ZStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
				    .getEingabeGFD_ZStatus())));
			}
		}
	}

	private int writeSchnittstelleBBP(int rowIndex, HSSFSheet sheet, Baumassnahme bm, int lfdNr) {

		HSSFRow row = sheet.createRow(rowIndex);
		HSSFCell cell = null;
		int columnIndex = 0;

		TerminUebersichtBaubetriebsplanung bbp = bm.getBaubetriebsplanung();

		columnIndex = writeStammdaten(rowIndex, sheet, bm, lfdNr);
		int rowsCreated = writeAenderungAusfallGrund(rowIndex, columnIndex, sheet, bm);
		columnIndex = columnIndex + 3;

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.schnittstellen.bbp")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(""));// Name
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(""));// Kd-Nr

		// Studie/Grobkonzept
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getStudieGrobkonzept() != null)
			cell.setCellValue(bbp.getStudieGrobkonzept());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bm.getArt().equals(Art.KS))
			cell.setCellValue(new HSSFRichTextString(""));
		else
			cell.setCellValue(new HSSFRichTextString(castStatusToString(StatusType.NEUTRAL)));

		// Anf. BBZR
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getAnforderungBBZRSoll() != null)
			cell.setCellValue(bbp.getAnforderungBBZRSoll());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getAnforderungBBZR() != null)
			cell.setCellValue(bbp.getAnforderungBBZR());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bbp.getAnforderungBBZRStatus() != null) {
			cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
			    .getAnforderungBBZRStatus())));
		}

		// Vorentwurf BiÜ/Zvf
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getBiUeEntwurfSoll() != null)
			cell.setCellValue(bbp.getBiUeEntwurfSoll());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getBiUeEntwurf() != null)
			cell.setCellValue(bbp.getBiUeEntwurf());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bbp.getBiUeEntwurfStatus() != null) {
			cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp.getBiUeEntwurfStatus())));
		}

		// Zvf-entwurf
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getZvfEntwurfSoll() != null)
			cell.setCellValue(bbp.getZvfEntwurfSoll());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getZvfEntwurf() != null)
			cell.setCellValue(bbp.getZvfEntwurf());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bbp.getZvfEntwurfStatus() != null) {
			cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp.getZvfEntwurfStatus())));
		}

		// Stellungnahme EVU
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getGesamtKonzeptBBZRSoll() != null)
			cell.setCellValue(bbp.getZvfEntwurfSoll());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getGesamtKonzeptBBZR() != null)
			cell.setCellValue(bbp.getGesamtKonzeptBBZR());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bbp.getGesamtKonzeptBBZRStatus() != null) {
			cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
			    .getGesamtKonzeptBBZRStatus())));
		}

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);// Ausfälle/SEV
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);// B-Konzept EVU

		// BiÜ/ZvF
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getZvfSoll() != null)
			cell.setCellValue(bbp.getZvfSoll());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_NUMERIC);
		if (bbp.getZvf() != null)
			cell.setCellValue(bbp.getZvf());
		else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		if (bbp.getZvfStatus() != null) {
			cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp.getZvfStatus())));
		}

		// Master-ÜB, ÜB, Fplo, Eingabe GFD-Z
		for (int i = 0; i <= 3; i++) {
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
		}

		return rowsCreated;
	}

	private int writeAenderungAusfallGrund(int rowIndex, int columnIndex, HSSFSheet sheet,
	    Baumassnahme bm) {

		HSSFRow row = sheet.getRow(rowIndex);
		HSSFCell cell = null;
		int rowsCreated = 0;

		// Eskalation/Ausfall ausgeben
		if (bm.getAusfallGrund() != null) {
			cell = row.createCell(columnIndex, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.ausfall")));

			cell = row.createCell(columnIndex + 1, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(bm.getAusfallGrund().getName()));

			cell = row.createCell(columnIndex + 2, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(bm.getBisherigerAufwandTimeString()));

			rowsCreated++;
		}

		// Änderungsdokumentation ausgeben
		Iterator<Aenderung> aenderungIter = bm.getAenderungen().iterator();
		while (aenderungIter.hasNext()) {
			Aenderung a = aenderungIter.next();
			if (rowsCreated == 0)
				row = sheet.getRow(rowIndex);
			else
				row = sheet.createRow(++rowIndex);

			cell = row.createCell(columnIndex, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.aenderung")));

			cell = row.createCell(columnIndex + 1, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(a.getGrund().getName()));

			cell = row.createCell(columnIndex + 2, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(a.getAufwandTimeString()));

			rowsCreated++;
		}

		if (rowsCreated == 0) {
			cell = row.createCell(columnIndex, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex + 1, HSSFCell.CELL_TYPE_BLANK);
			cell = row.createCell(columnIndex + 2, HSSFCell.CELL_TYPE_BLANK);
		}
		// return (rowsCreated <= 0 ? 0 : rowsCreated);
		return (rowsCreated <= 0 ? 1 : rowsCreated);
	}

	private void configureWorkbook(HSSFWorkbook workbook) {
		// HeaderStyle
		headerStyle = workbook.createCellStyle();
		HSSFFont headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("Calibri");
		headerFont.setColor((short) 1);

		headerStyle.setAlignment((HSSFCellStyle.ALIGN_CENTER));
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle.setFillForegroundColor(HSSFColor.TEAL.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);

		// DateStyle
		dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

		dateStyleBorder = workbook.createCellStyle();
		dateStyleBorder.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		dateStyleBorder.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

		defaultStyleBorder = workbook.createCellStyle();
		defaultStyleBorder.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

	}

	private void writeColumnHeader(HSSFSheet sheet) {

		// Kopfzeile 1 von 2
		HSSFRow row = sheet.createRow(0);

		int columnIndex = 0;
		HSSFCell cell = null;

		for (int i = 0; i <= 19; i++) {
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_BLANK);
		}

		cell = row.createCell(columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.aenderungausfall")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("title.meilensteine.schnittstelle")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.studie.long")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.anforderungbbzr.long")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.bbp.biueentwurf.long")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.bbp.zvfentwurf.long")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.stellungnahmeevu.nobr")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.gesamtkonzeptbbzr.nobr")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		// ausfaellesev
		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_BLANK);

		// bkonzeptevu
		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_BLANK);

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.bbp.zvf")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.masteruebergabeblatt")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.uebergabeblatt")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.fplo")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		cell = row.createCell(++columnIndex, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.eingabegfd_z")));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex += 2));

		// Kopfzeile 2 von 2
		row = sheet.createRow(1);
		columnIndex = 0;
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.lfdnr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.vorgangsnr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.fplonr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.streckebbp")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.streckevzg")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.streckenabschnitt")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.beginn.terminberechnung")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.baubeginn")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.bauende")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.artdermassnahme")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.betriebsweise")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.regionalbereichbm")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.regionalbereichfpl")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.prioritaet")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.art.short")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.kigbau")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.kigbaunr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.korridornr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.korridorzeitfenster")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.qsnr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.aenderung.typ")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.grund")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.aufwand")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.schnittstelle.typ")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.schnittstelle.name")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.schnittstelle.kdnr")));

		for (int i = 0; i <= 5; i++) {
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.soll")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.ist")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes
			    .getMessage("baumassnahme.termine.status")));
		}

		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.ausfaellesev.nobr")));
		cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(msgRes
		    .getMessage("baumassnahme.termine.bkonzeptevu.nobr")));

		for (int i = 0; i <= 4; i++) {
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.soll")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes.getMessage("baumassnahme.termine.ist")));
			cell = row.createCell(columnIndex++, HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(msgRes
			    .getMessage("baumassnahme.termine.status")));
		}

	}

	private String castStatusToString(StatusType status) {
		if (status == null)
			return null;

		String result = null;

		switch (status) {
		case GREEN:
			result = msgRes.getMessage("baumassnahme.termine.puenktlich");
			break;

		case NEUTRAL:
			result = msgRes.getMessage("baumassnahme.termine.nichterforderlich");
			break;

		case RED:
			result = msgRes.getMessage("baumassnahme.termine.ueberschritten");
			break;

		case COUNTDOWN14:
			result = msgRes.getMessage("baumassnahme.termine.bald");
			break;

		case OFFEN:
			result = msgRes.getMessage("baumassnahme.termine.offen");
			break;

		case LILA:
			result = msgRes.getMessage("baumassnahme.termine.ueberschrittenoffen");
			break;

		default:
			result = msgRes.getMessage("baumassnahme.termine.nichterforderlich");
		}

		return result;
	}
}
