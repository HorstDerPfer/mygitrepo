package db.training.bob.service.zvf;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.zvf.BBPStreckeDao;
import db.training.bob.model.zvf.BBPStrecke;
import db.training.easy.common.EasyServiceImpl;

public class BBPStreckeServiceImpl extends EasyServiceImpl<BBPStrecke, Serializable> implements
    BBPStreckeService {

	public BBPStreckeServiceImpl() {
		super(BBPStrecke.class);
	}

	public BBPStreckeDao getDao() {
		return (BBPStreckeDao) getBasicDao();
	}

	@Transactional
	public BBPStrecke findByNummer(int nummer) {
		BBPStrecke result = null;

		Criteria criteria = getCurrentSession().createCriteria(BBPStrecke.class);
		criteria.add(Restrictions.eq("nummer", nummer));

		result = getDao().findUniqueByCriteria(criteria);

		return result;
	}
}