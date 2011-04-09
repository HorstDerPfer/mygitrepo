package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaNeinAdapter extends XmlAdapter<String, Boolean> {

	public String marshal(Boolean value) throws Exception {
		if (value == true)
			return "Ja";
		return "Nein";
	}

	public Boolean unmarshal(String jaOderNein) throws Exception {
		if (jaOderNein.equalsIgnoreCase("Ja")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
