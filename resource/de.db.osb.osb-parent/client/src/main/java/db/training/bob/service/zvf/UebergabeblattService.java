package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.Collection;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;

public interface UebergabeblattService extends BasicService<Uebergabeblatt, Serializable> {

	public void fill(Uebergabeblatt zvf, Collection<FetchPlan> plans);
}