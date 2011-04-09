package db.training.bob.dao.zvf;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import db.training.bob.model.zvf.Zug;
import db.training.bob.web.statistics.zvf.BbzrAbweichungZuegeNummernResultBean;
import db.training.easy.common.BasicDao;

public interface ZugDao extends BasicDao<Zug, Serializable> {

	public List<Object[]> findZugInfosByMassnahme(Integer massnahmeId);
	
	List<BbzrAbweichungZuegeNummernResultBean> findBbzrMassnahmenByBaumassnahmen(
	    Collection<Integer> baumassnahmenIds, Date verkehrstagVonZeitraum,
	    Date verkehrstagBisZeitraum, Integer qsKs);

}