package db.training.bob.model.zvf.helper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;

public class RegionalbereichAdapter extends XmlAdapter<String, Regionalbereich> {

	private static final String OST = "Ost";

	private static final String NORD = "Nord";

	private static final String WEST = "West";

	private static final String SUEDOST = "Südost";

	private static final String MITTE = "Mitte";

	private static final String SUEDWEST = "Südwest";

	private static final String SUED = "Süd";

	public static List<String> getRegionalbereichNames() {
		List<String> names = new ArrayList<String>();
		names.add(OST);
		names.add(NORD);
		names.add(WEST);
		names.add(SUEDOST);
		names.add(MITTE);
		names.add(SUEDWEST);
		names.add(SUED);
		return names;
	}

	@Override
	public String marshal(Regionalbereich rb) throws Exception {
		if (rb.getName().equals(OST))
			return RegionalbereichXMLName.OST.toString();
		if (rb.getName().equals(NORD))
			return RegionalbereichXMLName.NORD.toString();
		if (rb.getName().equals(WEST))
			return RegionalbereichXMLName.WEST.toString();
		if (rb.getName().equals(SUEDOST))
			return RegionalbereichXMLName.SUEDOST.toString();
		if (rb.getName().equals(MITTE))
			return RegionalbereichXMLName.MITTE.toString();
		if (rb.getName().equals(SUEDWEST))
			return RegionalbereichXMLName.SUEDWEST.toString();
		return RegionalbereichXMLName.SUED.toString();
	}

	@Override
	public Regionalbereich unmarshal(String rbName) throws Exception {
		RegionalbereichService rbService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		if (rbName.equals(RegionalbereichXMLName.OST.toString()))
			return rbService.findByName(OST).get(0);
		if (rbName.equals(RegionalbereichXMLName.NORD.toString()))
			return rbService.findByName(NORD).get(0);
		if (rbName.equals(RegionalbereichXMLName.WEST.toString()))
			return rbService.findByName(WEST).get(0);
		if (rbName.equals(RegionalbereichXMLName.SUEDOST.toString()))
			return rbService.findByName(SUEDOST).get(0);
		if (rbName.equals(RegionalbereichXMLName.MITTE.toString()))
			return rbService.findByName(MITTE).get(0);
		if (rbName.equals(RegionalbereichXMLName.SUEDWEST.toString()))
			return rbService.findByName(SUEDWEST).get(0);
		return rbService.findByName(SUED).get(0);
	}
}
