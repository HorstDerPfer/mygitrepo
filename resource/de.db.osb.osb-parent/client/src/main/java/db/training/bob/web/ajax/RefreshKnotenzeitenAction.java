package db.training.bob.web.ajax;

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
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.KnotenzeitService;
import db.training.bob.service.zvf.ZugService;
import db.training.bob.web.baumassnahme.KnotenzeitenForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshKnotenzeitenAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshKnotenzeitenAction.class);

	private ZugService zugService = null;

	private KnotenzeitService knotenzeitService = null;

	public RefreshKnotenzeitenAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		zugService = serviceFactory.createZugService();
		knotenzeitService = serviceFactory.createKnotenzeitService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering RefreshKnotenzeitenAction.");

		Integer knotenzeit = null;
		List<Integer> zugIdList = null;
		KnotenzeitenForm knotenzeitenForm = (KnotenzeitenForm) form;
		Set<Zug> zugList = null;

		if (request.getParameter("knotenzeit") != null)// null bei add
			knotenzeit = FrontendHelper.castStringToInteger(request.getParameter("knotenzeit"));
		if (request.getParameter("zugId") != null) {
			zugIdList = castArrayToInteger(request.getParameter("zugId"));
			zugList = getZugList(zugIdList);
		}

		if (zugIdList != null & knotenzeit != null) {// delete
			Knotenzeit deleteKnoten = knotenzeitService.findById(knotenzeit);
			Iterator<Zug> it = zugList.iterator();
			Zug z = null;
			List<Knotenzeit> knoten = null;
			while (it.hasNext()) {
				z = it.next();
				knoten = z.getKnotenzeiten();
				knoten.remove(deleteKnoten);
				zugService.update(z);
			}
			try {
				knotenzeitService.delete(deleteKnoten);
			} catch (Exception e) {
			}// ConstraintViolationException, wenn noch andere Züge auf diese knotenzeit verweisen,
			// aber nicht für die Bearbeitung ausgewählt wurden
		} else { // add
			knotenzeitenForm = (KnotenzeitenForm) form;
			Knotenzeit kn = new Knotenzeit();
			knotenzeitService.create(kn);
			Iterator<Zug> it = zugList.iterator();
			Zug z = null;
			while (it.hasNext()) {
				z = it.next();
				z.getKnotenzeiten().add(kn);
				zugService.update(z);
			}
		}
		knotenzeitenForm.reset();
		List<Knotenzeit> knotenzeiten = getGemeinsameKnotenzeiten(zugList);
		fillForm(knotenzeiten, knotenzeitenForm);
		request.setAttribute("knotenzeiten", knotenzeiten);
		List<Haltart> haltArten = Arrays.asList(Haltart.values());
		request.setAttribute("haltArten", haltArten);
		request.setAttribute("zugIds", zugIdList);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");
		response.setHeader("Content-Type", "text/html; charset=utf-8");

		return mapping.findForward("SUCCESS");
	}

	private List<Knotenzeit> getGemeinsameKnotenzeiten(Set<Zug> zugList) {
		List<Knotenzeit> knotenzeiten = new ArrayList<Knotenzeit>();
		knotenzeiten.addAll(zugList.iterator().next().getKnotenzeiten());
		for (Zug z : zugList) {
			knotenzeiten.retainAll(z.getKnotenzeiten());
		}
		return knotenzeiten;
	}

	private Set<Zug> getZugList(List<Integer> zugIdList) {
		Zug z = null;
		Set<Zug> zugList = new HashSet<Zug>();
		for (Integer id : zugIdList) {
			z = zugService.findById(id, new FetchPlan[] { FetchPlan.UEB_KNOTENZEITEN });
			if (z != null)
				zugList.add(z);
		}
		return zugList;
	}

	private List<Integer> castArrayToInteger(String parameter) {
		// [100, 101]
		parameter = parameter.substring(1, parameter.length() - 1);
		List<Integer> idList = new ArrayList<Integer>();
		String[] ids = parameter.split(",");
		String part = null;
		for (int i = 0; i < ids.length; i++) {
			part = ids[i].trim();
			idList.add(Integer.valueOf(part));
		}
		return idList;
	}

	private void fillForm(List<Knotenzeit> knotenzeiten, KnotenzeitenForm knotenzeitenForm) {
		String index = null;
		Knotenzeit kn = null;
		int i = 0;
		Iterator<Knotenzeit> it = knotenzeiten.iterator();
		while (it.hasNext()) {
			index = "" + i++;
			kn = it.next();
			if (kn != null) {
				knotenzeitenForm.setBahnhof(index, kn.getBahnhof());
				if (kn.getHaltart() != null)
					if (kn.getHaltart().name().equals("LEER"))
						knotenzeitenForm.setHaltart(index, "");
					else if (kn.getHaltart().name().equals("PLUS"))
						knotenzeitenForm.setHaltart(index, "+");
					else
						knotenzeitenForm.setHaltart(index, kn.getHaltart().name());

				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				if (kn.getAnkunft() != null)
					knotenzeitenForm.setAn(index, df.format(kn.getAnkunft()));
				else
					knotenzeitenForm.setAn(index, "");

				if (kn.getAbfahrt() != null)
					knotenzeitenForm.setAb(index, df.format(kn.getAbfahrt()));
				else
					knotenzeitenForm.setAb(index, "");

				if (kn.getRelativlage() != null)
					knotenzeitenForm.setRelativlage(index, kn.getRelativlage().toString());
			}
		}
	}
}
