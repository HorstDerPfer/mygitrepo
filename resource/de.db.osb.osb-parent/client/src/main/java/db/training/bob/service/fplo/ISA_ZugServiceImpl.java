package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import db.training.bob.dao.fplo.ISA_ZugDao;
import db.training.bob.model.fplo.ISA_Fplo;
import db.training.bob.model.fplo.ISA_Zug;
import db.training.easy.common.ISAServiceImpl;
import db.training.easy.core.dao.ISADaoFactory;

public class ISA_ZugServiceImpl extends ISAServiceImpl<ISA_Zug, Serializable> implements
    ISA_ZugService {

	private ISA_ZugDao dao = ISADaoFactory.getISA_ZugDao();

	public ISA_ZugServiceImpl() {
		super(ISA_Zug.class);
		setBasicDao(dao);
	}

	@SuppressWarnings("unchecked")
	public List<ISA_Zug> findByVorgangsnr(Integer vorgangsnr, Integer regionalbereichId) {
		Transaction tx = null;
		List<ISA_Zug> objects = new ArrayList<ISA_Zug>();
		try {
			tx = getCurrentSession().beginTransaction();
			DetachedCriteria subselect = DetachedCriteria.forClass(ISA_Fplo.class).setProjection(
			    Projections.property("idZug")).add(Restrictions.eq("fplo", vorgangsnr.toString()));

			Criteria criteria = getCurrentSession().createCriteria(ISA_Zug.class);
			criteria.createAlias("bestellung", "b");
			criteria.add(Restrictions.eq("b.nl", regionalbereichId));
			criteria.add(Restrictions.eq("b.teamnr", 2));
			criteria.add(Property.forName("idZug").in(subselect));
			criteria.add(Restrictions.eq("b2", false));
			criteria.add(Restrictions.eq("fertig", true));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			objects = (List<ISA_Zug>) criteria.list();

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return objects;
	}
}
