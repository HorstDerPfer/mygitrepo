package db.training.osb.model.comparators;

import java.util.Comparator;

import db.training.osb.model.Anmelder;

public class AnmelderNameComparator implements Comparator<Anmelder> {

	public AnmelderNameComparator() {
		super();
	}

	@Override
	public int compare(Anmelder a1, Anmelder a2) {
		if (a1.getName() == null) {
			return -1;
		}
		if (a2.getName() == null) {
			return 1;
		}
		return a1.getName().compareTo(a2.getName());
	}
}
