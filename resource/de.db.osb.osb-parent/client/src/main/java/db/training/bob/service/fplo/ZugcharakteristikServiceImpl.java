package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.fplo.ZugcharakteristikDao;
import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.model.fplo.ISA_Bestellung;
import db.training.bob.model.fplo.ISA_Fahrplan;
import db.training.bob.model.fplo.ISA_Zug;
import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.FrontendHelper;

public class ZugcharakteristikServiceImpl extends EasyServiceImpl<Zugcharakteristik, Serializable>
    implements ZugcharakteristikService {

	private FahrplanService fahrplanService;

	public ZugcharakteristikServiceImpl() {
		super(Zugcharakteristik.class);
	}

	public ZugcharakteristikDao getDao() {
		return (ZugcharakteristikDao) getBasicDao();
	}

	public void setFahrplanService(FahrplanService fahrplanService) {
		this.fahrplanService = fahrplanService;
	}

	public FahrplanService getFahrplanService() {
		return fahrplanService;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Fahrplan> getFahrplanSortByLfd(int id) {
		List<Fahrplan> objects = null;
		Session session = getCurrentSession();

		Query query = session.createQuery("from Fahrplan where zugcharakteristik_ID=" + id
		    + "order by lfd");
		objects = query.list();

		return objects;
	}

	public List<Zugcharakteristik> createFromISAObjects(List<ISA_Zug> isaZuege) {
		List<Zugcharakteristik> result = new ArrayList<Zugcharakteristik>();
		Iterator<ISA_Zug> it = isaZuege.iterator();
		ISA_Zug isaZug = null;
		while (it.hasNext()) {
			isaZug = it.next();
			result.add(createFromISAObject(isaZug));
		}
		return result;
	}

	public Zugcharakteristik createFromISAObject(ISA_Zug isaZug) {
		Zugcharakteristik zugcharakteristik = new Zugcharakteristik();
		zugcharakteristik.setIdZug(isaZug.getIdZug());
		zugcharakteristik.setVerkehrstag(isaZug.getErstervtag());
		String gattungsbezeichnung = getGattungsbezeichnungFromGattung(isaZug.getGattung());
		zugcharakteristik.setGattungsbezeichnung(gattungsbezeichnung);
		String gattungsnr = getGattungsnrFromGattung(isaZug.getGattung());
		zugcharakteristik.setGattungsnr(gattungsnr);
		zugcharakteristik.setStartBf(isaZug.getStartbf());
		zugcharakteristik.setZielBf(isaZug.getZielbf());
		zugcharakteristik.setTagwechsel(isaZug.getTagwechsel());
		zugcharakteristik.setZugnr(FrontendHelper.castStringToInteger(isaZug.getZugnummer()));
		zugcharakteristik.setDatumKonstruktion(isaZug.getDatumKonstruktion());

		// Bestellung
		ISA_Bestellung bestellung = isaZug.getBestellung();
		if (bestellung != null)
			zugcharakteristik.setKundennummer(bestellung.getKundennr());

		// Fahrplan
		Set<ISA_Fahrplan> isaFahrplanSet = isaZug.getFahrplan();
		fahrplanService = getFahrplanService();
		Set<Fahrplan> fahrplanSet = fahrplanService.createFromISAObjects(isaFahrplanSet);
		zugcharakteristik.setFahrplan(fahrplanSet);

		return zugcharakteristik;
	}

	private String getGattungsbezeichnungFromGattung(String gattung) {
		if (gattung == null || gattung.length() == 0)
			return "";
		String[] parts = gattung.split("-");
		if (parts.length == 2)
			return parts[1].trim();
		return "";
	}

	private String getGattungsnrFromGattung(String gattung) {
		if (gattung == null || gattung.length() == 0)
			return "";
		String[] parts = gattung.split("-");
		if (parts.length > 0) {
			String part = parts[0].trim();
			if (part.length() > 0) {
				StringBuilder result = new StringBuilder();
				result.append(part.substring(0, part.length() - 1));
				result.append(".");
				result.append(part.substring(part.length() - 1));
				return result.toString();
			}
		}
		return "";
	}

	@Transactional
	public Zugcharakteristik findByIdZug(Integer idZug) {
		Zugcharakteristik object = null;

		Criteria criteria = getCurrentSession().createCriteria(Zugcharakteristik.class);
		criteria.add(Restrictions.eq("idZug", idZug));
		object = getDao().findUniqueByCriteria(criteria);

		return object;
	}

	public Zugcharakteristik merge(Zugcharakteristik persistent, Zugcharakteristik z) {
		z.setId(persistent.getId());
		persistent.getFahrplan().addAll(z.getFahrplan());
		return persistent;
	}

}