package db.training.easy.core;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GenerateSchema {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		boolean exportToConsole = true;

		// Gefaehrlich bei remote verbundener Datenbank! Bitte nur mit Wert "false" comitten.
		boolean exportToDatabase = false;

		SchemaExport export = new SchemaExport(new AnnotationConfiguration()
		    .configure("/hibernate.cfg.xml"));
		export.setDelimiter(";");
		export.create(exportToConsole, exportToDatabase);

	}

}
