package db.training.bob.model.zvf.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IsoDateAdapter extends XmlAdapter<String, Date> {

	private String isoPattern = "yyyy-MM-dd'T'HH:mm:ss";

	public String marshal(Date date) throws Exception {
		return new SimpleDateFormat(isoPattern).format(date);
	}

	public Date unmarshal(String dateString) throws Exception {
		return new SimpleDateFormat(isoPattern).parse(dateString);
	}
}
