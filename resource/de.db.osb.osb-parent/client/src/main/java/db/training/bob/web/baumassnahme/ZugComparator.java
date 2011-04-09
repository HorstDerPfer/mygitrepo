package db.training.bob.web.baumassnahme;

import java.util.Comparator;

import org.displaytag.properties.SortOrderEnum;

import db.training.bob.model.zvf.Zug;

public class ZugComparator implements Comparator<Zug> {

	private String criterion;

	private SortOrderEnum order;

	public ZugComparator(String criterion, SortOrderEnum order) {
		this.criterion = criterion;
		this.order = order;
	}

	public int compare(Zug arg0, Zug arg1) {
		if (arg0 == null || arg1 == null)
			return 0;

		if (order == SortOrderEnum.ASCENDING)
			return compareObjects(arg0, arg1);
		return compareObjects(arg1, arg0);

	}

	private int compareObjects(Zug arg0, Zug arg1) {
		if (criterion == null || criterion.equals("id"))
			return arg0.getId().compareTo(arg1.getId());
		if (criterion.equals("laufendeNr"))
			return arg0.getLaufendeNr().compareTo(arg1.getLaufendeNr());
		if (criterion.equals("zugnr"))
			return arg0.getZugnr().compareTo(arg1.getZugnr());
		if (criterion.equals("verkehrstag"))
			return arg0.getVerkehrstag().compareTo(arg1.getVerkehrstag());
		if (criterion.equals("zugbez"))
			return arg0.getZugbez().compareTo(arg1.getZugbez());
		if (criterion.equals("regelweg.abgangsbahnhof.langName")) {
			String name0 = arg0.getRegelweg().getAbgangsbahnhof().getLangName();
			String name1 = arg1.getRegelweg().getAbgangsbahnhof().getLangName();
			return name0.compareTo(name1);
		}
		if (criterion.equals("regelweg.zielbahnhof.langName")) {
			String name0 = arg0.getRegelweg().getZielbahnhof().getLangName();
			String name1 = arg1.getRegelweg().getZielbahnhof().getLangName();
			return name0.compareTo(name1);
		}
		if (criterion.equals("richtung"))
			return arg0.getRichtung().compareTo(arg1.getRichtung());

		return 0;
	}
}