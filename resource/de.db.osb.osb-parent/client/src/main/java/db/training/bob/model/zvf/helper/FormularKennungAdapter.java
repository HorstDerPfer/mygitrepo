package db.training.bob.model.zvf.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FormularKennungAdapter extends XmlAdapter<String, FormularKennung> {

	public FormularKennung unmarshal(String formularKennung) throws Exception {
		if (formularKennung.startsWith("ZVF"))
			return FormularKennung.ZVF_091008;
		return FormularKennung.UeB_091008;
	}

	@Override
	public String marshal(FormularKennung value) throws Exception {
		return value.toString();
	}
}
