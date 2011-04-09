package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.SearchBean;
import db.training.bob.model.zvf.Zug;
import db.training.bob.web.statistics.zvf.BbzrAbweichungZuegeNummernResultBean;
import db.training.easy.common.BasicService;
import db.training.hibernate.preload.Preload;

public interface ZugService extends BasicService<Zug, Serializable> {

	List<BbzrAbweichungZuegeNummernResultBean> findBbzrMassnahmenByBaumassnahmen(
	    SearchBean searchBean, Preload[] preloads);

}
