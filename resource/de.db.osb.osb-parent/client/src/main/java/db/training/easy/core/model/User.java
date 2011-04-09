package db.training.easy.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.easy.util.ClassUtils;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "app_user", uniqueConstraints = {})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends EasyPersistentObjectVc implements Comparable<User>, Historizable {

	@Column(name = "loginname", unique = true)
	private String loginName;

	@Column(name = "name")
	private String name;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "middleinitial", length = 3)
	private String middleInitial;

	@Column(name = "email")
	private String email;

	@ManyToOne(fetch = FetchType.EAGER)
	private Regionalbereich regionalbereich;

	@ManyToOne(fetch = FetchType.EAGER)
	private Bearbeitungsbereich bearbeitungsbereich;

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getEviMail() {
		if (email != null)
			return email.substring(0, email.indexOf("@") + 1);
		return null;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public void setBearbeitungsbereich(Bearbeitungsbereich bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	public Bearbeitungsbereich getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	/**
	 * Name und Vorname des Benutzers
	 * 
	 * @return name, firstName
	 */
	@Transient
	public String getCaption() {
		return name + ", " + firstName + " (" + email + ")";
	}

	@Transient
	public String getNameAndFirstname() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (firstName != null) {
			sb.append(", ");
			sb.append(firstName);
		}
		return sb.toString();
	}

	public String toString() {
		return ClassUtils.printBean(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(User that) {
		// null-User werden nach oben sortiert
		if (this == null) {
			if (that == null)
				return 0;
			return 1;
		}
		if (that == null)
			return -1;

		String t1 = this.getCaption();
		String t2 = that.getCaption();
		if (t1 == null) {
			if (t2 == null)
				return 0;
			return 1;
		}
		if (t2 == null)
			return -1;
		return t1.compareToIgnoreCase(t2);
	}

}