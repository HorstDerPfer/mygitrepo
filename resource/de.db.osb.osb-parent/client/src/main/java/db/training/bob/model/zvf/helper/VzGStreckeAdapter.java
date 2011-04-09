package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

public class VzGStreckeAdapter extends XmlAdapter<String, VzgStrecke> {

	@Override
	public String marshal(VzgStrecke vzgStrecke) throws Exception {
		return vzgStrecke.getNummer().toString();
	}

	@Override
	public VzgStrecke unmarshal(String nummer) throws Exception {
		VzgStreckeService streckeService = EasyServiceFactory.getInstance()
		    .createVzgStreckeService();
		try {
			int nr = Integer.valueOf(nummer);
			VzgStrecke vzgStrecke = streckeService.findByNummer(nr, null, null);
			return vzgStrecke;
		} catch (Exception e) {
			return null;
		}
	}
}
