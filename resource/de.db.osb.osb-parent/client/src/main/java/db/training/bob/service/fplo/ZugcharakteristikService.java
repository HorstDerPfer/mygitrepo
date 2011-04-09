package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.model.fplo.ISA_Zug;
import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.easy.common.BasicService;

public interface ZugcharakteristikService extends BasicService<Zugcharakteristik, Serializable> {

	public List<Fahrplan> getFahrplanSortByLfd(int id);

	public List<Zugcharakteristik> createFromISAObjects(List<ISA_Zug> isaZuege);

	public Zugcharakteristik createFromISAObject(ISA_Zug isaZug);

	public void setFahrplanService(FahrplanService fahrplanService);

	public FahrplanService getFahrplanService();

	public Zugcharakteristik findByIdZug(Integer idZug);

	public Zugcharakteristik merge(Zugcharakteristik persistent, Zugcharakteristik z);
}
