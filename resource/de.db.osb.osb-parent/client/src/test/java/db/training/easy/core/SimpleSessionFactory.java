package db.training.easy.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * a simple Sessionfactory for easy db. Should be used for junit tests only.
 * 
 * @author hennebrueder
 * 
 */
public class SimpleSessionFactory {

    private static SessionFactory sessionFactory;

    static {
	AnnotationConfiguration annotatedConfig = new AnnotationConfiguration();
	Configuration config = annotatedConfig
		.configure("/hibernateISA.cfg.xml");
	sessionFactory = config.buildSessionFactory();
    }

    public static SessionFactory getInstance() {
	return sessionFactory;
    }
}
