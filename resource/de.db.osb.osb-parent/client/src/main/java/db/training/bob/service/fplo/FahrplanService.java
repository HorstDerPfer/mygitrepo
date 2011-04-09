package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.Set;

import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.model.fplo.ISA_Fahrplan;
import db.training.easy.common.BasicService;

public interface FahrplanService extends BasicService<Fahrplan, Serializable> {

	public Set<Fahrplan> createFromISAObjects(Set<ISA_Fahrplan> isaFahrplaene);

	public Fahrplan createFromISAObject(ISA_Fahrplan isaFahrplan);
}
