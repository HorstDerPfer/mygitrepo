package db.training.bob.model.zvf.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

	// the desired format
	private String pattern = "yyyy-MM-dd";

	public String marshal(Date date) throws Exception {
		if (date != null)
			return new SimpleDateFormat(pattern).format(date);
		return "";
	}

	public Date unmarshal(String dateString) throws Exception {
		return new SimpleDateFormat(pattern).parse(dateString);
	}
}
