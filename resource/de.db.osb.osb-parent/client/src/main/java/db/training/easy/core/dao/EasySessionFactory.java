package db.training.easy.core.dao;

import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;

/**
 * Sessionfactory fuer die EasyDatenbank
 * 
 * @author hennebrueder
 * 
 */
public class EasySessionFactory {

	// private static Logger log = Logger.getLogger(EasySessionFactory.class);

	// private SessionFactory sessionFactory;

	// private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";

	// private Configuration configuration = new AnnotationConfiguration();

	private static WebApplicationContext wac;

	public static void setWebApplicationContext(WebApplicationContext ctx) {
		wac = ctx;
	}

	public static WebApplicationContext getWebApplicationContext() {
		return wac;
	}

	public static SessionFactory getInstance() {
		return (SessionFactory) getWebApplicationContext().getBean("hibernateSessionFactory");
	}

}