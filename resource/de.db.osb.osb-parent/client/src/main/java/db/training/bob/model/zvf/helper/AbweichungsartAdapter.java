package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AbweichungsartAdapter extends XmlAdapter<String, Abweichungsart> {

	@Override
	public String marshal(Abweichungsart value) throws Exception {
		if (value != null)
			return value.toString();
		return "";
	}

	@Override
	public Abweichungsart unmarshal(String abweichungsart) throws Exception {
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.VERSPAETUNG.toString()))
			return Abweichungsart.VERSPAETUNG;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.UMLEITUNG.toString()))
			return Abweichungsart.UMLEITUNG;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.AUSFALL.toString()))
			return Abweichungsart.AUSFALL;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.VORPLAN.toString()))
			return Abweichungsart.VORPLAN;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.GESPERRT.toString()))
			return Abweichungsart.GESPERRT;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.REGELUNG.toString()))
			return Abweichungsart.REGELUNG;
		return Abweichungsart.ERSATZHALTE;
	}

}
