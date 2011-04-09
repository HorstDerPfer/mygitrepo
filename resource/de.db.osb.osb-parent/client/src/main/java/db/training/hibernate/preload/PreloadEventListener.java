package db.training.hibernate.preload;

import org.hibernate.Hibernate;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.def.DefaultPostLoadEventListener;

import db.training.bob.service.FetchPlan;

/**
 * lädt einzelne Lazy-Properties nach (vgl. {@link FetchPlan})
 * 
 * Die zu ladenden Properties müssen vor der Datenbankabfrage an den PreloadEventListener übergeben
 * werden. Nach Abschluß der Abfrage sind die Fetch-Pläne wieder zu löschen.
 * 
 * Verwendung: <code>
 * PreloadEventListener.setPreloads(preloads);
 * result = crit.list();
 * PreloadEventListener.clearPreloads();
 * </code>
 * 
 * Code von {@link http://entwickler-forum.de/showthread.php?t=47067} bzw. Java-Magazin 4.08
 * 
 * @author michels
 * 
 */
public class PreloadEventListener extends DefaultPostLoadEventListener {

	// private static Logger log = Logger.getLogger(PreloadEventListener.class);

	private static final long serialVersionUID = 4300933096237342726L;

	private static ThreadLocal<Preload[]> preloadsThreadLocal = new ThreadLocal<Preload[]>();

	public static void setPreloads(Preload[] preloads) {
		preloadsThreadLocal.set(preloads);
	}

	public static void clearPreloads() {
		preloadsThreadLocal.set(null);
	}

	protected Object callGetter(Object entity, Preload preload) {
		try {
			return preload.invokeGetter(entity);
		} catch (Exception ex) {
			throw new RuntimeException(
			    "Can't invoke getter for property: " + preload.getProperty(), ex);
		}
	}

	@Override
	public void onPostLoad(PostLoadEvent event) {
		Object entity = event.getEntity();
		Preload[] preloads = preloadsThreadLocal.get();
		if (preloads != null) {
			for (Preload preload : preloads) {
				if (preload.getModelClass().isInstance(entity)) {
					Object getterResult = callGetter(entity, preload);
					Hibernate.initialize(getterResult);
				}
			}
		}
		super.onPostLoad(event);
	}
}