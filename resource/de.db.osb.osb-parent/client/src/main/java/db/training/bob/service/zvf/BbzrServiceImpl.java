package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;

import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class BbzrServiceImpl extends EasyServiceImpl<Uebergabeblatt, Serializable> implements
    BbzrService {

	private static Logger log = Logger.getLogger(BbzrServiceImpl.class);

	public BbzrServiceImpl() {
		super(Uebergabeblatt.class);
	}

	@Override
	public void fill(Uebergabeblatt bbzr, Collection<FetchPlan> plans) {
		if (log.isDebugEnabled())
			log.debug("Entering BbzrServiceImpl:fill()");

		if (plans.contains(FetchPlan.BBZR_HEADER) || plans.contains(FetchPlan.UEB_HEADER_SENDER)
		    || plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER)) {
			Hibernate.initialize(bbzr.getHeader());

			if (plans.contains(FetchPlan.UEB_HEADER_SENDER) && bbzr.getHeader() != null)
				Hibernate.initialize(bbzr.getHeader().getSender());
			if (plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER) && bbzr.getHeader() != null) {
				Header h = bbzr.getHeader();
				Hibernate.initialize(h.getEmpfaenger());
			}
		}

		if (plans.contains(FetchPlan.BBZR_BAUMASSNAHMEN)) {
			Hibernate.initialize(bbzr.getMassnahmen());
			for (Massnahme m : bbzr.getMassnahmen()) {
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
						streckeService.fill(m.getStrecke(), plans);
					}
				}
				if (plans.contains(FetchPlan.ZVF_MN_BBPSTRECKE)) {
					Hibernate.initialize(m.getBbp());
				}
				if (plans.contains(FetchPlan.ZVF_MN_ALLG_REGELUNGEN)) {
					Hibernate.initialize(m.getAllgregelungen());
				}
				if (plans.contains(FetchPlan.BBZR_MN_ZUEGE)) {
					Hibernate.initialize(m.getZug());
					if (m.getZug() != null) {
						// uebergabeblattspezifische fetchplans entfernen
						FetchPlan[] newFetchPlans = new FetchPlan[0];
						List<FetchPlan> newPlans = new ArrayList<FetchPlan>(plans);
						newPlans.remove(FetchPlan.UEB_MN_ZUEGE);
						newPlans.remove(FetchPlan.UEB_KNOTENZEITEN);
						newPlans.remove(FetchPlan.UEB_BEARBEITUNGSSTATUS);
						newFetchPlans = newPlans.toArray(newFetchPlans);
						ZugService zugService = EasyServiceFactory.getInstance().createZugService();
						zugService.fill(m.getZug(), newFetchPlans);
					}
				}
			}
		}
	}
}
