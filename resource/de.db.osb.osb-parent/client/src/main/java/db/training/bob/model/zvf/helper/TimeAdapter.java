package db.training.bob.model.zvf.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimeAdapter extends XmlAdapter<String, Date> {

	// the desired format
	private String pattern = "HH:mm";

	public String marshal(Date date) throws Exception {
		return new SimpleDateFormat(pattern).format(date);
	}

	public Date unmarshal(String dateString) throws Exception {
		return new SimpleDateFormat(pattern).parse(dateString);
	}
}
