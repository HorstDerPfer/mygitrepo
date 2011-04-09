package db.training.bob.web.baumassnahme;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.BBPStrecke;
import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Sender;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.BaumassnahmeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.model.VzgStrecke;

public class UebergabeblattExportExcelAction extends UebergabeblattExportAction {

	private static Logger log = Logger.getLogger(UebergabeblattExportExcelAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattExportExcelAction.");

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("tab"))) {
			request.setAttribute("tab", request.getParameter("tab"));
		}

		Integer id = null;
		if (request.getParameter("baumassnahmeId") != null)
			id = FrontendHelper.castStringToInteger(request.getParameter("baumassnahmeId"));
		Integer zvfId = null;
		if (request.getParameter("zvfId") != null)
			zvfId = FrontendHelper.castStringToInteger(request.getParameter("zvfId"));

		Uebergabeblatt ueb = getUebergabeblatt(zvfId);
		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		Baumassnahme bm = bmService.findById(id);

		String filename = ueb.getHeader().getFilename();
		filename = filename.substring(0, filename.length() - 3) + "xls";
		if (filename.equals("")) {
			filename = "ÜB" + ueb.getMassnahmen().iterator().next().getVorgangsNr().toString()
			    + ".xls";
		}

		HSSFWorkbook wb = createXls(filename, bm, ueb);
		ServletOutputStream outStream = response.getOutputStream();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
		wb.write(outStream);

		outStream.flush();

		return (null);
	}

	private HSSFWorkbook createXls(String filename, Baumassnahme bm, Uebergabeblatt ueb) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFCellStyle header = workbook.createCellStyle();
		HSSFCellStyle text = workbook.createCellStyle();
		HSSFCellStyle date = workbook.createCellStyle();
		HSSFCellStyle time = workbook.createCellStyle();
		HSSFFont standard = workbook.createFont();
		HSSFFont bold = workbook.createFont();

		Massnahme m = ueb.getMassnahmen().iterator().next();
		List<Strecke> strecken = m.getStrecke();
		int startRowEmpfaenger = 11;
		int startRowSender = 2;
		int startRowStrecken = startRowEmpfaenger + ueb.getHeader().getEmpfaenger().size() + 2;
		int startRowMassnahme = startRowStrecken + strecken.size() + 2;
		int startRowZuege = startRowMassnahme + 8;

		standard.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		header.setFillBackgroundColor(HSSFColor.TEAL.index);
		header.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		header.setFont(bold);
		text.setFillBackgroundColor((short) 0);
		text.setFont(standard);
		date.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		time.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm"));

		workbook.setSheetName(0, "Übergabeblatt");
		for (int i = 0; i < 20; i++) {
			sheet.setColumnWidth(i, 15 * 267);
		}

		// Vorgangsnr
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString("Vorgangsnr"));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(bm.getVorgangsNr());

		// Sender
		Sender s = ueb.getHeader().getSender();
		String[] columnHeader = { "Name", "Vorname", "Straße", "PLZ", "Ort", "Tel. extern",
		        "Tel. intern", "Email" };

		row = sheet.createRow(startRowSender);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[0]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getName()));

		row = sheet.createRow(startRowSender + 1);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[1]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getVorname()));

		row = sheet.createRow(startRowSender + 2);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[2]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getStrasse()));

		row = sheet.createRow(startRowSender + 3);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[3]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getPlz()));

		row = sheet.createRow(startRowSender + 4);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[4]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getOrt()));

		row = sheet.createRow(startRowSender + 5);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[5]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getTelefon()));

		row = sheet.createRow(startRowSender + 6);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[6]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getTelefonIntern()));

		row = sheet.createRow(startRowSender + 7);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader[7]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(s.getEmail()));

		// Empfänger
		row = sheet.createRow(startRowEmpfaenger);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString("Empfänger"));
		Set<String> empfaenger = ueb.getHeader().getEmpfaenger();
		int rowNr = 1;
		for (String emp : empfaenger) {
			row = sheet.createRow(startRowEmpfaenger + rowNr++);
			cell = row.createCell(0);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(emp));
		}

		// Strecken
		String[] columnHeader3 = { "VZG-Strecke", "Betroffener Bereich", "Zeitraum von",
		        "Zeitraum bis", "Unterbrochen", "Grund", "Betriebsweise" };
		row = sheet.createRow(startRowStrecken);
		for (int i = 0; i < columnHeader3.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(header);
			cell.setCellValue(new HSSFRichTextString(columnHeader3[i]));
		}
		Strecke str = null;
		for (int i = 0; i < strecken.size(); i++) {
			str = strecken.get(i);
			row = sheet.createRow(startRowStrecken + 1 + i);

			cell = row.createCell(0);
			cell.setCellStyle(text);

			if (str.getStreckeVZG().size() > 0) {
				StringBuffer buf = new StringBuffer();
				for (VzgStrecke vzg : str.getStreckeVZG()) {
					if (buf.length() != 0)
						buf.append(",");
					buf.append(vzg.getNummer());
				}
				cell.setCellValue(new HSSFRichTextString(buf.toString()));
			} else
				cell.setCellValue(new HSSFRichTextString(""));

			cell = row.createCell(1);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(str.getMassnahme()));
			cell = row.createCell(2);
			cell.setCellStyle(date);
			cell.setCellValue(str.getBaubeginn());
			cell = row.createCell(3);
			cell.setCellStyle(date);
			cell.setCellValue(str.getBauende());
			cell = row.createCell(4);
			cell.setCellStyle(text);
			if (str.getZeitraumUnterbrochen() != null)
				cell.setCellValue(str.getZeitraumUnterbrochen());
			cell = row.createCell(5);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(str.getGrund()));
			cell = row.createCell(6);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(str.getBetriebsweise()));
		}

		// Massnahme
		String[] columnHeader2 = { "BBP-Strecke", "Formularkennung", "Version", "Baumaßnahmenart",
		        "Kennung", "Lisba/KiGbau", "Qs-Nr/Ks-Nr/VeS-Nr", "Korridor", "Festgelegt für SPFV",
		        "Festgelegt für SPNV", "Festgelegt für SGV" };

		// BBP-Strecke - Lisba/KiGbau
		row = sheet.createRow(startRowMassnahme);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[0]));
		cell = row.createCell(1);
		cell.setCellStyle(text);

		if (m.getBbp().size() > 0) {
			StringBuffer buf = new StringBuffer();
			for (BBPStrecke bbp : m.getBbp()) {
				if (buf.length() != 0)
					buf.append(",");
				buf.append(bbp.getNummer());
			}
			cell.setCellValue(new HSSFRichTextString(buf.toString()));
		} else
			cell.setCellValue(new HSSFRichTextString(""));

		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[5]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getKigbau()));

		// Formularkennung - qs/Ks/VesNr
		row = sheet.createRow(startRowMassnahme + 1);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[1]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getVersion().getFormular().toString()));
		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[6]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getQsKsVesNr()));

		// Version - Korridor
		row = sheet.createRow(startRowMassnahme + 2);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[2]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		StringBuffer buf = new StringBuffer();
		buf.append(m.getVersion().getMajor());
		buf.append(".");
		buf.append(m.getVersion().getMinor());
		buf.append(".");
		buf.append(m.getVersion().getSub());
		cell.setCellValue(new HSSFRichTextString(buf.toString()));
		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[7]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getKorridor()));

		// Baumassnahmenart - Festgelegt SPFV
		row = sheet.createRow(startRowMassnahme + 3);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[3]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getBaumassnahmenart().toString()));
		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[8]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		if (m.getFestgelegtSPFV() != null)
			cell.setCellValue(m.getFestgelegtSPFV());

		// Kennung - Festgelegt SPNV
		row = sheet.createRow(startRowMassnahme + 4);
		cell = row.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[4]));
		cell = row.createCell(1);
		cell.setCellStyle(text);
		cell.setCellValue(new HSSFRichTextString(m.getKennung()));
		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[9]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		if (m.getFestgelegtSPNV() != null)
			cell.setCellValue(m.getFestgelegtSPNV());

		// leer - Festgelegt SGV
		row = sheet.createRow(startRowMassnahme + 5);
		cell = row.createCell(3);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString(columnHeader2[10]));
		cell = row.createCell(4);
		cell.setCellStyle(text);
		if (m.getFestgelegtSGV() != null)
			cell.setCellValue(m.getFestgelegtSGV());

		// Zuege
		String[] columnHeader4 = { "Nr", "Datum", "Zuggattung", "Zugnummer", "Abgangsbahnhof",
		        "Zielbahnhof", "Tfz", "Last", "Mbr + Brems", "Zuglänge", "VMax", "KV-Profil",
		        "Streckenklasse", "Bemerkung", "QS/KS", "Bahnhof", "Haltart", "An", "Ab",
		        "Relativlage", "Richtung" };
		row = sheet.createRow(startRowZuege);
		cell = row.createCell(11);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString("Besonderheiten"));
		CellRangeAddress region = new CellRangeAddress(startRowZuege, startRowZuege, 11, 12);
		sheet.addMergedRegion(region);

		cell = row.createCell(15);
		cell.setCellStyle(header);
		cell.setCellValue(new HSSFRichTextString("Knotenzeiten"));
		region = new CellRangeAddress(startRowZuege, startRowZuege, 15, 19);
		sheet.addMergedRegion(region);
		row = sheet.createRow(startRowZuege + 1);
		for (int i = 0; i < columnHeader4.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(header);
			cell.setCellValue(new HSSFRichTextString(columnHeader4[i]));
		}
		List<Zug> zuege = m.getZug();
		Zug zug = null;
		int currentRow = startRowZuege + 2;
		for (int i = 0; i < zuege.size(); i++) {
			zug = zuege.get(i);
			row = sheet.createRow(currentRow++);

			cell = row.createCell(0);
			cell.setCellStyle(text);
			cell.setCellValue(i + 1);
			cell = row.createCell(1);
			cell.setCellStyle(date);
			if (zug.getVerkehrstag() != null)
				cell.setCellValue(zug.getVerkehrstag());
			cell = row.createCell(2);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(zug.getZugbez()));
			cell = row.createCell(3);
			cell.setCellStyle(text);
			if (zug.getZugnr() != null)
				cell.setCellValue(zug.getZugnr());
			cell = row.createCell(4);
			cell.setCellStyle(text);
			if (zug.getRegelweg().getAbgangsbahnhof() != null)
				cell.setCellValue(new HSSFRichTextString(zug.getRegelweg().getAbgangsbahnhof()
				    .getLangName()));
			cell = row.createCell(5);
			cell.setCellStyle(text);
			if (zug.getRegelweg().getZielbahnhof() != null)
				cell.setCellValue(new HSSFRichTextString(zug.getRegelweg().getZielbahnhof()
				    .getLangName()));
			cell = row.createCell(6);
			cell.setCellStyle(text);
			if (zug.getZugdetails().getTfz() != null)
				cell.setCellValue(new HSSFRichTextString(zug.getZugdetails().getTfz().getTfz()));
			cell = row.createCell(7);
			cell.setCellStyle(text);
			if (zug.getZugdetails().getLast().getLast() != null)
				cell.setCellValue(zug.getZugdetails().getLast().getLast());
			cell = row.createCell(8);
			cell.setCellStyle(text);
			if (zug.getZugdetails().getBrems() != null)
				cell.setCellValue(new HSSFRichTextString(zug.getZugdetails().getBrems()));
			cell = row.createCell(9);
			cell.setCellStyle(text);
			if (zug.getZugdetails().getLaenge() != null)
				cell.setCellValue(zug.getZugdetails().getLaenge());
			cell = row.createCell(10);
			cell.setCellStyle(text);
			if (zug.getZugdetails().getVmax() != null)
				cell.setCellValue(zug.getZugdetails().getVmax());
			cell = row.createCell(11);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(zug.getKvProfil()));
			cell = row.createCell(12);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(zug.getStreckenKlasse()));
			cell = row.createCell(13);
			cell.setCellStyle(text);
			cell.setCellValue(new HSSFRichTextString(zug.getBemerkung()));
			cell = row.createCell(14);
			cell.setCellStyle(text);
			int qsks = zug.getQs_ks();
			if (qsks == 1)
				cell.setCellValue(new HSSFRichTextString("QS"));
			else if (qsks == 2)
				cell.setCellValue(new HSSFRichTextString("KS"));
			else
				cell.setCellValue(new HSSFRichTextString("nicht aktiv"));
			// Knotenzeiten
			Iterator<Knotenzeit> itKnotenzeiten = zug.getKnotenzeiten().iterator();
			Knotenzeit k = null;
			while (itKnotenzeiten.hasNext()) {
				k = itKnotenzeiten.next();
				cell = row.createCell(15);
				cell.setCellStyle(text);
				if (k.getBahnhof() != null)
					cell.setCellValue(new HSSFRichTextString(k.getBahnhof()));
				cell = row.createCell(16);
				cell.setCellStyle(text);
				if (k.getHaltart() != null)
					cell.setCellValue(new HSSFRichTextString(k.getHaltart().toString()));
				cell = row.createCell(17);
				cell.setCellStyle(time);
				if (k.getAnkunft() != null)
					cell.setCellValue(k.getAnkunft());
				cell = row.createCell(18);
				cell.setCellStyle(time);
				if (k.getAbfahrt() != null)
					cell.setCellValue(k.getAbfahrt());
				cell = row.createCell(19);
				cell.setCellStyle(text);
				if (k.getRelativlage() != null)
					cell.setCellValue(k.getRelativlage());
				if (itKnotenzeiten.hasNext())
					row = sheet.createRow(currentRow++);
			}
			cell = row.createCell(20);
			cell.setCellStyle(text);
			if (zug.getRichtung() == false)
				cell.setCellValue(1);
			if (zug.getRichtung() == true)
				cell.setCellValue(2);

		}

		return workbook;
	}
}
