package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;
import db.training.osb.dao.StreckenbandZeileDao;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;
import db.training.osb.model.VzgStrecke;

/**
 * Das Streckenband ist eine sortierte Liste von StreckenbandZeile-Objekten die in der Datenbank
 * liegen. Das DAO ist deshalb nicht StreckenbandDao sondern StreckenbandZeileDao. Die
 * Servicemethoden operieren aber auf einer Menge von StreckenbandZeilen.
 * 
 * @author michels
 * 
 */
public class StreckenbandServiceImpl extends EasyServiceImpl<StreckenbandZeile, Serializable>
    implements StreckenbandService {

	private static Logger log = Logger.getLogger(StreckenbandServiceImpl.class);

	public StreckenbandServiceImpl() {
		super(StreckenbandZeile.class);
	}

	public StreckenbandZeileDao getDao() {
		return (StreckenbandZeileDao) getBasicDao();
	}

	@Transactional
	public Streckenband findByVzgStrecke(VzgStrecke streckeVzg, int fahrplanjahr, Preload[] preloads)
	    throws IllegalArgumentException {
		if (streckeVzg == null) {
			throw new IllegalArgumentException("streckeVzg darf nicht NULL sein.");
		}

		return findByVzgStrecke(streckeVzg.getNummer(), fahrplanjahr, preloads);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Streckenband findByVzgStrecke(Integer streckennummer, int fahrplanjahr,
	    Preload[] preloads) throws IllegalArgumentException, NoResultException {
		if (streckennummer == null) {
			throw new IllegalArgumentException("streckennummer darf nicht NULL oder 0 sein.");
		}

		Streckenband result = null;

		try {
			//
			// Streckenband laden
			//
			if (log.isDebugEnabled())
				log.debug("Streckenband laden. Nr: " + streckennummer + "; FplJahr: "
				    + fahrplanjahr);

			Criteria criteria = getCurrentSession().createCriteria(StreckenbandZeile.class)
			    .add(Restrictions.eq("fahrplanjahr", fahrplanjahr))
			    .createAlias("strecke", "st", Criteria.INNER_JOIN)
			    .add(Restrictions.eq("st.nummer", streckennummer)).addOrder(Order.asc("rowNum"));

			PreloadEventListener.setPreloads(preloads);
			result = new Streckenband(criteria.list());
			PreloadEventListener.clearPreloads();

			//
			// Wenn kein Streckenband gefunden wurde: Streckenband neu initialisieren
			//
			if (result == null || result.size() == 0) {
				if (log.isDebugEnabled())
					log.debug("Start generierung streckenband_zeile");

				// Maßnahmen mit VZG Strecke auflisten
				Query qry = getCurrentSession().getNamedQuery("streckenband");
				qry.setInteger("strecke", streckennummer);
				qry.setInteger("fahrplanjahr", fahrplanjahr);
				qry.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

				PreloadEventListener.setPreloads(preloads);
				List<Gleissperrung> gleissperrungenList = qry.list();
				PreloadEventListener.clearPreloads();

				if (log.isDebugEnabled() && gleissperrungenList != null)
					log.debug("Neu generierte gleissperrungen liste:" + gleissperrungenList.size());

				VzgStrecke streckeVzg = EasyServiceFactory.getInstance().createVzgStreckeService()
				    .findByNummer(streckennummer, fahrplanjahr, null);

				if (streckeVzg == null) {
					throw new NoResultException("VzG-Strecke nicht gefunden: Nr=" + streckennummer);
				}

				result = new Streckenband(gleissperrungenList.size());

				// Streckenband erstellen
				for (ListIterator<Gleissperrung> glIterator = gleissperrungenList.listIterator(); glIterator
				    .hasNext();) {
					int index = glIterator.nextIndex();
					Gleissperrung g = glIterator.next();
					StreckenbandZeile row = new StreckenbandZeile();
					row.setGleissperrung(g);
					row.setStrecke(streckeVzg);
					row.setRowNum(index);
					row.setFahrplanjahr(fahrplanjahr);
					result.add(row);
				}

				// Streckenband neu schreiben
				delete(streckennummer, fahrplanjahr);
				getDao().saveAll(result);

				if (log.isDebugEnabled())
					log.debug("Ende generierung streckenband_zeile");
			}
		} catch (HibernateException ex) {
			if (log.isErrorEnabled())
				log.error("Fehler beim Laden des Streckenbandes: ", ex);

		}

		return result;
	}

	@Transactional
	public Streckenband findByZeileId(Integer rowId, int fahrplanjahr)
	    throws IllegalArgumentException {
		if (rowId == null) {
			throw new IllegalArgumentException("rowId kann nicht NULL sein.");
		}

		Streckenband sb = null;

		StreckenbandZeile searchRow = getDao().findById(rowId);
		Criteria criteria = getCurrentSession().createCriteria(StreckenbandZeile.class);
		criteria.add(Restrictions.eq("strecke", searchRow.getStrecke()));
		criteria.addOrder(Order.asc("rowNum"));
		criteria.add(Restrictions.eq("fahrplanjahr", fahrplanjahr));
		List<StreckenbandZeile> resultList = getDao().findByCriteria(criteria);

		sb = new Streckenband(resultList);
		return sb;
	}

	public Streckenband moveRow(Integer rowId, SortDirection direction, int fahrplanjahr) {
		Streckenband sb = findByZeileId(rowId, fahrplanjahr);

		return moveRow(sb, rowId, direction);
	}

	public Streckenband moveRow(Streckenband sb, Integer rowId, SortDirection direction) {
		if (sb == null) {
			return null;
		}

		boolean hasChanges = false;

		try {
			// durch StreckenbandZeilen iterieren bis rowId gefunden ist, und dann die Zeile mit
			// Vorgänger/Nachfolger (abhängig von direction) tauschen
			for (ListIterator<StreckenbandZeile> itRow = sb.listIterator(); itRow.hasNext()
			    && !hasChanges;) {
				int index = itRow.nextIndex();
				StreckenbandZeile row = itRow.next();

				if (row.getId() == rowId) {
					if (direction == SortDirection.MOVE_UP) {
						swapRows(sb, index, index - 1);
					} else if (direction == SortDirection.MOVE_DOWN) {
						swapRows(sb, index, index + 1);
					}
					hasChanges = true;
				}
			}

			if (hasChanges) {
				getDao().saveAll(sb);
			}
		} catch (IndexOutOfBoundsException ex) {
			// nix passiert;
		}
		return sb;
	}

	/**
	 * verändert die Reihenfolge der Zeilen im Streckenband durch tauschen der Zeilennummern.
	 * 
	 * IndexOutOfBoundsException - if either i or j is out of range (i < 0 || i >= list.size() || j
	 * < 0 || j >= list.size())
	 * 
	 * @param sb
	 *            Streckenband
	 * @param i
	 *            Index eines zu tauschenden Elements
	 * @param j
	 *            Index des anderen zu tauschenden Elements
	 */
	private static void swapRows(Streckenband sb, int i, int j) {
		if (i < 0 || i >= sb.size() || j < 0 || j >= sb.size()) {
			throw new IndexOutOfBoundsException();
		}
		StreckenbandZeile itemI = sb.get(i);
		StreckenbandZeile itemJ = sb.get(j);
		int tempRowNum = itemI.getRowNum();
		itemI.setRowNum(itemJ.getRowNum());
		itemJ.setRowNum(tempRowNum);
	}

	@Transactional(readOnly = false)
	public void update(Streckenband list) {
		if (log.isDebugEnabled())
			log.debug("update(" + list + ")");
		getDao().saveAll(list);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Collection<String> findGewerkeOnStreckenband(VzgStrecke streckeVzg, int fahrplanjahr) {
		Collection<String> result = null;

		Query qry = getCurrentSession().createQuery(
		    "select distinct(mn.gewerk) from SAPMassnahme mn where fahrplanjahr = :jahr");
		qry.setInteger("jahr", fahrplanjahr);

		result = qry.list();

		return result;
	}

	@Transactional(readOnly = false)
	public void delete(Integer streckennummer, int fahrplanjahr) {
		if (streckennummer == null)
			return;

		Query idQry = getCurrentSession()
		    .createQuery(
		        "SELECT sbz.id FROM StreckenbandZeile sbz WHERE sbz.fahrplanjahr = :fpljahr AND sbz.strecke.nummer = :streckennummer");
		idQry.setInteger("fpljahr", fahrplanjahr);
		idQry.setInteger("streckennummer", streckennummer);
		List<?> ids = idQry.list();

		if (ids != null && !ids.isEmpty()) {
			Query qry = getCurrentSession().createQuery(
			    "DELETE StreckenbandZeile WHERE id in (:ids)");
			qry.setParameterList("ids", ids);
			int rowCount = qry.executeUpdate();
			if (log.isDebugEnabled()) {
				log.debug("Streckenband Nr: " + streckennummer + " gelöscht. Anzahl Zeilen: "
				    + rowCount);
			}
		}
	}
}