package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import db.training.bob.model.zvf.BBPStrecke;
import db.training.bob.service.zvf.BBPStreckeService;
import db.training.easy.core.service.EasyServiceFactory;

public class BBPStreckeAdapter extends XmlAdapter<String, BBPStrecke> {

	@Override
	public String marshal(BBPStrecke bbpStrecke) throws Exception {
		return bbpStrecke.getNummer().toString();
	}

	@Override
	public BBPStrecke unmarshal(String nummer) throws Exception {
		BBPStreckeService streckeService = EasyServiceFactory.getInstance()
		    .createBBPStreckeService();
		try {
			int nr = Integer.valueOf(nummer);
			BBPStrecke bbpStrecke = streckeService.findByNummer(nr);
			if (bbpStrecke == null)
				throw new Exception();
			return bbpStrecke;
		} catch (Exception e) {
			throw e;
		}
	}

}
