package db.training.bob.web.baumassnahme;

import java.util.Comparator;

class ColumnIndexComparator implements Comparator<Column> {

	public ColumnIndexComparator() {
		;
	}

	public int compare(Column arg0, Column arg1) {
		if (arg0 == null || arg1 == null) {
			return 0;
		}

		if (arg0.getIndex() < arg1.getIndex()) {
			return -1;
		}

		if (arg0.getIndex() > arg1.getIndex()) {
			return 1;
		}

		return 0;
	}

}