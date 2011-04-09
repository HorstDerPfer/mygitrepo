package db.training.easy.core.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import db.training.logwrapper.Logger;

/**
 * Sessionfactory fuer die ISADatenbank
 * 
 * @author hennebrueder
 * 
 */
public class ISASessionFactory {

	private static Logger log = Logger.getLogger(EasySessionFactory.class);

	private static SessionFactory sessionFactory;

	private static String CONFIG_FILE_LOCATION = "/hibernateISA.cfg.xml";

	// private static String CONFIG_FILE_LOCATION = "/hibernateISATest.cfg.xml";

	private static Configuration configuration = new AnnotationConfiguration();

	static {
		try {

			configuration.configure(CONFIG_FILE_LOCATION);
			if (log.isDebugEnabled())
				log.debug("ISA CONNECTION-PROVIDER: "
				    + configuration.getProperty("connection.provider_class"));

			sessionFactory = configuration.buildSessionFactory();

		} catch (Exception e) {
			System.err.println("%%%% Error Creating HibernateSessionFactory %%%%");
			e.printStackTrace();
			throw new HibernateException("Could not initialize the Hibernate configuration");
		}
	}

	public static SessionFactory getInstance() {
		return sessionFactory;
	}
}