package db.training.bob.model;

import java.util.SortedSet;
import java.util.TreeSet;

public class Status {

	public static StatusType aggregate(final StatusType... StatusTypes) {
		SortedSet<StatusType> statusSet = new TreeSet<StatusType>();
		for (StatusType s : StatusTypes) {
			if (s != null)
				statusSet.add(s);
		}
		if (statusSet.size() == 0)
			return StatusType.NEUTRAL;
		return statusSet.last();
	}

}
