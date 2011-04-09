package db.training.hibernate.history;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.CollectionEntry;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.AbstractCollectionEvent;
import org.hibernate.event.Initializable;
import org.hibernate.event.PostCollectionRecreateEvent;
import org.hibernate.event.PostCollectionRecreateEventListener;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreCollectionRemoveEvent;
import org.hibernate.event.PreCollectionRemoveEventListener;
import org.hibernate.event.PreCollectionUpdateEvent;
import org.hibernate.event.PreCollectionUpdateEventListener;
import org.hibernate.jdbc.Work;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.ClassUtils;
import db.training.security.SecurityService;
import db.training.security.User;

/**
 * @author hennebrueder Date: 17.06.2009
 * @param <T>
 */
public class HistoryEventListener implements PostDeleteEventListener, PostInsertEventListener,
    PostUpdateEventListener, PreCollectionRemoveEventListener, PreCollectionUpdateEventListener,
    PostCollectionRecreateEventListener, Initializable {

	private static final long serialVersionUID = 1L;

	final Logger logger = LoggerFactory.getLogger(HistoryEventListener.class);

	public HistoryEventListener() {
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

	private Serializable extractId(EntityPersister persister, Object entity) {
		Serializable entityId = null;
		if (persister.hasIdentifierProperty()) {
			entityId = persister.getIdentifier(entity, persister.guessEntityMode(entity));
		}
		return entityId;
	}

	@SuppressWarnings("unchecked")
	private List<HistoryLogEntry> processCollection(AbstractCollectionEvent event,
	    HistoryLogEntry.Command command) {

		PersistentCollection persistentCollection = event.getCollection();

		PersistenceContext persistenceContext = event.getSession().getPersistenceContext();
		CollectionEntry collectionEntry = persistenceContext
		    .getCollectionEntry(persistentCollection);
		if (collectionEntry == null) {
			logger.debug("Removal of collection of elements is not historized");
			return Collections.EMPTY_LIST;
		}

		List<HistoryLogEntry> entries = new ArrayList<HistoryLogEntry>();

		/**
		 * Entfernen von Eintraegen Vergleiche aktuelle Liste mit einem Snapshot
		 */
		if (persistentCollection.getValue() instanceof Collection) {
			Collection collection = (Collection) persistentCollection.getValue();
			if (command == HistoryLogEntry.Command.UPDATE()) {
				final Serializable snapshot = persistenceContext.getSnapshot(persistentCollection);
				// For unknown reason we might get the snapshot as Map even if the getValue is of
				// type Collection
				Collection snapshotCollection = null;
				if (snapshot instanceof Map) {
					snapshotCollection = (Collection) ((Map) snapshot).keySet();
				} else
					snapshotCollection = (Collection) snapshot;

				for (Object possibleDelete : snapshotCollection) {
					if (!collection.contains(possibleDelete)) {
						entries.add(createCollectionMapHistoryEntry(event, collectionEntry,
						    possibleDelete, null, HistoryLogEntry.Command.UPDATE(),
						    "Remove %s with id %s"));
					}
				}
			}
		} else if (persistentCollection.getValue() instanceof Map) {
			Map map = (Map) persistentCollection.getValue();
			if (command == HistoryLogEntry.Command.UPDATE()) {
				Map snapshot = (Map) persistenceContext.getSnapshot(persistentCollection);
				for (Object possibleDelete : snapshot.keySet()) {
					if (!map.containsKey(possibleDelete)) {
						entries.add(createCollectionMapHistoryEntry(event, collectionEntry,
						    possibleDelete, snapshot.get(possibleDelete), HistoryLogEntry.Command
						        .UPDATE(), "Remove key: %s, id %s value: %s, id %s"));
					}
				}
			}

		} else
			throw new IllegalStateException(String.format(
			    "Historisierung unterstuetzt den Datentyp %s nicht", persistentCollection
			        .getValue().getClass()));

		if (persistentCollection.getValue() instanceof Collection) {
			Collection collection = (Collection) persistentCollection.getValue();
			for (Object collectionElement : collection) {
				HistoryLogEntry logEntry = null;
				if (command == HistoryLogEntry.Command.INSERT()) {
					logEntry = createCollectionMapHistoryEntry(event, collectionEntry,
					    collectionElement, null, command, "Add %s with id %s");
					logger.debug("Inserting collection element {}", logEntry);

				} else if (command == HistoryLogEntry.Command.UPDATE()) {
					// inserts und updates
					Serializable snapshot = persistenceContext.getSnapshot(persistentCollection);
					boolean entryExists = false;
					if (snapshot instanceof Collection)
						entryExists = ((Collection) snapshot).contains(collectionElement);
					else if (snapshot instanceof Map)
						entryExists = ((Map) snapshot).containsKey(collectionElement);
					if (entryExists) {
						logger.debug("Leave collection element unchanged {}", collectionElement);
					} else {
						logEntry = createCollectionMapHistoryEntry(event, collectionEntry,
						    collectionElement, null, command, "Add %s with id %s");
						logger.debug("Add collection element {}", logEntry);
					}

				} else if (command == HistoryLogEntry.Command.DELETE()) {
					logEntry = createCollectionMapHistoryEntry(event, collectionEntry,
					    collectionElement, null, HistoryLogEntry.Command.UPDATE(),
					    "Remove %s with id %s");
					logger.debug("Recreating collection element {}", logEntry);

				}
				if (logEntry != null)
					entries.add(logEntry);

			}
		} else if (persistentCollection.getValue() instanceof Map) {
			Map map = (Map) persistentCollection.getValue();
			for (Object key : map.keySet()) {
				HistoryLogEntry logEntry = null;
				if (command == HistoryLogEntry.Command.INSERT()) {
					logEntry = createCollectionMapHistoryEntry(event, collectionEntry, key, map
					    .get(key), command, "Add key: %s, id %s value: %s, id %s");
					logger.debug("Inserting collection element {}", logEntry);

				} else if (command == HistoryLogEntry.Command.UPDATE()) {
					// inserts und updates
					Map snapshot = (Map) persistenceContext.getSnapshot(persistentCollection);
					if (snapshot.containsKey(key)) {
						logger.debug("Leave collection element unchanged {}", key);
					} else {
						logEntry = createCollectionMapHistoryEntry(event, collectionEntry, key, map
						    .get(key), command, "Add key: %s, id %s value: %s, id %s");
						logger.debug("Add collection element {}", logEntry);
					}

				} else if (command == HistoryLogEntry.Command.DELETE()) {
					logEntry = createCollectionMapHistoryEntry(event, collectionEntry, key, map
					    .get(key), HistoryLogEntry.Command.UPDATE(),
					    "Remove key: %s, id %s value: %s, id %s");
					logger.debug("Recreating collection element {}", logEntry);

				}
				if (logEntry != null)
					entries.add(logEntry);

			}
		}

		return entries;
	}

	/**
	 * Creates an entry for the history event
	 * 
	 * @param event
	 *            - the event created by Hibernate
	 * @param collectionEntry
	 *            - the changed element as collectionEntry
	 * @param entity
	 *            - an instance of the changed entity or value type
	 * @param command
	 *            - command type
	 * @param message
	 *            - the message to be saved, add, remove, update
	 * @param mapValueEntity
	 *            - if we deal with a map then this is the value
	 * @return a new log entry to be saved
	 */
	private HistoryLogEntry createCollectionMapHistoryEntry(AbstractCollectionEvent event,
	    CollectionEntry collectionEntry, Object entity, Object mapValueEntity,
	    HistoryLogEntry.Command command, String message) {
		Serializable ownerEntityId = event.getAffectedOwnerIdOrNull();
		final String ownerEntityName = event.getAffectedOwnerOrNull().getClass().getSimpleName();
		final Date transTime = new Date();
		String userName = getUserName();
		String propertyname = collectionEntry.getRole();
		int pos = propertyname.lastIndexOf('.');
		if (pos != -1)
			propertyname = propertyname.substring(pos + 1);

		String entityName = event.getSession().guessEntityName(entity);

		String idOrValue;
		try {
			idOrValue = extractId(event.getSession().getEntityPersister(entityName, entity), entity)
			    .toString();
		} catch (MappingException e) {
			// value is a simple type
			idOrValue = entity.toString();
		}

		if (mapValueEntity != null) { //
			String mapValueEntityName = event.getSession().guessEntityName(entity);

			String mapValueIdValue;
			try {
				mapValueIdValue = extractId(
				    event.getSession().getEntityPersister(mapValueEntityName, entity), entity)
				    .toString();
			} catch (MappingException e) {
				mapValueIdValue = mapValueEntity.toString();
				// value is a simple type
			}
			return new HistoryLogEntry(ownerEntityId.toString(), ownerEntityName, propertyname,
			    command, userName, transTime, null, String.format(message, entity.getClass()
			        .getSimpleName(), idOrValue, mapValueEntity.getClass().getSimpleName(),
			        mapValueIdValue));
		} else {
			return new HistoryLogEntry(ownerEntityId.toString(), ownerEntityName, propertyname,
			    command, userName, transTime, null, String.format(message, entity.getClass()
			        .getSimpleName(), idOrValue));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public final void initialize(final Configuration cfg) {
		//
	}

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		if (!(event.getAffectedOwnerOrNull() instanceof Historizable))
			return;

		List<HistoryLogEntry> entries = processCollection(event, HistoryLogEntry.Command.DELETE());
		SessionFactory factory = event.getSession().getFactory();
		saveHistoryEntry(factory, event.getSession(), entries);
	}

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
		if (!(event.getAffectedOwnerOrNull() instanceof Historizable))
			return;

		List<HistoryLogEntry> entries = processCollection(event, HistoryLogEntry.Command.UPDATE());
		SessionFactory factory = event.getSession().getFactory();
		saveHistoryEntry(factory, event.getSession(), entries);
	}

	/**
	 * This code can break, if the entity has a different name than its class name
	 * 
	 * @param event
	 *            - the Hibernate event
	 */
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		if (!(event.getAffectedOwnerOrNull() instanceof Historizable))
			return;

		List<HistoryLogEntry> entries = processCollection(event, HistoryLogEntry.Command.INSERT());
		SessionFactory factory = event.getSession().getFactory();
		saveHistoryEntry(factory, event.getSession(), entries);
	}

	/**
	 * Log deletions made to the current model in the the Audit Trail.
	 * 
	 * @param event
	 *            the pre-deletion event
	 */
	// @Override
	public void onPostDelete(PostDeleteEvent event) {
		if (!(event.getEntity() instanceof Historizable))
			return;

		String userName = getUserName();
		Serializable entityId = extractId(event.getPersister(), event.getEntity());

		final String entityName = event.getEntity().getClass().getSimpleName();
		final Date transTime = new Date();

		HistoryLogEntry logEntry = new HistoryLogEntry(entityId.toString(), entityName, null,
		    HistoryLogEntry.Command.DELETE(), userName, transTime, ClassUtils.printProperties(event
		        .getEntity(), event.getPersister().getPropertyNames()), null);
		SessionFactory factory = event.getPersister().getFactory();
		logger.debug("Deleting {}", logEntry);

		saveHistoryEntry(factory, event.getSession(), Arrays.asList(logEntry));
	}

	public void onPostInsert(PostInsertEvent event) {
		if (!(event.getEntity() instanceof Historizable))
			return;

		String userName = getUserName();
		Serializable entityId = extractId(event.getPersister(), event.getEntity());
		final String entityName = event.getEntity().getClass().getSimpleName();
		final Date transTime = new Date(); // new Date(event.getSource().getTimestamp());
		final EntityMode entityMode = event.getPersister().guessEntityMode(event.getEntity());
		Object newPropValue;

		List<HistoryLogEntry> entries = new ArrayList<HistoryLogEntry>();
		for (String propertyName : event.getPersister().getPropertyNames()) {
			newPropValue = event.getPersister().getPropertyValue(event.getEntity(), propertyName,
			    entityMode);
			// because we are performing an insert we only need to be concerned will non-null values
			if (newPropValue != null) {
				// collections will fire their own events
				if (!(newPropValue instanceof Collection<?>)
				    && !(newPropValue instanceof Map<?, ?>)) {
					HistoryLogEntry logEntry = new HistoryLogEntry(entityId.toString(), entityName,
					    propertyName, HistoryLogEntry.Command.INSERT(), userName, transTime, null,
					    ClassUtils.printProperties(newPropValue, event.getPersister()
					        .getPropertyNames()));
					logger.debug("Inserting {}", logEntry);
					entries.add(logEntry);
				}
			}
		}
		SessionFactory factory = event.getPersister().getFactory();

		saveHistoryEntry(factory, event.getSession(), entries);
	}

	/**
	 * Log updates made to the current model in the the Audit Trail.
	 * 
	 * @param event
	 *            the pre-update event
	 */
	// @Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (!(event.getEntity() instanceof Historizable))
			return;
		String userName = getUserName();
		Serializable entityId = extractId(event.getPersister(), event.getEntity());

		final String entityName = event.getEntity().getClass().getSimpleName();
		final Date transTime = new Date();
		EntityPersister persister = event.getPersister();
		final EntityMode entityMode = persister.guessEntityMode(event.getEntity());
		List<HistoryLogEntry> entries = new ArrayList<HistoryLogEntry>();

		// get the loaded state
		Object[] loadedState = event.getOldState();
		org.hibernate.classic.Session session = null;
		try {
			if (loadedState == null) {
				session = event.getSession().getSessionFactory().openSession();
				Object loaded = session.get(event.getEntity().getClass(), entityId);
				SessionImplementor imp = (SessionImplementor) session;
				EntityEntry entityEntry = imp.getPersistenceContext().getEntry(loaded);
				if (entityEntry != null)
					loadedState = entityEntry.getLoadedState();
			}
			// for unknown reason, we can get an update here but don't get a loaded case. In that
			// case, we abort.
			if (loadedState == null)
				return;

			// cycle through property names, extract corresponding property values and insert new
			// entry
			// in audit trail

			String[] propertyNames = persister.getPropertyNames();

			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				Object newPropValue = persister.getPropertyValue(event.getEntity(), propertyName,
				    entityMode);
				// because we are performing an insert we only need to be concerned will non-null
				// values
				if (newPropValue != null) {
					Object oldPropValue = loadedState[i];

					// collections will fire their own events
					boolean skipLogEntry = false;
					if ((newPropValue instanceof Collection<?>)
					    || (newPropValue instanceof Map<?, ?>))
						skipLogEntry = true;

					if ((newPropValue instanceof HibernateProxy)
					    && !Hibernate.isInitialized(newPropValue))
						skipLogEntry = true;

					if (!skipLogEntry) {
						HistoryLogEntry logEntry;
						if (newPropValue instanceof EasyPersistentObject) {
							EasyPersistentObject newEntityValue = (EasyPersistentObject) newPropValue;
							EasyPersistentObject oldEntityValue = (EasyPersistentObject) oldPropValue;
							if (oldEntityValue == null
							    || !oldEntityValue.getId().equals(newEntityValue.getId())) {

								logEntry = new HistoryLogEntry(entityId.toString(), entityName,
								    propertyName, HistoryLogEntry.Command.UPDATE(), userName,
								    transTime, ClassUtils.printProperties(oldEntityValue, null),
								    ClassUtils.printProperties(newEntityValue, null));
								logger.debug("Updating {}", logEntry);
								entries.add(logEntry);
							}
						} else if (!newPropValue.equals(oldPropValue)) {
							logEntry = new HistoryLogEntry(entityId.toString(), entityName,
							    propertyName, HistoryLogEntry.Command.UPDATE(), userName,
							    transTime, oldPropValue != null ? oldPropValue.toString() : null,
							    newPropValue.toString());
							logger.debug("Updating {}", logEntry);
							entries.add(logEntry);
						}

					}
				}
			}
		} finally {
			if (session != null)
				session.close();
		}

		// need to have a separate session for audit save

		saveHistoryEntry(persister.getFactory(), event.getSession(), entries);
	}

	private void saveHistoryEntry(final SessionFactory factory, final Session session,
	    final List<HistoryLogEntry> logEntries) {
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				StatelessSession statelessSession = null;
				try {
					statelessSession = factory.openStatelessSession(connection);
					for (HistoryLogEntry logEntry : logEntries) {
						statelessSession.insert(logEntry);
					}
				} catch (RuntimeException e) {
					StringBuilder builder = new StringBuilder();
					for (HistoryLogEntry logEntry : logEntries) {
						builder.append(logEntry).append("\n");
					}
					logger.error("Error saving history entries for the following logEntries\n{}",
					    builder.toString());
				} finally {
					if (statelessSession != null)
						statelessSession.close();
				}
			}
		});
	}
}