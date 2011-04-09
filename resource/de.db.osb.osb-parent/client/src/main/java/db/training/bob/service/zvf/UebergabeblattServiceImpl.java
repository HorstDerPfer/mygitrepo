package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;

import db.training.bob.dao.zvf.UebergabeblattDao;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class UebergabeblattServiceImpl extends EasyServiceImpl<Uebergabeblatt, Serializable>
    implements UebergabeblattService {

	private static Logger log = Logger.getLogger(UebergabeblattServiceImpl.class);

	public UebergabeblattServiceImpl() {
		super(Uebergabeblatt.class);
	}

	public UebergabeblattDao getDao() {
		return (UebergabeblattDao) getBasicDao();
	}

	@Override
	public void fill(Uebergabeblatt ueb, Collection<FetchPlan> plans) {
		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattServiceImpl:fill()");

		if (plans.contains(FetchPlan.UEB_HEADER) || plans.contains(FetchPlan.UEB_HEADER_SENDER)
		    || plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER)) {
			Hibernate.initialize(ueb.getHeader());

			if (plans.contains(FetchPlan.UEB_HEADER_SENDER) && ueb.getHeader() != null)
				Hibernate.initialize(ueb.getHeader().getSender());
			if (plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER) && ueb.getHeader() != null)
				Hibernate.initialize(ueb.getHeader().getEmpfaenger());
		}

		if (plans.contains(FetchPlan.UEB_BAUMASSNAHMEN)) {
			Hibernate.initialize(ueb.getMassnahmen());
			for (Massnahme m : ueb.getMassnahmen()) {
				if (m == null)
					continue;

				if (plans.contains(FetchPlan.ZVF_MN_VERSION)) {
					Hibernate.initialize(m.getVersion());
				}
				if (plans.contains(FetchPlan.ZVF_MN_STRECKEN)) {
					Hibernate.initialize(m.getStrecke());

					if (plans.contains(FetchPlan.ZVF_MN_STRECKE_STRECKEVZG)) {
						StreckeService streckeService = EasyServiceFactory.getInstance()
						    .createStreckeService();
						for (Strecke strecke : m.getStrecke()) {
							streckeService.fill(strecke, plans);
						}
					}
				}
				if (plans.contains(FetchPlan.ZVF_MN_BBPSTRECKE)) {
					Hibernate.initialize(m.getBbp());
				}
				if (plans.contains(FetchPlan.ZVF_MN_ALLG_REGELUNGEN)) {
					Hibernate.initialize(m.getAllgregelungen());
				}
				if (plans.contains(FetchPlan.UEB_MN_FPLO)
				    || plans.contains(FetchPlan.MN_FPLO_NIEDERLASSUNGEN)) {
					Hibernate.initialize(m.getFplonr());

					if (plans.contains(FetchPlan.MN_FPLO_NIEDERLASSUNGEN) && m.getFplonr() != null)
						Hibernate.initialize(m.getFplonr().getNiederlassungen());

				}
				if (plans.contains(FetchPlan.UEB_MN_ZUEGE)) {
					Hibernate.initialize(m.getZug());
					if (m.getZug() != null) {
						// uebergabeblattspezifische fetchplans entfernen
						FetchPlan[] newFetchPlans = new FetchPlan[0];
						List<FetchPlan> newPlans = new ArrayList<FetchPlan>(plans);
						newPlans.remove(FetchPlan.BBZR_MN_ZUEGE);
						newFetchPlans = newPlans.toArray(newFetchPlans);

						ZugService zugService = EasyServiceFactory.getInstance().createZugService();
						zugService.fill(m.getZug(), newFetchPlans);
					}
				}
			}
		}
	}
}