package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.proxy.HibernateProxy;

import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.Bahnhof;
import db.training.bob.model.zvf.Fplonr;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Regelweg;
import db.training.bob.model.zvf.Sender;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Version;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;

public abstract class UebergabeblattExportAction extends BaseAction {

	protected abstract ActionForward run(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception;

	protected Uebergabeblatt getUebergabeblatt(int zvfId) {

		Preload[] preloads = new Preload[] { new Preload(Uebergabeblatt.class, "header"),
		        new Preload(Header.class, "sender"),
		        new Preload(Uebergabeblatt.class, "massnahmen"),
		        new Preload(Massnahme.class, "version"), new Preload(Massnahme.class, "strecke"),
		        new Preload(Massnahme.class, "bbp"), new Preload(Strecke.class, "streckeVZG"),
		        new Preload(Massnahme.class, "allgregelungen"),
		        new Preload(Massnahme.class, "zug"), new Preload(Zug.class, "regelweg"),
		        new Preload(Regelweg.class, "abgangsbahnhof"),
		        new Preload(Regelweg.class, "zielbahnhof"), new Preload(Zug.class, "abweichung"),
		        new Preload(Abweichung.class, "halt"), new Preload(Halt.class, "ausfall"),
		        new Preload(Halt.class, "ersatz"), new Preload(Abweichung.class, "ausfallvon"),
		        new Preload(Abweichung.class, "ausfallbis"),
		        new Preload(Abweichung.class, "umleitweg"),
		        new Preload(Abweichung.class, "vorplanab"), new Preload(Massnahme.class, "fplonr"),
		        new Preload(Fplonr.class, "niederlassungen"),
		        new Preload(Zug.class, "knotenzeiten") };

		UebergabeblattService uebService = EasyServiceFactory.getInstance()
		    .createUebergabeblattService();
		Uebergabeblatt ueb = uebService.findById(zvfId, preloads);
		Header header = ueb.getHeader();

		// JAXB hat Probleme mit Proxies, deshalb werden diese hier durch ihre Implementation
		// ersetzt
		header.setSender((Sender) ((HibernateProxy) header.getSender())
		    .getHibernateLazyInitializer().getImplementation());
		Massnahme m = ueb.getMassnahmen().iterator().next();
		m.setVersion((Version) ((HibernateProxy) m.getVersion()).getHibernateLazyInitializer()
		    .getImplementation());
		m.setFplonr(((Fplonr) ((HibernateProxy) m.getFplonr()).getHibernateLazyInitializer()
		    .getImplementation()));

		List<Zug> zuege = m.getZug();
		Iterator<Zug> itZug = zuege.iterator();
		List<Zug> noProxyzuege = new ArrayList<Zug>();
		while (itZug.hasNext()) {
			Zug z = itZug.next();
			Regelweg regelweg = z.getRegelweg();
			regelweg.setAbgangsbahnhof((Bahnhof) ((HibernateProxy) regelweg.getAbgangsbahnhof())
			    .getHibernateLazyInitializer().getImplementation());
			regelweg.setZielbahnhof((Bahnhof) ((HibernateProxy) regelweg.getZielbahnhof())
			    .getHibernateLazyInitializer().getImplementation());
			z.setRegelweg(((Regelweg) ((HibernateProxy) z.getRegelweg())
			    .getHibernateLazyInitializer().getImplementation()));
			noProxyzuege.add(z);
		}
		return ueb;
	}

}
