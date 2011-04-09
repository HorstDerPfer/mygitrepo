package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class HaltartAdapter extends XmlAdapter<String, Haltart> {

	@Override
	public String marshal(Haltart value) throws Exception {
		return value.toString();
	}

	@Override
	public Haltart unmarshal(String haltart) throws Exception {
		if (haltart.equalsIgnoreCase(Haltart.B.toString()))
			return Haltart.B;
		if (haltart.equalsIgnoreCase(Haltart.X.toString()))
			return Haltart.X;
		if (haltart.equalsIgnoreCase(Haltart.H.toString()))
			return Haltart.H;
		if (haltart.equalsIgnoreCase(Haltart.PLUS.toString()))
			return Haltart.PLUS;
		return Haltart.LEER;
	}

}
