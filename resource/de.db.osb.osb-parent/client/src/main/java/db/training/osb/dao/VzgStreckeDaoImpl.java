package db.training.osb.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDaoImp;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.VzgStrecke;
import db.training.osb.web.buendel.VzgStreckeReport;

public class VzgStreckeDaoImpl extends BasicDaoImp<VzgStrecke, Serializable> implements
    VzgStreckeDao {

	public VzgStreckeDaoImpl(SessionFactory instance) {
		super(VzgStrecke.class, instance);
	}

	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, Date gueltigVon,
	    Date gueltigBis, Regionalbereich regionalbereich) {

		return executeGetVzgStreckenverlaufCaptionQuery(vzgStrecke, regionalbereich, gueltigVon,
		    gueltigBis);
	}

	public Set<VzgStrecke> findByGueltigkeit(Date gueltigVon, Date gueltigBis) {

		return executeGetVzgStreckenverlaufCaptionQuery(null, null, gueltigVon, gueltigBis);
	}

	public Set<VzgStrecke> executeGetVzgStreckenverlaufCaptionQuery(VzgStrecke vzgStrecke,
	    Regionalbereich regionalbereich, Date gueltigVon, Date gueltigBis) {

		PreloadEventListener.clearPreloads();

		Query qry = getCurrentSession().getNamedQuery("vzgStreckenverlaufCaption");

		if (regionalbereich != null) {
			qry.setInteger("regionalbereichPruefen", 1);
			qry.setInteger("regionalbereichId", regionalbereich.getId());
		} else {
			qry.setInteger("regionalbereichPruefen", 0); // vergl. Elsner-Trick
			qry.setInteger("regionalbereichId", 0); // muss angegeben werden, Prüfung wird nicht
			                                        // durchgeführt
		}

		if (vzgStrecke != null) {
			qry.setInteger("strecke", vzgStrecke.getNummer());
			qry.setInteger("alleStreckenLaden", 0);
		} else {
			qry.setInteger("strecke", 0);
			qry.setInteger("alleStreckenLaden", 1);
		}
		qry.setDate("gueltigVon", gueltigVon);
		qry.setDate("gueltigBis", gueltigBis);

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> qryResults = qry.setResultTransformer(
		    Transformers.ALIAS_TO_ENTITY_MAP).list();
		Set<VzgStrecke> result = new TreeSet<VzgStrecke>();

		for (Iterator<HashMap<String, Object>> iter = qryResults.iterator(); iter.hasNext();) {
			HashMap<String, Object> map = iter.next();
			VzgStreckeReport strecke = new VzgStreckeReport((VzgStrecke) map.get("st"));
			strecke.setFirstBst((Betriebsstelle) map.get("bmin"));
			strecke.setLastBst((Betriebsstelle) map.get("bmax"));

			result.add(strecke);
		}

		return result;
	}
}