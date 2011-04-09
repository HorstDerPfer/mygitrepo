package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import db.training.bob.dao.fplo.FahrplanDao;
import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.model.fplo.ISA_Fahrplan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.FrontendHelper;

public class FahrplanServiceImpl extends EasyServiceImpl<Fahrplan, Serializable> implements
    FahrplanService {

	public FahrplanServiceImpl() {
		super(Fahrplan.class);
	}

	public FahrplanDao getDao() {
		return (FahrplanDao) getBasicDao();
	}

	public Fahrplan createFromISAObject(ISA_Fahrplan isaFahrplan) {
		if (isaFahrplan == null)
			return null;

		Fahrplan fahrplan = new Fahrplan();
		fahrplan.setAbfahrt(FrontendHelper.castStringToTime(isaFahrplan.getAbfahrt()));
		fahrplan.setAnkunft(FrontendHelper.castStringToTime(isaFahrplan.getAnkunft()));
		fahrplan.setBst(isaFahrplan.getBst());
		fahrplan.setHaltart(isaFahrplan.getHaltart());
		fahrplan.setLfd(isaFahrplan.getLfd());
		fahrplan.setVzg(FrontendHelper.castStringToInteger(isaFahrplan.getVzg()));
		fahrplan.setZugfolge(isaFahrplan.getZugfolge());
		fahrplan.setIdFahrplan(isaFahrplan.getIdFahrplan());
		return fahrplan;
	}

	public Set<Fahrplan> createFromISAObjects(Set<ISA_Fahrplan> isaFahrplanSet) {
		if (isaFahrplanSet == null)
			return new HashSet<Fahrplan>();

		Set<Fahrplan> resulSet = new HashSet<Fahrplan>();
		Iterator<ISA_Fahrplan> it = isaFahrplanSet.iterator();
		ISA_Fahrplan isaFahrplan = null;
		Fahrplan fahrplan = null;
		while (it.hasNext()) {
			isaFahrplan = it.next();
			fahrplan = createFromISAObject(isaFahrplan);
			// this.create(fahrplan);
			resulSet.add(fahrplan);
		}
		return resulSet;
	}
}