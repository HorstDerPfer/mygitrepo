package db.training.bob.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.Massnahme;
import db.training.easy.common.BasicDaoImp;

public class MassnahmeDaoImpl extends BasicDaoImp<Massnahme, Serializable> implements MassnahmeDao {

	public MassnahmeDaoImpl(Class<Massnahme> clazz,	SessionFactory sessionFactory) {
		super(clazz, sessionFactory);
	}
	
	public MassnahmeDaoImpl(SessionFactory instance) {
		super(Massnahme.class, instance);
	}

	/**
	 * Finde alle Massnahme zu einer gegebenen Baumassnahme.
	 * Dazu suche alle Uebergabeblätter und dann deren Massnahmen. Wähle dabei die Massnahme mit der neusten Version.
	 * 
	 * @param bm_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findMassnahmenByBaumassnahmen(Integer bm_id){
		
		String mQueryString = "select ue.id, v.major, v.minor, m.id from uebergabeblatt ue, uebergabeblatt_massnahme ue_m, massnahme m, zvfversion v " +
		"where ue.id in (" +
		"select ue1.id from bm bm1, bm_uebergabeblatt bm_ue1, uebergabeblatt ue1 " +
		"where bm1.id = ? " +
		"and bm1.id = bm_ue1.bm_id " +
		"and ue1.id = bm_ue1.zvf_id) " +
		"and ue.id = ue_m.uebergabeblatt_id " +
		"and m.id = ue_m.massnahmen_id " +
		"and v.id = m.version_id " +
		"order by ue.id, v.major, v.minor desc";
		Query mQuery = getCurrentSession().createSQLQuery(mQueryString);
		mQuery.setInteger(0, bm_id);
		return mQuery.list();
	}
}
