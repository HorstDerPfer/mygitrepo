package db.training.bob.web.baumassnahme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.KnotenzeitService;
import db.training.bob.service.zvf.ZugService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class KnotenzeitenSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(KnotenzeitenSaveAction.class);

	private ZugService zugService = null;

	private KnotenzeitService knotenzeitService = null;

	public KnotenzeitenSaveAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		zugService = serviceFactory.createZugService();
		knotenzeitService = serviceFactory.createKnotenzeitService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering KnotenzeitenSaveAction.");

		User loginUser = getLoginUser(request);

		KnotenzeitenForm knotenzeitenForm = (KnotenzeitenForm) form;

		Boolean add = knotenzeitenForm.getAdd();
		if (log.isDebugEnabled())
			log.debug("ADD: " + add);

		@SuppressWarnings("unused")
		Integer bmId = null;

		request.setAttribute("zugIds", knotenzeitenForm.getZugIds());
		request.setAttribute("ersterVerkehrstag", knotenzeitenForm.getErsterVerkehrstag());
		request.setAttribute("letzterVerkehrstag", knotenzeitenForm.getLetzterVerkehrstag());
		List<Integer> zugIds = knotenzeitenForm.getZugIds();
		if (zugIds == null || zugIds.size() == 0)
			return mapping.findForward("FAILURE");

		Set<Zug> zugList = getZugList(zugIds, loginUser);

		bmId = knotenzeitenForm.getBaumassnahmeId();

		List<Knotenzeit> knotenzeiten = getGemeinsameKnotenzeiten(zugList);
		String index = null;
		Knotenzeit kn = null;
		Iterator<Knotenzeit> it = knotenzeiten.iterator();
		int i = 0;

		if (add != null) {
			if (add == true) { // add
				Knotenzeit knz = new Knotenzeit();
				knotenzeitService.create(knz);
				Iterator<Zug> itZug = zugList.iterator();
				Zug z = null;
				while (itZug.hasNext()) {
					z = itZug.next();
					z.getKnotenzeiten().add(knz);
					zugService.update(z);
				}
			}
		}

		while (it.hasNext()) {
			kn = it.next();
			index = "" + i++;
			kn.setBahnhof(knotenzeitenForm.getBahnhof(index));
			if (knotenzeitenForm.getHaltart(index).equals(""))
				kn.setHaltart(Haltart.LEER);
			else if (knotenzeitenForm.getHaltart(index).equals("+"))
				kn.setHaltart(Haltart.PLUS);
			else
				kn.setHaltart(Haltart.valueOf(knotenzeitenForm.getHaltart(index)));

			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			if (!knotenzeitenForm.getAn(index).equals(""))
				kn.setAnkunft(df.parse(knotenzeitenForm.getAn(index)));
			else
				kn.setAnkunft(null);

			if (!knotenzeitenForm.getAb(index).equals(""))
				kn.setAbfahrt(df.parse(knotenzeitenForm.getAb(index)));
			else
				kn.setAbfahrt(null);

			if (knotenzeitenForm.getRelativlage(index) != "")
				try {
					kn.setRelativlage(Integer.valueOf(knotenzeitenForm.getRelativlage(index)));
				} catch (NumberFormatException e) {
				}
			knotenzeitService.update(kn);
		}

		request.setAttribute("knotenzeiten", knotenzeiten);
		List<Haltart> haltArten = Arrays.asList(Haltart.values());
		request.setAttribute("haltArten", haltArten);

		addMessage("success.knotenzeiten.save");
		return mapping.findForward("SUCCESS");
	}

	private List<Knotenzeit> getGemeinsameKnotenzeiten(Set<Zug> zugList) {
		List<Knotenzeit> knotenzeiten = new ArrayList<Knotenzeit>();
		if (zugList.size() > 0) {
			knotenzeiten.addAll(zugList.iterator().next().getKnotenzeiten());
			for (Zug z : zugList) {
				knotenzeiten.retainAll(z.getKnotenzeiten());
			}
		}
		return knotenzeiten;
	}

	private Set<Zug> getZugList(List<Integer> zugIdList, User loginUser) {
		Zug z = null;
		Set<Zug> zugList = new HashSet<Zug>();
		for (Integer id : zugIdList) {
			z = zugService.findById(id, new FetchPlan[] { FetchPlan.UEB_BEARBEITUNGSSTATUS,
			        FetchPlan.UEB_KNOTENZEITEN });
			if (z != null) {
				zugList.add(z);
				z.getBearbeitungsStatusMap().put(loginUser.getRegionalbereich(), true);
				zugService.update(z);
			}
		}
		return zugList;
	}
}
