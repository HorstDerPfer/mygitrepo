package db.training.easy.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import db.training.logwrapper.Logger;

/*
 * 
 * 
 * Für die Ausführung wird die Konfiguration des Connection Pooling übergangen! 
 * 
 * 
 */
public class GenerateSchemaUpdate {

	private static Logger log = Logger.getLogger(GenerateSchemaUpdate.class);

	private static final boolean SCRIPT_TO_CONSOLE = true;

	private static final boolean UPDATE_DATABASE = false;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		long timer = System.nanoTime();

		Configuration cfg = new AnnotationConfiguration().configure("/hibernate.cfg.xml");

		// Konfiguration für Connection Pool löschen
		removeC3p0Configuration(cfg.getProperties());

		SchemaUpdate update = new SchemaUpdate(cfg);

		timer = (System.nanoTime() - timer) / 1000;
		log.debug(String.format("Konfiguration laden (%s)", timer));

		timer = System.nanoTime();
		log.debug(String.format("Start: SchemaUpdate (%s)", (timer / 1000)));
		update.execute(SCRIPT_TO_CONSOLE, UPDATE_DATABASE);
		timer = (System.nanoTime() - timer) / 1000;
		log.debug(String.format("Ende: SchemaUpdate (%s)", timer));

		List exceptions = update.getExceptions();
		if (exceptions != null && exceptions.size() > 0) {
			for (Iterator iter = exceptions.iterator(); iter.hasNext();) {
				Object ex = iter.next();
				System.out.print(ex.toString());
			}
		} else {
			log.debug("keine Exceptions");
		}
	}

	private static void removeC3p0Configuration(Properties properties) {
		Set<Object> delete = new HashSet<Object>();

		for (Object key : properties.keySet()) {
			if (key.toString().indexOf("c3p0") > -1
			    || key.toString().indexOf("connection.provider_class") > -1)
				delete.add(key);
		}
		for (Object key : delete)
			properties.remove(key);
	}
}
