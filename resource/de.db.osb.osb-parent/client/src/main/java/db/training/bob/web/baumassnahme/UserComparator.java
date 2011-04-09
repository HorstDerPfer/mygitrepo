package db.training.bob.web.baumassnahme;

import java.util.Comparator;

import db.training.easy.core.model.User;

public class UserComparator implements Comparator<User> {

	public UserComparator() {
	}

	public int compare(User arg0, User arg1) {
		if (arg0 == null || arg1 == null) {
			return 0;
		}

		if (arg0.getName().compareTo(arg1.getName()) != 0) {
			return arg0.getName().compareTo(arg1.getName());
		}

		return arg0.getFirstName().compareTo(arg1.getFirstName());
	}

}