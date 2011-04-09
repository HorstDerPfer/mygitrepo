package db.training.bob.dao.zvf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.Zug;
import db.training.bob.web.statistics.zvf.BbzrAbweichungZuegeNummernResultBean;
import db.training.easy.common.BasicDaoImp;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;

public class ZugDaoImpl extends BasicDaoImp<Zug, Serializable> implements ZugDao {
	
	private Logger logger = Logger.getLogger(ZugDaoImpl.class);

	public ZugDaoImpl(SessionFactory instance) {
		super(Zug.class, instance);
	}
	
	/**
	 * Gib eine Liste von Zug-Ids zu einer gegebenen Massnahme zurück.
	 * 
	 * @param massnahmeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findZugInfosByMassnahme(Integer massnahmeId){
		List<Object[]> result = new ArrayList<Object[]>();
		
		String queryString = "select distinct z.id, z.verspaetung, z.verkehrstag, z.qs_ks, z.betreiber, m.festgelegt_sgv, m.festgelegt_spfv, m.festgelegt_spnv from massnahme m, massnahme_zug m_z, zug z " +
		"where m.id = m_z.massnahme_id " +
		"and z.id = m_z.zug_id " +
		"and z.art = 0 " +
		"and z.verspaetung > 0" +
		"and m.id = ? "; ;
		Query query = getCurrentSession().createSQLQuery(queryString);
		
		query.setInteger(0, massnahmeId);
		result.addAll(query.list());
		
		return result;
	}
	
	@Override
	public List<BbzrAbweichungZuegeNummernResultBean> findBbzrMassnahmenByBaumassnahmen(
	    Collection<Integer> baumassnahmenIds, Date verkehrstagVonZeitraum,
	    Date verkehrstagBisZeitraum, Integer qsKs) {

		if (baumassnahmenIds == null || baumassnahmenIds.size() == 0) {
			return new ArrayList<BbzrAbweichungZuegeNummernResultBean>();
		}

		PreloadEventListener.clearPreloads();

		Query qry = getCurrentSession().getNamedQuery("findBbzrZuegeByBaumassnahmen_ResultBean");
		qry.setParameterList("baumassnahmenIds", baumassnahmenIds, Hibernate.INTEGER);

		if (verkehrstagVonZeitraum != null) {
			qry.setDate("verkehrstagVonZeitraum", verkehrstagVonZeitraum);
			qry.setInteger("verkehrstagVonUnused", 0);
		} else {
			qry.setDate("verkehrstagVonZeitraum", new Date());
			qry.setInteger("verkehrstagVonUnused", 1);
		}

		if (verkehrstagBisZeitraum != null) {
			qry.setDate("verkehrstagBisZeitraum", verkehrstagBisZeitraum);
			qry.setInteger("verkehrstagBisUnused", 0);
		} else {
			qry.setDate("verkehrstagBisZeitraum", new Date());
			qry.setInteger("verkehrstagBisUnused", 1);
		}

		// qry.setResultTransformer(new AliasToBeanResultTransformer(
		// BbzrAbweichungZuegeNummernResultBean.class));

		// FIXME: Die Abfrage liefert weniger Ergebniszeilen zurück als das Ausführen des Statements
		// mit gleichen Einschränkungen im Sql Developer

		// anstelle von ResultTransformer das ResultSet selbst einlesen
		ScrollableResults results = qry.scroll(ScrollMode.FORWARD_ONLY);
		List<BbzrAbweichungZuegeNummernResultBean> rows = new ArrayList<BbzrAbweichungZuegeNummernResultBean>();
		int count = 0;
		while (results.next()) {
			BbzrAbweichungZuegeNummernResultBean bean = new BbzrAbweichungZuegeNummernResultBean();
			bean.setFestgelegtSgv(results.getBoolean(0));
			bean.setFestgelegtSpfv(results.getBoolean(1));
			bean.setFestgelegtSpnv(results.getBoolean(2));
			bean.setBetreiber(results.getString(3));
			bean.setZugNr(results.getString(4));
			bean.setVerspaetung(results.getBoolean(5));
			bean.setUmleitung(results.getBoolean(6));
			bean.setAusfall(results.getBoolean(7));
			bean.setVorplan(results.getBoolean(8));
			bean.setGesperrt(results.getBoolean(9));
			bean.setErsatzhalte(results.getBoolean(10));
			bean.setRegelung(results.getBoolean(11));

			rows.add(bean);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Anzahl Ergebnisse von findBbzrZuegeByBaumassnahmen_ResultBean: "
			    + rows.size() + " Count: " + count);
		}

		return rows;
	}
}