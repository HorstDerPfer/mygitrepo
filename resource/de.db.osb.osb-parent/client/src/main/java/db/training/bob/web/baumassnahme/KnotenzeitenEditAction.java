package db.training.bob.web.baumassnahme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.ZugService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class KnotenzeitenEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(KnotenzeitenEditAction.class);

	private ZugService zugService;

	private BaumassnahmeService baumassnahmeService;

	public KnotenzeitenEditAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		zugService = serviceFactory.createZugService();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering KnotenzeitenEditAction.");

		KnotenzeitenForm knotenzeitenForm = (KnotenzeitenForm) form;
		// Integer zugId = null;
		Integer bmId = null;

		if (request.getParameter("bmId") != null)
			bmId = Integer.parseInt(request.getParameter("bmId"));
		else
			bmId = knotenzeitenForm.getBaumassnahmeId();

		List<Integer> zugIdList = getParameterZuege(request, knotenzeitenForm);
		if (zugIdList == null || zugIdList.size() == 0) {
			if (log.isDebugEnabled())
				log.debug("keine Züge");
			addError("error.ueb.zug.auswahl");
			request.setAttribute("id", bmId);
			return mapping.findForward("FAILURE");
		}
		Set<Zug> zugList = getZugList(zugIdList);

		if (zugList.size() == 1) {
			// vorhandene Knotenzeiten anzeigen
			Zug z = zugList.iterator().next();
			if (!hasErrors(request)) {
				knotenzeitenForm.reset();
				List<Knotenzeit> knotenzeiten = z.getKnotenzeiten();

				knotenzeitenForm.setId(z.getId());
				knotenzeitenForm.setBaumassnahmeId(bmId);
				knotenzeitenForm.setAdd(false);

				fillForm(knotenzeiten, knotenzeitenForm);

			}
			request.setAttribute("zug", z);
			request.setAttribute("knotenzeiten", z.getKnotenzeiten());
			request.setAttribute("zugIds", zugIdList);
		} else if (zugList.size() > 1) {
			// Batch-Eingabe
			List<Knotenzeit> gemeinsameKnotenzeiten = getGemeinsameKnotenzeiten(zugList);
			if (!hasErrors(request)) {
				knotenzeitenForm.reset();
				knotenzeitenForm.setBaumassnahmeId(bmId);
				knotenzeitenForm.setAdd(false);
				knotenzeitenForm.setZugIds(zugIdList);
				setErstenUndLetztenVerkehrstag(zugList, knotenzeitenForm);
				if (gemeinsameKnotenzeiten.size() > 0) {
					fillForm(gemeinsameKnotenzeiten, knotenzeitenForm);
				}
			}
			request.setAttribute("knotenzeiten", gemeinsameKnotenzeiten);
			request.setAttribute("zugIds", zugIdList);
			request.setAttribute("ersterVerkehrstag", knotenzeitenForm.getErsterVerkehrstag());
			request.setAttribute("letzterVerkehrstag", knotenzeitenForm.getLetzterVerkehrstag());
		} else {

		}

		Baumassnahme b = baumassnahmeService.findById(bmId);
		zugIdList = null;
		List<Haltart> haltArten = Arrays.asList(Haltart.values());
		request.setAttribute("haltArten", haltArten);

		request.setAttribute("baumassnahme", b);
		return mapping.findForward("SUCCESS");
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

	private List<Knotenzeit> getGemeinsameKnotenzeiten(Set<Zug> zugList) {
		List<Knotenzeit> knotenzeiten = new ArrayList<Knotenzeit>();
		knotenzeiten.addAll(zugList.iterator().next().getKnotenzeiten());
		for (Zug z : zugList) {
			knotenzeiten.retainAll(z.getKnotenzeiten());
		}
		return knotenzeiten;
	}

	private void setErstenUndLetztenVerkehrstag(Set<Zug> zugList, KnotenzeitenForm knotenzeitenForm) {
		Date ersterVerkehrstag = null;
		Date letzterVerkehrstag = null;
		for (Zug z : zugList) {
			if (ersterVerkehrstag == null) {
				ersterVerkehrstag = z.getVerkehrstag();
				letzterVerkehrstag = z.getVerkehrstag();
			} else {
				if (ersterVerkehrstag.compareTo(z.getVerkehrstag()) > 0)
					ersterVerkehrstag = z.getVerkehrstag();
				if (letzterVerkehrstag.compareTo(z.getVerkehrstag()) < 0)
					letzterVerkehrstag = z.getVerkehrstag();
			}
		}
		knotenzeitenForm.setErsterVerkehrstag(FrontendHelper.castDateToString(ersterVerkehrstag));
		knotenzeitenForm.setLetzterVerkehrstag(FrontendHelper.castDateToString(letzterVerkehrstag));
	}

	private Set<Zug> getZugList(List<Integer> zugIdList) {
		Zug z = null;
		Set<Zug> zugList = new HashSet<Zug>();
		for (Integer id : zugIdList) {
			z = zugService.findById(id, new FetchPlan[] { FetchPlan.UEB_KNOTENZEITEN,
			        FetchPlan.UEB_ZUG_REGELWEG });
			if (z != null)
				zugList.add(z);
		}
		return zugList;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> getParameterZuege(HttpServletRequest request,
	    KnotenzeitenForm knotenzeitenForm) {
		String[] zuege = null;
		List<Integer> zugIdList = new ArrayList<Integer>(); // von edit ÜB
		if (request.getParameterValues("checkZug") != null) {
			zuege = request.getParameterValues("checkZug");
			int zIndex;
			for (String z : zuege) {
				zIndex = Integer.valueOf(z);
				zugIdList.add(zIndex);
				knotenzeitenForm.setZugIds(zugIdList);
			}
			knotenzeitenForm.setError(false);
			return zugIdList;
		}
		if (request.getAttribute("zugIds") != null) {// nach save success
			zugIdList = (List<Integer>) request.getAttribute("zugIds");
			knotenzeitenForm.setZugIds(zugIdList);
			knotenzeitenForm.setError(false);
			return zugIdList;
		}
		if (knotenzeitenForm.getZugIds() != null & knotenzeitenForm.isError() == true) {// nach save
			// failure
			zugIdList = knotenzeitenForm.getZugIds();
		}
		knotenzeitenForm.setError(false);
		return zugIdList;
	}
}
