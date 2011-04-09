package db.training.bob.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.model.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "bearbeiter")
public class Bearbeiter extends EasyPersistentObject {

	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	private Boolean aktiv;

	protected Bearbeiter() {
		super();
	}

	public Bearbeiter(User user, boolean aktiv) {
		this.user = user;
		this.aktiv = aktiv;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (Hibernate.getClass(this) != Hibernate.getClass(obj)))
			return false;
		final Bearbeiter otherBearbeiter = (Bearbeiter) obj;
		return (user != null && user.getLoginName().equals(otherBearbeiter.user.getLoginName()));
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

}
