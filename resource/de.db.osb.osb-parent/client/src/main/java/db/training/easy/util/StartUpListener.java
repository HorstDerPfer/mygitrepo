package db.training.easy.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import db.training.easy.core.dao.EasyDaoFactory;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import de.dbsystems.kolt.talo.CoreCatalog;
import de.dbsystems.kolt.talo.TaloLog;
import de.dbsystems.kolt.talo.TaloLogFactory;

/**
 * Ist im wesentlichen eine Kopie des ContextLoaderListener des Spring framework. Fuegt nur die
 * Initialisierung der EasySessionFActory hinzu.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class StartUpListener implements ServletContextListener {

	private static TaloLog talo = TaloLogFactory.getLog(StartUpListener.class);

	private ContextLoader contextLoader;

	/**
	 * Initialize the root web application context.
	 */
	public void contextInitialized(ServletContextEvent event) {

		talo.log(CoreCatalog.infoApplicationStarting());

		this.contextLoader = createContextLoader();
		WebApplicationContext initWebApplicationContext = this.contextLoader
		    .initWebApplicationContext(event.getServletContext());

		Logger log = Logger.getLogger(getClass());
		if (log.isDebugEnabled())
			log.debug("Start initializing EasyServiceFactory");
		EasyServiceFactory easyServiceFactory = EasyServiceFactory.getInstance();
		easyServiceFactory.setWebApplicationContext(initWebApplicationContext);
		EasySessionFactory.setWebApplicationContext(initWebApplicationContext);
		EasyDaoFactory.getInstance().setWebApplicationContext(initWebApplicationContext);

		if (log.isDebugEnabled())
			log.debug("Finished initializing EasyServiceFactory");

		talo.log(CoreCatalog.infoApplicationStarted());
	}

	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * 
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	/**
	 * Return the ContextLoader used by this listener.
	 * 
	 * @return the current ContextLoader
	 */
	public ContextLoader getContextLoader() {
		return this.contextLoader;
	}

	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		if (this.contextLoader != null) {
			talo.log(CoreCatalog.infoApplicationStopping(null));
			this.contextLoader.closeWebApplicationContext(event.getServletContext());
		}

		talo.log(CoreCatalog.infoApplicationStopped(null));
	}
}
