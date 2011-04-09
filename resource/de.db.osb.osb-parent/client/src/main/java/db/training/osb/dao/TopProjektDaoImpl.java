package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;

public class TopProjektDaoImpl extends BasicDaoImp<TopProjekt, Serializable> implements
    TopProjektDao {

	public TopProjektDaoImpl(SessionFactory instance) {
		super(TopProjekt.class, instance);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<TopProjekt> findByVzgStrecke(VzgStrecke vzgStrecke) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t from TopProjekt t ");
		sql.append("left join t.massnahmen m ");
		sql.append("where m.hauptStrecke = :vzgStrecke");
		return getCurrentSession().createQuery(sql.toString()).setParameter("vzgStrecke",
		    vzgStrecke).list();
	}
}