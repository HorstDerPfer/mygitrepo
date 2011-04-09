package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EinsNullLeerAdapter extends XmlAdapter<String, Boolean> {

	private final static String EINS = "1";

	private final static String NULL = "0";

	public String marshal(Boolean value) throws Exception {
		if (value == null || value == false)
			return NULL;
		return EINS;
	}

	public Boolean unmarshal(String einsOderNull) throws Exception {
		if (einsOderNull.equals(EINS))
			return Boolean.TRUE;
		return Boolean.FALSE; // "0" oder ""
	}
}
