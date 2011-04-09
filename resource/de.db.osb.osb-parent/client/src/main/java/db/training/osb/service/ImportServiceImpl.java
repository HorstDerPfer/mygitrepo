package db.training.osb.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Paket;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.Umleitung;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.web.imports.ImportMessage;
import db.training.osb.web.imports.ImportTable;
import db.training.osb.web.imports.ImportMessage.Level;
import db.training.security.SecurityService;
import db.training.security.User;
import db.training.security.hibernate.TqmUser;

@SuppressWarnings("unchecked")
public class ImportServiceImpl extends EasyServiceImpl implements ImportService {

	public ImportServiceImpl() {
		super(null);
	}

	private static Logger log = Logger.getLogger(ImportServiceImpl.class);

	private String tableOwner;

	public String getTableOwner() {
		return tableOwner;
	}

	public void setTableOwner(String tableOwner) {
		this.tableOwner = tableOwner;
	}

	/**
	 * erzeugt eine Liste aller Tabellennamen, für die ein Benutzer <code>secUser</code> Daten
	 * importieren kann, und gibt diese zurück. Um in Verknüpfungstabellen schreiben zu können, muss
	 * der Benutzer Schreibzugriff auf die beiden, dazugehörigen Entity-Tabellen haben.
	 * 
	 * "Regionalrechte werden berücksichtigt.
	 * 
	 * @param secUser
	 * @return
	 */
	private static List<String> getAllowedTables(TqmUser secUser) {
		List<String> allowedTables = new ArrayList<String>();

		if (secUser == null)
			return allowedTables;

		String regionalPermissions = System.getProperty("regionalPermissions");

		boolean importRechteTemporaerAufgeweicht = regionalPermissions != null
		    && regionalPermissions.equals("true")
		    && (secUser.hasRole("BEARBEITER_REGIONAL") || secUser.hasRole("BEARBEITER_ZENTRAL") || secUser
		        .hasRole("ADMINISTRATOR_REGIONAL"));

		if (secUser.hasAuthorization("ROLE_IMPORT_AUSFUEHREN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(SAPMassnahme.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_TOPPROJEKT_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(TopProjekt.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_BUENDEL_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Buendel.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Fahrplanregelung.class.getAnnotation(Table.class).name()
			    .toUpperCase());
		if (secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Umleitung.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_UMLEITUNGSWEG_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Umleitungsweg.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Gleissperrung.class.getAnnotation(Table.class).name().toUpperCase());
		if (secUser.hasAuthorization("ROLE_IMPORT_AUSFUEHREN_ALLE")
		    || importRechteTemporaerAufgeweicht)
			allowedTables.add(Paket.class.getAnnotation(Table.class).name().toUpperCase());

		return allowedTables;
	}

	@Transactional(readOnly = false)
	public List<ImportMessage> importData(InputStream inputStream, TqmUser secUser) {
		if (log.isDebugEnabled())
			log.debug("importData(InputStream inputStream, User user)");

		int rowCount = 0;
		int cellCount = 0;
		List<ImportMessage> importMessages = new ArrayList<ImportMessage>();
		String message = null;
		StringBuilder sqlQuery = null;
		boolean linkTable = false;
		try {
			HSSFWorkbook hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
			for (String tableName : hssfworkbook.getSheetName(0).split("_"))
				if (!getAllowedTables(secUser).contains(tableName.toUpperCase())) {
					message = "Excel-Problem: Tabellenblatt 1 traegt nicht den Namen einer erlaubten Datenbanktabelle.";
					importMessages.add(new ImportMessage(Level.ERROR, message));
					log.error(message);
					return importMessages;
				}
			String dateNow = FrontendHelper.castDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			String sheetAndTableName = hssfworkbook.getSheetName(0);
			HSSFSheet sheet = hssfworkbook.getSheetAt(0);
			HSSFRow row = sheet.getRow(rowCount++);
			if (row == null) {
				message = "Excel-Problem: Tabellenblatt 1 enthaelt keine Daten.";
				importMessages.add(new ImportMessage(Level.ERROR, message));
				log.error(message);
				return importMessages;
			}

			HSSFCell cell = row.getCell(cellCount++);

			StringBuilder sqlPrefix = new StringBuilder("INSERT INTO " + tableOwner + "."
			    + sheetAndTableName + " (");

			// lastchangedate wird für die Dokumentation der Änderungshistorie
			// benötigt, z.Zt. in
			// Klärung #482
			// if (!sheetAndTableName.contains("_"))
			// sqlPrefix.append("`lastchangedate`, ");

			// Prüfung ob Linktabelle - Tabelle ist Linktabelle, wenn diese einen _ enthaelt
			if (hssfworkbook.getSheetName(0).contains("_"))
				linkTable = true;

			// Bei einer Link-Tabelle gibt es keine Spalte ID
			if (!linkTable)
				sqlPrefix.append("ID, ");

			while (cell != null && cell.getRichStringCellValue() != null) {
				// Die excel Tabelle darf keine Spalte ID enthalten, da die IDs von der
				// HibernateSequenz generiert wird
				if (cell.getRichStringCellValue().getString().toUpperCase().equals("ID")) {
					message = "Excel Tabelle darf keine Spalte ID enthalten!";
					importMessages.add(new ImportMessage(Level.ERROR, message));
					log.error(message);
					return importMessages;
				}
				sqlPrefix.append(cell.getRichStringCellValue() + ", ");
				cell = row.getCell(cellCount++);
			}
			sqlPrefix.replace(sqlPrefix.length() - 2, sqlPrefix.length(), ") VALUES (");

			row = sheet.getRow(rowCount++);
			int spalten = cellCount - 1;
			if (isEmptyRow(row, spalten)) {
				message = "Excel-Problem: Tabellenblatt 1, Zeile 2 enthaelt keine Daten.";
				importMessages.add(new ImportMessage(Level.ERROR, message));
				log.error(message);
				return importMessages;
			}
			cellCount = 0;
			while (!isEmptyRow(row, spalten)) {
				sqlQuery = new StringBuilder(sqlPrefix);

				// lastchangedate wird für die Dokumentation der
				// Änderungshistorie benötigt, z.Zt.
				// in Klärung #482
				// if (!sheetAndTableName.contains("_"))
				// sqlQuery.append("'" + dateNow + "', ");
				if (!linkTable)
					sqlQuery.append("hibernate_sequence.NEXTVAL, ");

				while (cellCount < spalten) {
					cell = row.getCell(cellCount++);

					if (cell != null)
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_BLANK:
							sqlQuery.append("NULL, ");
							break;

						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								String date = FrontendHelper.castDateToString(cell
								    .getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
								sqlQuery.append("TO_DATE('" + date
								    + "', 'yyyy-MM-dd HH24:MI:SS'), ");
							} else
								sqlQuery.append(cell.getNumericCellValue() + ", ");
							break;

						case HSSFCell.CELL_TYPE_STRING:
							String value = cell.getRichStringCellValue().getString();
							if (!"NULL".equalsIgnoreCase(value)) {
								// Import einer Zeichenkette mit max 20 Zeichen
								// als Datum
								Date cellDate = null;
								if (value.length() < 21) {
									cellDate = FrontendHelper.castStringToDate(value);
								}

								// wenn cellDate != null -> gueltige
								// Konvertierung von String ->
								// Datum
								if (cellDate != null) {
									String date = FrontendHelper.castDateToString(cellDate,
									    "yyyy-MM-dd HH:mm:ss");
									sqlQuery.append("TO_DATE('" + date
									    + "', 'yyyy-MM-dd HH24:MI:SS'), ");
								} else {
									value = String.format("'%s'", value);
									sqlQuery.append(value);
									sqlQuery.append(", ");
								}
							}

							break;

						default:
							break;
						}
					else
						sqlQuery.append("NULL, ");
				}
				sqlQuery.replace(sqlQuery.length() - 2, sqlQuery.length(), ")");
				if (log.isDebugEnabled())
					log.debug("SQL: " + sqlQuery);
				try {
					getCurrentSession().createSQLQuery(sqlQuery.toString()).executeUpdate();
					if (!sheetAndTableName.contains("_")) {
						// Wenn in Datentabelle (nicht Linktabelle) eingefuegt
						// wird, Eintrag in
						// Aenderungshistorie schreiben
						// updateHistoryLog(sheetAndTableName);
					}
				} catch (ConstraintViolationException e) {
					message = "Datensatz in der Datenbank vorhanden!  Bitte Daten überprüfen!";
					importMessages.add(new ImportMessage(Level.ERROR, message));
					log.error(message);
					return importMessages;
				} catch (SQLGrammarException e) {
					message = "SQL-Befehl konnte nicht ausgeführt werden: " + e.getCause();
					importMessages.add(new ImportMessage(Level.ERROR, message));
					log.error(message);
					return importMessages;
				} catch (RuntimeException e) {
					message = "Datenbank-Problem: " + e.getCause();
					importMessages.add(new ImportMessage(Level.ERROR, message));
					log.error(message);
					return importMessages;
					// throw e;
				}
				row = sheet.getRow(rowCount++);
				cellCount = 0;
			}
		} catch (IOException e) {
			message = "Excel-Problem: " + e.getCause();
			importMessages.add(new ImportMessage(Level.ERROR, message));
			log.error(message);
		}
		return importMessages;
	}

	public HSSFWorkbook createWorkbookTemplate(ImportTable importTable) {
		String sheetAndTableName = null;
		HSSFWorkbook workbook = null;
		if (importTable != null) {
			if (importTable.equals(ImportTable.MASSNAHMEN))
				sheetAndTableName = SAPMassnahme.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.GLEISSPERRUNGEN))
				sheetAndTableName = Gleissperrung.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.TOPPROJEKTE))
				sheetAndTableName = TopProjekt.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.BUENDEL))
				sheetAndTableName = Buendel.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.FAHRPLANREGELUNGEN))
				sheetAndTableName = Fahrplanregelung.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.UMLEITUNGEN))
				sheetAndTableName = Umleitung.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.UMLEITUNGSWEGE))
				sheetAndTableName = Umleitungsweg.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.PAKETE))
				sheetAndTableName = Paket.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.BUENDEL_FAHRPLANREGELUNGEN))
				sheetAndTableName = Buendel.class.getAnnotation(Table.class).name() + "_"
				    + Fahrplanregelung.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.TOPPROJEKTE_MASSNAHMEN))
				sheetAndTableName = TopProjekt.class.getAnnotation(Table.class).name() + "_"
				    + SAPMassnahme.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.UMLEITUNGEN_FAHRPLANREGELUNGEN))
				sheetAndTableName = Umleitung.class.getAnnotation(Table.class).name() + "_"
				    + Fahrplanregelung.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.UMLEITUNGEN_UMLEITUNGSWEGE))
				sheetAndTableName = Umleitung.class.getAnnotation(Table.class).name() + "_"
				    + Umleitungsweg.class.getAnnotation(Table.class).name();
			else if (importTable.equals(ImportTable.GLEISSPERRUNGEN_BUENDEL))
				sheetAndTableName = Gleissperrung.class.getAnnotation(Table.class).name() + "_"
				    + Buendel.class.getAnnotation(Table.class).name();

			if (sheetAndTableName != null) {
				ResultSet resultSet = null;
				sheetAndTableName = sheetAndTableName.toUpperCase();

				try {
					int cellCount = 0;
					workbook = new HSSFWorkbook();
					HSSFSheet sheet = workbook.createSheet(sheetAndTableName);
					HSSFRow row = sheet.createRow(0);
					HSSFCell cell = row.createCell(0);

					resultSet = EasyServiceFactory.getInstance().createSqlQueryService()
					    .findResultsByQuery(
					        "SELECT column_name AS FIELD FROM all_tab_columns WHERE owner = '"
					            + getTableOwner() + "' AND table_name = '" + sheetAndTableName
					            + "' ORDER BY column_id");

					while (resultSet.next()) {
						String heading = resultSet.getString("FIELD");
						if (!heading.toUpperCase().matches("ID|LASTCHANGEDATE|LASTCHANGEUSER")) {
							cell = row.createCell(cellCount++);
							cell.setCellValue(new HSSFRichTextString(heading));
						}
					}

					resultSet.close();
				} catch (Exception e) {
					workbook = null;
					log.error(e);

					try {
						if (resultSet != null)
							resultSet.close();
					} catch (SQLException ex) {
					}
				}
			}
		}

		return workbook;
	}

	public <T extends EasyPersistentObject> List<T> importTo(Class<T> clazz,
	    InputStream inputStream, TqmUser secUser) throws IOException {

		// FIXME: Der klägliche Versuch, mit Hibernate zu importieren
		@SuppressWarnings("unused")
		HSSFWorkbook hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));

		return null;
	}

	private boolean isEmptyRow(HSSFRow row, int spalten) {
		if (row != null)
			for (int i = 0; i <= spalten; i++)
				if (row.getCell(i) != null)
					return false;
		return true;
	}

	/**
	 * Liest den Benutzer aus dem Spring security context
	 * 
	 * @return the username or null
	 */
	private String getUserName() {
		SecurityService securityService = EasyServiceFactory.getInstance().createSecurityService();
		final User user = securityService.getCurrentUser();
		return user != null ? user.getUsername() : null;
	}
}