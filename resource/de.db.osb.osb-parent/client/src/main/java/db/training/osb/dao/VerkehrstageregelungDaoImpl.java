package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Verkehrstageregelung;

public class VerkehrstageregelungDaoImpl extends BasicDaoImp<Verkehrstageregelung, Serializable>
    implements VerkehrstageregelungDao {

	public VerkehrstageregelungDaoImpl(SessionFactory instance) {
		super(Verkehrstageregelung.class, instance);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Verkehrstageregelung> findByVts(Integer vts) {
		return getCurrentSession().createCriteria(Verkehrstageregelung.class).add(
		    Restrictions.eq("vts", vts)).list();
	}
}