package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import db.training.bob.dao.fplo.ISA_FploDao;
import db.training.bob.model.fplo.ISA_Fplo;
import db.training.easy.common.ISAServiceImpl;
import db.training.easy.core.dao.ISADaoFactory;

public class ISA_FploServiceImpl extends ISAServiceImpl<ISA_Fplo, Serializable> implements
    ISA_FploService {

	private ISA_FploDao dao = ISADaoFactory.getISA_FploDao();

	public ISA_FploServiceImpl() {
		super(ISA_Fplo.class);
		setBasicDao(dao);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findByFplo(String bob_vorgangsnr) {
		Transaction tx = null;
		List<Integer> objects = null;
		try {
			tx = getCurrentSession().beginTransaction();
			//TODO: SQL-Query muss fuer Migration ISA_* noch angepasst werden		
			Query q = getCurrentSession().createSQLQuery(
			    "select id_zug from tbl_fplo where fplo=" + bob_vorgangsnr);
			objects = q.list();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return objects;
	}
}
